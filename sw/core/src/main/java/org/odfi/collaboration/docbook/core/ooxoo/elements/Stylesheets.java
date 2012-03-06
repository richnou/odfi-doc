package org.odfi.collaboration.docbook.core.ooxoo.elements;


import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxattribute;
import uni.hd.cag.ooxoo.core.wrap.OOXList;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NCNameBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.AnyURIBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.MaxOccursBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NonNegativeIntegerBuffer;


@Ooxnode(localName="Stylesheets",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
@Ooxelement()
public class Stylesheets extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public Stylesheets(){
		
	}
	@Ooxnode(localName="Description",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer Description = null;

	@Ooxnode(localName="Stylesheet",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet> Stylesheet = null;

	@Ooxnode(localName="id",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxattribute(use="required")
	private uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer id = null;

	/**
	 *
	 * @return value for the field : Description
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getDescription(Boolean create) {

		if (this.Description == null && create)
			Description= new uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer();
		return Description;
	}

	/**
	 *
	 * @return value for the field : Description
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getDescription() {
		return getDescription(false);
	}

	/**
	 *
	 * @param value for the field : Description
	 */
	public void setDescription(uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer Description) {
		this.Description=Description;
	}

	/**
	 *
	 * @return value for the field : Stylesheet
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet> getStylesheet(Boolean create) {

		/**
		 *
		 * This nested class is used to implement multiple constraint. It extends the OOXList type with the base type as parametrization class
		 *
		 */
		class _nestedmultiple extends OOXList<org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet> {

			public _nestedmultiple() {
				super(new NonNegativeIntegerBuffer(0),new MaxOccursBuffer(true));
			}

			public org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet add() {
				org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet o = new org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet();
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
	public uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet> getStylesheet() {
		return getStylesheet(false);
	}

	/**
	 *
	 * @param value for the field : Stylesheet
	 */
	public void setStylesheet(uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet> Stylesheet) {
		this.Stylesheet=Stylesheet;
	}

	/**
	 *
	 * @return value for the field : id
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getId(Boolean create) {

		if (this.id == null && create) {
			id= new uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer();
		}
		return id;
	}

	/**
	 *
	 * @return value for the field : id
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getId() {
		return getId(false);
	}

	/**
	 *
	 * @param value for the field : id
	 */
	public void setId(uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer id) {
		this.id=id;
	}

}
