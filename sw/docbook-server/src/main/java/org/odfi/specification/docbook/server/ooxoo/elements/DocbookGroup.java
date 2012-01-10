package org.odfi.specification.docbook.server.ooxoo.elements;


import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.wrap.OOXList;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NCNameBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.AnyURIBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.MaxOccursBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NonNegativeIntegerBuffer;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxattribute;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;


/**
 *
 * Contains A book or groups of books
 *
 */
@Ooxnode(localName="DocbookGroup",targetNamespace="urn:org:odfi:specification:docbook-server")
@Ooxelement()
public class DocbookGroup extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public DocbookGroup(){
		
	}
	@Ooxnode(localName="DocbookGroup",targetNamespace="urn:org:odfi:specification:docbook-server")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup> DocbookGroup = null;

	@Ooxnode(localName="Docbook",targetNamespace="urn:org:odfi:specification:docbook-server")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.Docbook> Docbook = null;

	@Ooxnode(localName="name",targetNamespace="urn:org:odfi:specification:docbook-server")
	@Ooxattribute(use="required")
	private uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer name = null;

	/**
	 *
	 * @return value for the field : DocbookGroup
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup> getDocbookGroup(Boolean create) {

		/**
		 *
		 * This nested class is used to implement multiple constraint. It extends the OOXList type with the base type as parametrization class
		 *
		 */
		class _nestedmultiple extends OOXList<org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup> {

			public _nestedmultiple() {
				super(new NonNegativeIntegerBuffer(0),new MaxOccursBuffer(true));
			}

			public org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup add() {
				org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup o = new org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup();
				this.addElement(o);
				return o;
			}
		}

		if (this.DocbookGroup==null)
			DocbookGroup= new _nestedmultiple();
		return DocbookGroup;
	}

	/**
	 *
	 * @return value for the field : DocbookGroup
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup> getDocbookGroup() {
		return getDocbookGroup(false);
	}

	/**
	 *
	 * @param value for the field : DocbookGroup
	 */
	public void setDocbookGroup(uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup> DocbookGroup) {
		this.DocbookGroup=DocbookGroup;
	}

	/**
	 *
	 * @return value for the field : Docbook
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.Docbook> getDocbook(Boolean create) {

		/**
		 *
		 * This nested class is used to implement multiple constraint. It extends the OOXList type with the base type as parametrization class
		 *
		 */
		class _nestedmultiple extends OOXList<org.odfi.specification.docbook.server.ooxoo.elements.Docbook> {

			public _nestedmultiple() {
				super(new NonNegativeIntegerBuffer(0),new MaxOccursBuffer(true));
			}

			public org.odfi.specification.docbook.server.ooxoo.elements.Docbook add() {
				org.odfi.specification.docbook.server.ooxoo.elements.Docbook o = new org.odfi.specification.docbook.server.ooxoo.elements.Docbook();
				this.addElement(o);
				return o;
			}
		}

		if (this.Docbook==null)
			Docbook= new _nestedmultiple();
		return Docbook;
	}

	/**
	 *
	 * @return value for the field : Docbook
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.Docbook> getDocbook() {
		return getDocbook(false);
	}

	/**
	 *
	 * @param value for the field : Docbook
	 */
	public void setDocbook(uni.hd.cag.ooxoo.core.wrap.OOXList<org.odfi.specification.docbook.server.ooxoo.elements.Docbook> Docbook) {
		this.Docbook=Docbook;
	}

	/**
	 *
	 * @return value for the field : name
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getName(Boolean create) {

		if (this.name == null && create) {
			name= new uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer();
		}
		return name;
	}

	/**
	 *
	 * @return value for the field : name
	 */
	public uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer getName() {
		return getName(false);
	}

	/**
	 *
	 * @param value for the field : name
	 */
	public void setName(uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer name) {
		this.name=name;
	}

}
