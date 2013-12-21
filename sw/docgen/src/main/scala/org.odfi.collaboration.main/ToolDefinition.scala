package org.odfi.collaboration.main

import com.idyria.osi.ooxoo.core.buffers.datatypes.ClassBuffer
import com.idyria.osi.ooxoo.core.buffers.datatypes.XSDStringBuffer
import com.idyria.osi.ooxoo.core.buffers.structural.xelement
import com.idyria.osi.ooxoo.core.buffers.structural.xattribute

trait ToolDefinition extends App {

  /**
   * Basic description
   */
  var description: XSDStringBuffer = ""

  def arg(name: String): Argument = {
		new Argument(name)
  }

}

class Argument(
    @xattribute var name: XSDStringBuffer) {

  /**
   * Description
   */
  @xelement
  var description: XSDStringBuffer = null

  /**
   * Type of the argument, for specific checking
   */
  @xelement(name = "Type")
  var argumentType: ClassBuffer[_] = null

  def apply(closure: Argument => Any) = {
    closure(this)
  }
}