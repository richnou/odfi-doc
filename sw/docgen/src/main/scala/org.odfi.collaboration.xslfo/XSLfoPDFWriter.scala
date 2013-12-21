package org.odfi.collaboration.xslfo

import java.io.InputStream
import java.io.OutputStream
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.awt.geom.AffineTransform
import java.awt.font.FontRenderContext

trait XSLFOWriter {

  // Masters
  //-----------------
  var masters = Map[String, OPageMaster]()

  var currentMaster: OPageMaster = null

  // Font States
  //----------------

  var styleStack = scala.collection.mutable.Stack[OStyleContext]()

  def stackFont(reader: XMLStreamReader) = {

    // Stack a new context
    //-----------------------
    var styleContext = new OStyleContext

    // Fonts
    //------------------------

    //-- Create or use previous
    reader.getAttributeValue(null, "font-family") match {

      //  previous font
      case null =>

        styleContext.font = styleStack.head.font.clone

      case fontFamily =>
        styleContext.font = new OFont(fontFamily)

    }

    // Extras
    //----------------
    reader.getAttributeValue(null, "font-size") match {

      // Nothing
      case null =>
      case size =>

        styleContext.font.size = size.replaceAll("""[a-zA-Z]+""", "").toFloat

    }

    styleStack.push(styleContext)

  }

  def destackFont = {
    styleStack.pop
  }

  def currentStyle = styleStack.head

  /**
   * Parse Using SAX parser
   */
  def output(input: InputStream, output: OutputStream) = {

    // StreamoutState
    //---------------------

    // Create Parser
    //-------------------
    var reader = XMLInputFactory.newInstance().createXMLStreamReader(input)

    while (reader.hasNext()) {
      reader.next();

      // Structural
      //-----------------
      if (reader.isStartElement() || reader.isEndElement()) {

        reader.getLocalName() match {

          // Root
          //--------------
          case "root" if (reader.isStartElement()) =>

            // Font context
            stackFont(reader)

          case "root" if (reader.isEndElement()) =>

          // Page master : Creation
          //-------------------
          case "simple-page-master" if (reader.isEndElement()) =>
            currentMaster.finalizeRegions; currentMaster = null
          case "simple-page-master" if (reader.isStartElement()) =>

            //-- name is mandatory
            reader.getAttributeValue(null, "master-name") match {
              case null => throw new RuntimeException("A page master must have a @master-name attribute")
              case name =>
                var master = new OPageMaster(name)

                // Page width and height
                //--------------------------
                getAttributes(reader, "page-width", "page-height").foreach {
                  case ("page-width", value)  => master.width = ODimensions.sizeSpecToSize(value)
                  case ("page-height", value) => master.height = ODimensions.sizeSpecToSize(value)
                }

                // Margins
                //------------
                getAttributes(reader, "margin-left", "margin-right", "margin-top", "margin-bottom").foreach {
                  case ("margin-left", value) =>
                    println(s"Master maring left"); master.marginLeft = ODimensions.sizeSpecToSize(value)
                  case ("margin-right", value)  => master.marginRight = ODimensions.sizeSpecToSize(value)
                  case ("margin-top", value)    => master.marginTop = ODimensions.sizeSpecToSize(value)
                  case ("margin-bottom", value) => master.marginBottom = ODimensions.sizeSpecToSize(value)
                }

                // Save
                //----------------
                currentMaster = master
                masters = masters + (name -> master)
            }

          // Page Master : Regions
          //------------------------
          case region if (region.matches("region-.+")) =>

            var regionName = region.replace("region-", "")

            // Create 
            var pageRegion = currentMaster.createRegion(regionName)

          // Adapt Margins

          // Start Page Sequence
          //-------------------------
          case "page-sequence" if (reader.isEndElement()) => endPageSequence
          case "page-sequence" if (reader.isStartElement()) =>

            //-- master reference is mandatory
            reader.getAttributeValue(null, "master-reference") match {
              case null                              => throw new RuntimeException("A page sequence master must have a @master-reference attribute")
              case name if (!masters.contains(name)) => throw new RuntimeException(s"page sequence master $name does not exist")
              case name =>

                //-- Go
                currentMaster = masters(name)
                startPageSequence(currentMaster)

            }

          // Flow : Select location in output
          //-------------------
          case "flow" if (reader.isStartElement()) =>
            //-- flow name
            reader.getAttributeValue(null, "flow-name") match {
              case null => throw new RuntimeException("A flow must have a flow-name attribute")

              // Flow does not exist in current master
              case name if (currentMaster.getRegion(name.replace("xsl-region-", "")) == None) => throw new RuntimeException(s"flow $name does not exist in current master (${currentMaster.name}")

              case name =>

                //-- Go
                startFlow(currentMaster.getRegion(name.replace("xsl-region-", "")).get)

            }

          // Content: Block
          //--------------------
          case "block" if (reader.isEndElement()) => destackFont
          case "block" if (reader.isStartElement()) =>

            // Stack Style
            //------------------
            stackFont(reader)

            // handle page break
            //-------------------------
            getAttributes(reader, "page-break-before").foreach {
              _ match {
                case ("page-break-before", "always") =>
                  breakPage

                case _ =>
              }
            }

          // default
          case _ =>

        }

      } // EOF structural

      // Text
      //--------------
      if (reader.isCharacters()) {
        writeCharacters(reader.getText())
      }

    }

  }

  def getAttributes(reader: XMLStreamReader, names: String*): Map[String, String] = {

    names.map { v => (v -> reader.getAttributeValue(null, v)) }.filterNot { case (k, v) => v == null }.toMap[String, String]
  }

  // Implementation
  //-----------------------
  def endPageSequence
  def startPageSequence(master: OPageMaster)
  def breakPage
  def startFlow(area: OPageRegion)

  //-- Blocks
  def startBlock(block: OBlock)

  //-- Text write

  /**
   * Warning, this might be call outside of correct text area (because of XML)
   */
  def writeCharacters(chars: String)

}

class XSLfoPDFWriter extends XSLFOWriter {

  var document = new PDDocument();

  // Text handling
  //-------------------
  def breakText(font: PDType1Font, text: String, keepWhitespaces: Boolean = false): Array[String] = {

    var af = new AffineTransform();
    var fr = new FontRenderContext(af, true, true);
    var maxBounds = font.getawtFont().getMaxCharBounds(fr)
    var maxCharWidth = maxBounds.getWidth()
    var charHeight = maxBounds.getHeight() + Math.abs(maxBounds.getY())

    //println(s"Average char width: " + maxBounds)
    //println(s"Average char charHeight: " + charHeight)

    var currentWidth: Double = 0
    var maxWidth = currentPage.getBleedBox().getUpperRightX()
    var currentGroup = 0

    var interline = charHeight + 5

    // WhiteSpace handling
    //-------------------------
    var resText = text
    if (!keepWhitespaces) {
      resText = text.replaceAll("""\s*\r?\n\s*""", "")
    }

    println(s"Breaking text:" + resText)

    // Processing
    //----------------
    resText.split("""\r?\n""").map {
      line =>

        var brokenLines = line.groupBy {

          actualChar =>

            var charWidth = maxCharWidth
            (currentWidth + charWidth) match {

              // Break
              case upcomingWidth if (upcomingWidth > maxWidth) =>

                currentGroup += 1
                currentWidth = charWidth

              // Stay at same index
              case _ =>
                //println(s"Breaking")
                currentWidth += charWidth
            }

            currentGroup
        }

        // Return the sorted list of brokenlines
        brokenLines.toSeq.sortWith { (a, b) => a._1 < b._1 }.map(e => e._2).toArray
    }.flatten

  }

  // Implementation
  //---------------------

  //-- Page
  var currentPage: PDPage = null
  var currentPageStream: PDPageContentStream = null
  var currentPosition: Tuple2[Float, Float] = (0, 0)

  def startPageSequence(master: OPageMaster) = {

    //-- Create Page
    currentPage = new PDPage(new PDRectangle(master.width, master.height));
    document.addPage(currentPage)

    //-- Set Bleed box from margin
    var inMargins = master.getAreaInMargins
    currentPage.setBleedBox(new PDRectangle(inMargins.width, inMargins.height))
    currentPage.getBleedBox().setLowerLeftX(inMargins.x)
    currentPage.getBleedBox().setLowerLeftY(inMargins.y)
    currentPage.getBleedBox().setUpperRightX(inMargins.x.data + inMargins.width)
    currentPage.getBleedBox().setUpperRightX(inMargins.y.data + inMargins.height)

    currentPageStream = new PDPageContentStream(document, currentPage);

    println(s"Added page with dimensions: " + currentPage.findCropBox())
    println(s"Added page with inbox: " + currentPage.getBleedBox() + " x should be: " + inMargins.x)

  }

  def breakPage = {
    startPageSequence(currentMaster)
    startFlow(currentMaster.getRegion("body").get)
  } 

  def endPageSequence = {
    currentPageStream.close()
    currentPageStream = null
  }

  /**
   * Move current Cursor to flow begin
   */
  def startFlow(area: OPageRegion) = {

    //currentPageStream.moveTo(area.x, area.y.data + area.height)
    currentPageStream.moveTo(currentPage.getBleedBox().getLowerLeftX(), currentPage.getBleedBox().getLowerLeftY() + currentPage.getBleedBox().getHeight())

    currentPosition = (currentPage.getBleedBox().getLowerLeftX(), currentPage.getBleedBox().getLowerLeftY() + currentPage.getBleedBox().getHeight())

    currentPosition = (area.x, area.y.data + area.height)

    //currentPageStream.moveTextPositionByAmount(currentPage.getBleedBox().getLowerLeftX(), currentPage.getBleedBox().getLowerLeftY() + currentPage.getBleedBox().getHeight())

    //-- Start text
    currentPageStream.beginText()

    // Position cursor
    currentPageStream.moveTextPositionByAmount(currentPosition._1, currentPosition._2)

    //-- end text
    currentPageStream.endText()

  }

  def startBlock(block: OBlock) = {

    currentPosition = (currentPosition._1, currentPosition._2 - block.paddingTop)

  }

  def writeCharacters(chars: String) = {

    if (currentPageStream != null) {

      var font = PDType1Font.TIMES_ROMAN
      currentPageStream.setFont(font, currentStyle.font.size)

      //-- Start text
      currentPageStream.beginText()

      // Position cursor
      currentPageStream.moveTextPositionByAmount(currentPosition._1, currentPosition._2)

      var interline = 8

      breakText(font, chars).foreach {
        l =>
          //println(s"Outputing line: "+l._1)
          currentPageStream.moveTextPositionByAmount(0, -interline.ceil.toInt)
          currentPosition = (currentPosition._1, currentPosition._2 - interline.ceil.toInt)

          currentPageStream.drawString(l)

      }

      //println(s"Writing text from x: " + currentPage.getBleedBox().getLowerLeftX())

      //currentPageStream.drawString(chars)

      //-- end text
      currentPageStream.endText()
    }

  }

  override def output(input: InputStream, output: OutputStream) = {

    // Prepare output
    //--------------------
    //var document = new PDDocument();

    // Generic Call
    //---------------------
    super.output(input, output)

    // Save Output
    //----------------------
    document.save(output)
    document.close();

  }

}