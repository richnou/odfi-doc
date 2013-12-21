package org.odfi.collaboration.main

import org.odfi.collaboration.tcl.TCLFile
import java.io.File
import org.odfi.collaboration.out.SingleFileMarkdownOut
import java.io.FileOutputStream

object DocGen extends ToolDefinition {

  println(s"Welcome to DocGen")

  description = "Doc Gen Reads some source files and generates documentation"

  var currentArg = ""
  var resArguments = scala.collection.mutable.Map[String, Iterable[String]]()

  var grouped = args.grouped(2)

  def getArg(name: String): Iterable[String] = {

    args.grouped(2).collect { case pair if (pair(0).trim.startsWith(s"--$name")) =>  pair(1) }.toIterable match {
      case r if (r.size == 0) =>
        println(s"Required --$name arg has not been provided")
        sys.exit(-1)
      case r =>  r
    }
  }

  // Find Generator type
  //-----------------
  
  var supportedGenerators = Map("markdown" -> new SingleFileMarkdownOut)
  var generators = getArg("generator")

  // Output Destination
  //-----------------------
  var output = getArg("output")

  output.foreach {
    out => 
      
      println(s"Selected Output: "+out)
  }
  
  // Remaining Files
  //--------------------
  var files = grouped.collect { case pair if (!pair(0).startsWith("-")) => pair }.flatten.toIterable

  // Parse : Filter non existing
  //------------------
  
  
  
  //-- Report non existing
  files.filterNot(new File(_).exists()).foreach {
    nf => println("Warning, file $f does not exist, not parsing")
  }
  
  println(s"Files: ${files.mkString(",")}")
  
  
  files.collect{case f if(new File(f).exists()) => new File(f)}.foreach {
    f =>
      println(s"Parsing File: $f")
     
      
      // Only Support TCL right now
      //------------------------------
      var tclfile = new TCLFile
      tclfile.parse(f.getAbsolutePath())

      // Generate all outputs
      
      // Output, only support inplace and path for now
      //-----------------
      output.foreach {
        out =>
          
          // Determine output file
          //----------
          var outputFile = out match {

            // Inplace
            //--------------
            case "inplace" =>
            	
         
              var targetFile = new File(f.getParentFile(),s"${f.getName()}.md")
              targetFile
              //generators.map(supportedGenerators.get(_))
              
            // Directory
            //----------------
            case d if (new File(d).isDirectory() && new File(d).exists()) =>
              
              new File(d,s"${f.getName()}.md")
              
            // File
            //----------------
            case of if (new File(of).isFile()) => new File(of)

            // Error 
            //-----------
            case _ =>
              println(s"""Provided output: $out must be an existing directory, a file, or the "inplace" value """)
              sys.exit(-1)
          }
          
          // Write output
          //-----------------
          println("Generating output to: "+outputFile.getAbsolutePath())
          supportedGenerators("markdown").produce(tclfile,new FileOutputStream(outputFile))
              
      }

  }

}