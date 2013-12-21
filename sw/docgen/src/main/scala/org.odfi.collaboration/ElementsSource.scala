package org.odfi.collaboration

import com.idyria.osi.ooxoo.core.buffers.structural.XList
import org.odfi.collaboration.models.typehierarchy.Class
import com.idyria.osi.ooxoo.core.buffers.datatypes.XSDStringBuffer

trait ElementsSource {

  /**
   * Base Name for this source, could be file name etc...
   */
  var sourcesName: XSDStringBuffer = ""

  var foundClasses = XList { Class() }

}