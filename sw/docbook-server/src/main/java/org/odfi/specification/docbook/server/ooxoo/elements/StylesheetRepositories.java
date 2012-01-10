package org.odfi.specification.docbook.server.ooxoo.elements;


import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;
import uni.hd.cag.ooxoo.core.wrap.OOXList;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NCNameBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.AnyURIBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.MaxOccursBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.NonNegativeIntegerBuffer;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxattribute;


@Ooxnode(localName="StylesheetRepositories",targetNamespace="urn:org:odfi:specification:docbook-server")
@Ooxelement()
public class StylesheetRepositories extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public StylesheetRepositories(){
		
	}
	@Ooxnode(localName="Repository",targetNamespace="urn:org:odfi:specification:docbook-server")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> Repository = null;

	/**
	 *
	 * @return value for the field : Repository
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> getRepository(Boolean create) {

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

		if (this.Repository==null)
			Repository= new _nestedmultiple();
		return Repository;
	}

	/**
	 *
	 * @return value for the field : Repository
	 */
	public uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> getRepository() {
		return getRepository(false);
	}

	/**
	 *
	 * @param value for the field : Repository
	 */
	public void setRepository(uni.hd.cag.ooxoo.core.wrap.OOXList<uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer> Repository) {
		this.Repository=Repository;
	}

}
