package org.odfi.collaboration.out

import java.io.OutputStream
import java.io.PrintStream
import org.odfi.collaboration.ElementsSource
import com.idyria.osi.vui.impl.html.HtmlTreeBuilder
import com.idyria.osi.vui.impl.html.components.Table
import org.odfi.collaboration.models.typehierarchy.Namespace
import org.odfi.collaboration.models.typehierarchy.Class
import org.odfi.collaboration.models.typehierarchy.Field
import org.odfi.collaboration.models.typehierarchy.Method

class SingleFileMarkdownOut extends HtmlTreeBuilder {

  def produce(source: ElementsSource,outputStream: OutputStream) = {

    var pr = new PrintStream(outputStream)

    var md = s"""

${source.sourcesName.toString}
=============================

    

# Classes
${
  // Classes ////////////////////
      source.foundClasses.map {
        c =>
s"""
## ${c.name}

${
            // Namespace
            //-----------------
            c.namespace match {
              case null => "";
              case ns   => s"Namespace: ${ns.name.toString}\n"
            }
          }
${
			// Description
			//-------------------------
			c.description match {
			  case null => ""
			  case desc => s"Description:\n$desc"
			}
}

${
            // Inheritance
            //----------------
            c.inherits match {
              case null     => ""
              case inherits => inherits.types.map(t => s"- $t").mkString("\n")
            }
          }

### Fields
${
fieldsTable(c)
}


### Methods
${methodsTable(c)}
        
        """

      }.mkString("\n")
    }



    
    """

    pr.print(md)
    pr.flush
    pr.close()

  }

  def fieldsTable(c: Class): String = {
    table[Field] {
      t =>
        t.column("Name") {
          c => 
            c.content {
              f => f.name
            }
        }
        t.column("Visibility") {
          c =>
            
            c.content {
              f => f.visibility
            }
        }
        t.column("Description") {
          c =>
            
            c.content {
              f => f.description
            }
        }
        
        // Add
        c.fields.foreach(t.add(_))
        
    }.toString()
  }

  def methodsTable(c: Class): String = {

    table[Method] {
      t =>
        t.column("Name") {
          c => 
            c.content {
              m => m.name
            }
        }
        t.column("Visibility") {
          c =>
            
            c.content {
              m=> m.visibility
            }
        }
        t.column("Description") {
          c =>
            
            c.content {
              m => m.description
            }
        }
        
        // Add
        c.methods.foreach(t.add(_))
        
    }.toString()
  }

}