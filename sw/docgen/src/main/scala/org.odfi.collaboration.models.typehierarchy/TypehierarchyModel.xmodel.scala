package org.odfi.collaboration.models.typehierarchy

import com.idyria.osi.ooxoo.model.ModelBuilder
import com.idyria.osi.ooxoo.model.producers
import com.idyria.osi.ooxoo.model.producer
import com.idyria.osi.ooxoo.model.out.markdown.MDProducer
import com.idyria.osi.ooxoo.model.out.scala.ScalaProducer

@producers(Array(
  new producer(value = classOf[ScalaProducer]),
  new producer(value = classOf[MDProducer])
))
object TypehierarchyModel extends ModelBuilder {

  // General
  //--------------
 
  var namespace = "Namespace" is {
	  
    withTrait("Description")

  }

  var typeElement = "Type" is {
    withTrait("Description")
    
    importElement(namespace)
  }
  
  "Class" is {

    classType("Type")
   
    "Inherits" is {
      classType(typeElement)
      importElement(typeElement).setMultiple
    } 
    
    // SubClasses
    //------------------
    importElement("Class").setMultiple
    
    // Fields
    //-------------
    "Field" multiple {
      withTrait("Description")
      
      attribute("visibility")
      
    }
    // Methods
    //---------------
    "Method" multiple {
      withTrait("Description")
      
      attribute("visibility")
      
    }  
    
    
  }

}