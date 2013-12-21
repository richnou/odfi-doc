package org.odfi.collaboration.xslfo

import com.idyria.osi.ooxoo.core.buffers.datatypes.XSDStringBuffer
import com.idyria.osi.ooxoo.core.buffers.datatypes.IntegerBuffer
import com.idyria.osi.ooxoo.core.buffers.datatypes.DoubleBuffer
import com.idyria.osi.ooxoo.core.buffers.datatypes.FloatBuffer

// Style
//--------------------

class OStyleContext {
  
  var font : OFont = null
}

class OFont(var name: XSDStringBuffer) {

  var size: FloatBuffer = 12.0f

  override def clone : OFont = {
    
    var cl = new OFont(name)
    cl.size = size
    
    cl
  }
  
}

// Masters And Areas
//-------------------------

class OArea {

  var width: FloatBuffer = 0
  var height: FloatBuffer = 0

  var x: FloatBuffer = 0
  var y: FloatBuffer = 0

  var marginLeft: FloatBuffer = 0
  var marginRight: FloatBuffer = 0
  var marginTop: FloatBuffer = 0
  var marginBottom: FloatBuffer = 0

  /**
   * Returns an OArea reprenting the area inside margins, with not margins then and appropriate x/y positions
   */
  def getAreaInMargins: OArea = {

    var result = new OArea
    
    result.width = width - (marginRight.data + marginLeft.data)
    result.height = height - (marginTop.data + marginBottom.data)

    println(s"Gett area in margins, "+marginLeft)
    
    result.x = marginLeft
    result.y = marginBottom

    result
  }

}

class OPageMaster(var name: XSDStringBuffer) extends OArea {

  var regions = Map[String, OPageRegion]()

  /**
   * Creates a Region, wich is first an area with default positions as the in margins area of the page
   *
   * Depending on the region name adapt the other regions
   */
  def createRegion(regionName: String): OPageRegion = {

    var inmargins = getAreaInMargins

    // Create
    //--------------------
    var pageRegion = new OPageRegion(regionName)

    // Record values
    pageRegion.width = inmargins.width
    pageRegion.height = inmargins.height
    pageRegion.x = inmargins.x
    pageRegion.y = inmargins.y

    regions = regions + (regionName -> pageRegion)

    pageRegion
  }

  def getRegion(regionName: String): Option[OPageRegion] = {

    regions.get(regionName)

  }

  /**
   * Realign correctly regions
   */
  def finalizeRegions = {

    regions.foreach {
      case ("body", pageRegion) =>

        pageRegion.y.data += ODimensions.sizeSpecToSize("2cm")
        pageRegion.height.data -= ODimensions.sizeSpecToSize("2cm")

      case ("after", pageRegion) =>
        pageRegion.height = ODimensions.sizeSpecToSize("2cm")
      case _ =>
    }

  }

}

class OPageRegion(var name: XSDStringBuffer) extends OArea {

}


class OBlock {
  
  var paddingTop: FloatBuffer = 0
  var paddingBottom: FloatBuffer = 0
  
}

object ODimensions {

  val defaultDPI = 96

  def sizeSpecToSize(spec: String): Float = {

    // Direct size
    //------------
    if (spec.matches("[0-9]+")) {
      return spec.toInt
    } 
    // cm 
    //----------------
    else if (spec.matches(".+cm")) {
      var res = (spec.replace("cm", "").toFloat / 2.54 * defaultDPI).floor.toFloat
      
      println(s"Converted $spec to $res")
      
      return res
    } 
    
    // mm 
    //----------------
    else if (spec.matches(".+mm")) {
      return ((spec.replace("m", "").toFloat/10) / 2.54 * defaultDPI).floor.toFloat
    } else {
      throw new RuntimeException(s"Unsupported size format: $spec ")
    }

  }

}