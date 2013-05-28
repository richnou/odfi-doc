package org.odfi.specification.docbook.server.ooxoo.elements;


import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;


/**
 *
 * Contains path to a docbook top
 *
 */
@Ooxnode(localName="Docbook",targetNamespace="urn:org:odfi:specification:docbook-server")
@Ooxelement()
public class Docbook extends uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer {


	/**
	 * This is the default empty constructor
	 */
	public Docbook(){
		
	}
	/**
	 * This is the XSD String constructor
	 */
	public Docbook(java.lang.String str) {
		super(str);
	}
}
