package org.odfi.collaboration.docbook.core.ooxoo.ctypes;


import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.MaxOccursBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NonNegativeIntegerBuffer;
import uni.hd.cag.ooxoo.core.wrap.OOXList;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;


/**
 *
 * 
 * Output configuration for a stylesheet
 * 
 *
 */
public class OutputType extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public OutputType(){
		
	}
	@Ooxnode(localName="Path",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer Path = null;

	@Ooxnode(localName="File",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> File = null;

	/**
	 *
	 * @return value for the field : Path
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getPath(Boolean create) {

		if (this.Path == null && create)
			Path= new uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer();
		return Path;
	}

	/**
	 *
	 * @return value for the field : Path
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getPath() {
		return getPath(false);
	}

	/**
	 *
	 * @param value for the field : Path
	 */
	public void setPath(uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer Path) {
		this.Path=Path;
	}

	/**
	 *
	 * @return value for the field : File
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> getFile(Boolean create) {

		/**
		 *
		 * This nested class is used to implement multiple constraint. It extends the OOXList type with the base type as parametrization class
		 *
		 */
		class _nestedmultiple extends OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> {

			public _nestedmultiple() {
				super(new NonNegativeIntegerBuffer(0),new MaxOccursBuffer(true));
			}

			public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer add() {
				uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer o = new uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer();
				this.addElement(o);
				return o;
			}
		}

		if (this.File==null)
			File= new _nestedmultiple();
		return File;
	}

	/**
	 *
	 * @return value for the field : File
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> getFile() {
		return getFile(false);
	}

	/**
	 *
	 * @param value for the field : File
	 */
	public void setFile(uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> File) {
		this.File=File;
	}

}
