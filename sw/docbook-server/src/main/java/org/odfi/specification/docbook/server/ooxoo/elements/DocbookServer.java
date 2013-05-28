package org.odfi.specification.docbook.server.ooxoo.elements;


import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxattribute;


@Ooxnode(localName="DocbookServer",targetNamespace="urn:org:odfi:specification:docbook-server")
@Ooxelement()
public class DocbookServer extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public DocbookServer(){
		
	}
	@Ooxnode(localName="DocbookGroup",targetNamespace="urn:org:odfi:specification:docbook-server")
	@Ooxelement()
	private org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup DocbookGroup = null;

	@Ooxnode(localName="StylesheetRepositories",targetNamespace="urn:org:odfi:specification:docbook-server")
	@Ooxelement()
	private org.odfi.specification.docbook.server.ooxoo.elements.StylesheetRepositories StylesheetRepositories = null;

	/**
	 *
	 * @return value for the field : DocbookGroup
	 */
	public org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup getDocbookGroup(Boolean create) {

		if (this.DocbookGroup == null && create)
			DocbookGroup= new org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup();
		return DocbookGroup;
	}

	/**
	 *
	 * @return value for the field : DocbookGroup
	 */
	public org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup getDocbookGroup() {
		return getDocbookGroup(false);
	}

	/**
	 *
	 * @param value for the field : DocbookGroup
	 */
	public void setDocbookGroup(org.odfi.specification.docbook.server.ooxoo.elements.DocbookGroup DocbookGroup) {
		this.DocbookGroup=DocbookGroup;
	}

	/**
	 *
	 * @return value for the field : StylesheetRepositories
	 */
	public org.odfi.specification.docbook.server.ooxoo.elements.StylesheetRepositories getStylesheetRepositories(Boolean create) {

		if (this.StylesheetRepositories == null && create)
			StylesheetRepositories= new org.odfi.specification.docbook.server.ooxoo.elements.StylesheetRepositories();
		return StylesheetRepositories;
	}

	/**
	 *
	 * @return value for the field : StylesheetRepositories
	 */
	public org.odfi.specification.docbook.server.ooxoo.elements.StylesheetRepositories getStylesheetRepositories() {
		return getStylesheetRepositories(false);
	}

	/**
	 *
	 * @param value for the field : StylesheetRepositories
	 */
	public void setStylesheetRepositories(org.odfi.specification.docbook.server.ooxoo.elements.StylesheetRepositories StylesheetRepositories) {
		this.StylesheetRepositories=StylesheetRepositories;
	}

}
