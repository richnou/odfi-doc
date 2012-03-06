package org.odfi.collaboration.docbook.core.ooxoo.ctypes;


import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;
import uni.hd.cag.ooxoo.core.wrap.OOXList;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NCNameBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.AnyURIBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.MaxOccursBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NonNegativeIntegerBuffer;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxattribute;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;


/**
 *
 * 
 * A list of stylesheets to apply one after another
 * 
 *
 */
public class TransformChainType extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public TransformChainType(){
		
	}
	@Ooxnode(localName="Stylesheet",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> Stylesheet = null;

	/**
	 *
	 * @return value for the field : Stylesheet
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> getStylesheet(Boolean create) {

		/**
		 *
		 * This nested class is used to implement multiple constraint. It extends the OOXList type with the base type as parametrization class
		 *
		 */
		class _nestedmultiple extends OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> {

			public _nestedmultiple() {
				super(new NonNegativeIntegerBuffer(1),new MaxOccursBuffer(true));
			}

			public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer add() {
				uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer o = new uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer();
				this.addElement(o);
				return o;
			}
		}

		if (this.Stylesheet==null)
			Stylesheet= new _nestedmultiple();
		return Stylesheet;
	}

	/**
	 *
	 * @return value for the field : Stylesheet
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> getStylesheet() {
		return getStylesheet(false);
	}

	/**
	 *
	 * @param value for the field : Stylesheet
	 */
	public void setStylesheet(uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> Stylesheet) {
		this.Stylesheet=Stylesheet;
	}

}
