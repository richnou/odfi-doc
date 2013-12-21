package org.odfi.collaboration.out

import java.io.OutputStream
import java.io.PrintStream
import org.odfi.collaboration.ElementsSource
import com.idyria.osi.vui.impl.html.HtmlTreeBuilder
import com.idyria.osi.vui.impl.html.components.Table

class SingleFileHTMLOut(var outputStream: OutputStream) extends HtmlTreeBuilder {

  def produce(source: ElementsSource) = {

    var pr = new PrintStream(outputStream)

    /*p.println(s"""
<html>
       
        <head></head>
        <body>
        
        <ul>
    		${source.foundClasses.map(c => s"""<li>${c.name}</li>""").mkString}
        </ul>
        
    	${
      source.foundClasses.foreach {
        c =>

      }
    }
    
    
        </body>
        
</html>
    
    
""")*/

    var htmlRes = html {
      head {
        title("Doc for: " + source.sourcesName)
      }
      body {

        h1( source.sourcesName.toString)
        
        // Summary of classes
        //------------------------
        ul {
          source.foundClasses.foreach {
            c => li(a(c.name,s"#desc-class-${c.name}"))
          }
        }

        // Details
        //------------------
        source.foundClasses.foreach {
          c =>

            div {
              id(s"desc-class-${c.name}")
              h2(c.name)

              // Namespace
              //---------------
              if (c.namespace!=null) {  
	              div {
	
	                h5("Namespace: " + c.namespace.name)
	              }
              }
              // Inheritance
              //---------------
              if (c.inherits!=null) {      
	              div {
	                h3("Type hierarchy")
	                ul {
	                  c.inherits.types.foreach {
	                    t => li(text(t.name))
	                  }
	                }
	              }
              }
            
              if (c.description!=null) {
                
               
                  text(c.description.toString)
               
              }
           

              // Fields
              //--------------
              div {
                h3("Fields")
                table[Object] {
                  t =>
                    t.column("Name") {
                      c =>
                    }
                    t.column("Visibility") {
                      c =>
                    }
                }
              }

              // Methods
              //-----------------
              div {
                h3("Methods")

                table[Object] {
                  t =>
                    t.column("Name") {
                      c =>
                    }
                    t.column("Visibility") {
                      c =>
                    }
                }

              }

            }

        }

      }
    }

    pr.println(htmlRes.toString)
    pr.flush
    pr.close()

  }

}