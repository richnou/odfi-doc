package org.odfi.collaboration.tcl

import java.io.FileReader
import scala.util.parsing.combinator.PackratParsers
import scala.util.parsing.combinator.RegexParsers
import org.odfi.collaboration.models.typehierarchy.Namespace
import org.odfi.collaboration.models.typehierarchy.Class
import com.idyria.osi.ooxoo.core.buffers.structural.XList
import scala.util.parsing.input.StreamReader
import java.io.StringReader
import org.odfi.collaboration.ElementsSource
import java.io.File
import org.odfi.collaboration.models.typehierarchy.Field
import org.odfi.collaboration.models.typehierarchy.Method
import org.odfi.collaboration.models.typehierarchy.Inherits
import org.odfi.collaboration.models.typehierarchy.Type

class TCLFile extends RegexParsers with PackratParsers with ElementsSource {

 /* def top : Parser[String] = packageDefinition.* ~ (namespace).* ^^ {
    r => r.toString
  }
  */
  //----------------------------
  // Stacks and so
  //-------------------------
  var namespaces = scala.collection.mutable.Stack[Namespace]()
  
  var classes = scala.collection.mutable.Stack[Class]()
  

  
  
  ///////////////////////////////
  // Common
  ///////////////////////////////
  lazy val identifier : PackratParser[String] = """[\w:-_"]+""".r ^^ {r => r}
  
  lazy val unknownline : PackratParser[String] = """[^}].*[^{]?\r?\n""".r ^^ {
    r => 
      //println(s"Found unknownline: "+r)
      r
  }
  
  lazy val ignoreLine: PackratParser[_] = ( ( (""".*\{\r?\n""".r ^^ {s => println(s"Found block start: "+s.trim());s}) ~ ignoreLine.* ~ "}") ^^ {
    
    r  => 
      println(s"EOF block")
      r
     
  } | unknownline) ^^ {
    r => 
      println(s"Found Unknown line: "+r.toString().trim())
      
      r
  }
  
  lazy val commentLine  : PackratParser[String] = """#+.*\r?\n""".r ^^ {r => r}
  lazy val commentsBlock : PackratParser[_] = ("""(\s*#+.*\r?\n)+""".r) ^^ {
    r => 
      
      // Take last block comment to remove comments blocks that would be separater by empty lines
      var blockComment = r.split("""\r?\n\s+\r?\n""").last.trim
    
      // Remove # Markers
      //--------------------
      blockComment = blockComment.replaceAll("""#+""", "").trim
      
      blockComment
      
  }
  ///////////////////////////////
  // Tops
  ///////////////////////////////
  
  
  def top : PackratParser[String] = (commentLine | packageDefinition | namespace).* ^^ {
    r => r.toString
  }
  
  def packageDefinition : PackratParser[String] = ("""^package\s+""".r ) ~>( ( """provide\s+""".r | """require\s+""".r) ~ identifier ~ ("""[0-9\.]+""".r).? ) ^^ {
    r => 
      println(s"Package: "+r)
      r.toString
  }
  
  def namespace: PackratParser[String] = "namespace" ~ "eval" ~> (identifier ^^ {
    r => 
      println(s"Found namespace: " + r)
      var ns = new Namespace
      ns.name = r
      namespaces.push(ns)
      r
  }) <~ "{" ~ (content) ~ "}"
  
  ///////////////////////////////
  // Content
  ///////////////////////////////
  lazy val content : PackratParser[String] = (itclClass | proc | codeContent).* ^^ {
    r => ""
  }
  
  lazy val proc : PackratParser[String] = commentsBlock.? ~ ("proc" ~> (identifier ~ args)) <~ block  ^^ {
    r => 
      
      println(s"Found Procedure: "+r._2._1)
      ""
  }
  
  /////////////////////////////////
  // Code content
  ////////////////////////////////
  //lazy val codeContent : PackratParser[_] = (unknownline | ifelse).*
  
  
  /*lazy val ifelse : PackratParser[_] = ifRule ~ codeContent.? ~ ( ((elseifRule ~ codeContent ~ "}").* ~ (elseRule ~ codeContent ~ "}").?) | "}")
  
  lazy val ifRule  = """if\s+{.+}\s+{""".r
 
  lazy val elseifRule= """}\s+elseif\s+{.+}\s+{""".r
  
  lazy val elseRule  = """}\s+else\s+{""".r*/
  
  lazy val codeContent : PackratParser[_] = (someContent | block).+
  
  lazy val someContent : PackratParser[_] = """[^{}]+""".r ^^ {
    r => 
      //println(s"Found some content: "+r.trim())
      r
  }
  
  lazy val block : PackratParser[_] = "{" ~ (codeContent).? ~"}"
  
  ///////////////////////////////
  // ITCL
  ///////////////////////////////
  lazy val itclClass : PackratParser[_] = (commentsBlock.? ~ "itcl::class" ~ identifier ^^ {
   r =>  
     // Create Class
      //----------------
      var cl = new Class
      cl.name = r._2
      
      // Description
      //-----------------
      r._1._1 match {
        case Some(d) => cl.description = d.toString 
        case _ => 
      }
      
      // Add to Stack
      //------------------
      
      // FIXME: Nested classes are not supported already
      
      //-- Destack existing
      classes.size match {
        case 0 => 
        case _ => classes.pop
      }
      
      //-- Add Class
      var level = classes.size
      classes.push(cl)
      foundClasses+=cl
      
      //-- NS ? 
      namespaces.headOption match {
        case Some(ns) => cl.namespace = ns
        case None => 
      }
      
      println(s"[$level] Class: "+r)
      
     r
  }) <~ "{"  ~ (itclInherit | itclMethod|itclField | odfiItclField |itclConstructor|codeContent).* ~ "}"
  
  //~ itclInherit.?
  
  //-- INheritance
  
  lazy val itclInherit  : PackratParser[_] = "inherit" ~> identifier  ^^ {
    r => 
      println(s"-- Inherits "+r)
      classes.headOption match {
        case Some(cl) =>
          
          // Creates INheritance container if not present
          cl.inherits match {
            case null => cl.inherits = Inherits()
            case _ =>
          }
          
          // Record into list as type information
          cl.inherits.types += Type()
          cl.inherits.types.last.name = r.toString()
          
        case None => 
      }
      r
  }
  
  //-- Methods
  //---------------------
  lazy val args  : PackratParser[_] = (identifier | ("{" ~ (args.*) ~ "}") )
  lazy val itclMethod  : PackratParser[_] = commentsBlock.? ~ ("public" | "protected" | "private") ~ ("method" ~> (identifier ~ args)) <~ block ^^ {
    r => 
      //println(s"-- Found method: "+r)
      
      classes.headOption match {
        case Some(cl) =>
          
          // Create Field
          //-----------------
          cl.methods += Method()
          var method = cl.methods.last
          
          //-- Desc
          r._1._1 match {
            case Some(d) => method.description = d.toString
            case  _ => 
          }
          
          //-- Visibility
          method.visibility = r._1._2
          
          //-- Name
          method.name = r._2._1
          
        case None => 
      }
      
      r
  }
  
  lazy val itclConstructor : PackratParser[_] = "constructor" ~> (args ~ (args).?) <~ block ^^ {
    
    r => 
      
      //println("-- Found constructor")
      r
  }
  
  
  //-- Fields
  //-- TCL Fields and ODFI fields
  //------------------
  lazy val itclField  : PackratParser[_] = commentsBlock.? ~ ("public" | "protected" | "private") ~ ("variable" ~> identifier <~ """.+\r?\n""".r) ^^ {
    r => 
      //println(s"-- Found field: "+r)
      
      classes.headOption match {
        case Some(cl) =>
          
          // Create Field
          //-----------------
          cl.fields += Field()
          var field = cl.fields.last
          
          //-- Desc
          r._1._1 match {
            case Some(d) => field.description = d.toString
            case  _ => 
          }
          
          //-- Visibility
          field.visibility = r._1._2
          
          //-- Name
          field.name = r._2
          
        case None => 
      }
      
      r
  }
  
  lazy val odfiItclField = commentsBlock.? ~ ("odfi::common::classField" ~> ("public" | "protected" | "private")) ~ (identifier <~ """.+\r?\n""".r) ^^ {
    r => 
      //println(s"-- Found field: "+r)
      
      classes.headOption match {
        case Some(cl) =>
          
          // Create Field
          //-----------------
          cl.fields += Field()
          var field = cl.fields.last
          
          //-- Desc
          r._1._1 match {
            case Some(d) => field.description = d.toString
            case  _ => 
          }
          
          //-- Visibility
          field.visibility = r._1._2
          
          //-- Name
          field.name = r._2
          
        case None => 
      }
      
      r
  }
  
  
  def testMethod(str: String) = {
    
    itclMethod(new PackratReader(StreamReader(new StringReader(str))))match {
      case Success(result, _) => result
      case failure: NoSuccess => 
        
        println(s"Failure: "+failure)
    }
    
  }
  
  //-- Commendts
  //--------------------
  def cleanComments(str: String) : String = {
    str.replaceAll("^#+", "")
  }
  
  def parse(str: String) : Unit = {

    // Read File
    //-----------------
    var file = new File(str)
    this.sourcesName = file.getName
    parseAll(top,new PackratReader(StreamReader(new FileReader(file)))) match {
      case Success(result, _) => result
      case failure: NoSuccess => 
        
        println(s"Failure: "+failure)
    }
  }

}