package org.odfi.collaboration.models.typehierarchy

import com.idyria.osi.ooxoo.model.ModelBuilder
import com.idyria.osi.ooxoo.model.ModelBuilder
import com.idyria.osi.ooxoo.model.producers
import com.idyria.osi.ooxoo.model.producer
import com.idyria.osi.ooxoo.model.out.markdown.MDProducer
import com.idyria.osi.ooxoo.model.out.scala.ScalaProducer

@producers(Array(
  new producer(value = classOf[ScalaProducer]),
  new producer(value = classOf[MDProducer])
))
object CommonModel extends ModelBuilder {
  
  // General
  //--------------
  "Description" is {
    isTrait
    attribute("name") is "The name of the object"
    "Description" ofType("cdata") 
  }
  
  
  
}