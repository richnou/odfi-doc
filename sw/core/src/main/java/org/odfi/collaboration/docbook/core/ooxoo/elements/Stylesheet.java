package org.odfi.collaboration.docbook.core.ooxoo.elements;


import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxnode;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxelement;
import uni.hd.cag.ooxoo.core.ElementBuffer;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;
import uni.hd.cag.ooxoo.core.wrap.annotations.Ooxattribute;


@Ooxnode(localName="Stylesheet",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
@Ooxelement()
public class Stylesheet extends ElementBuffer{


	/**
	 * This is the default empty constructor
	 */
	public Stylesheet(){
		
	}
	@Ooxnode(localName="Description",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer Description = null;

	@Ooxnode(localName="Output",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private org.odfi.collaboration.docbook.core.ooxoo.ctypes.OutputType Output = null;

	@Ooxnode(localName="PostProcessing",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private org.odfi.collaboration.docbook.core.ooxoo.ctypes.PostProcessingType PostProcessing = null;

	@Ooxnode(localName="TransformChain",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxelement()
	private org.odfi.collaboration.docbook.core.ooxoo.ctypes.TransformChainType TransformChain = null;

	@Ooxnode(localName="name",targetNamespace="http://www.idyria.com/osi/docbook/stylesheets")
	@Ooxattribute(use="required")
	private uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer name = null;

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
	 * @return value for the field : Output
	 */
	public org.odfi.collaboration.docbook.core.ooxoo.ctypes.OutputType getOutput(Boolean create) {

		if (this.Output == null && create)
			Output= new org.odfi.collaboration.docbook.core.ooxoo.ctypes.OutputType();
		return Output;
	}

	/**
	 *
	 * @return value for the field : Output
	 */
	public org.odfi.collaboration.docbook.core.ooxoo.ctypes.OutputType getOutput() {
		return getOutput(false);
	}

	/**
	 *
	 * @param value for the field : Output
	 */
	public void setOutput(org.odfi.collaboration.docbook.core.ooxoo.ctypes.OutputType Output) {
		this.Output=Output;
	}

	/**
	 *
	 * @return value for the field : PostProcessing
	 */
	public org.odfi.collaboration.docbook.core.ooxoo.ctypes.PostProcessingType getPostProcessing(Boolean create) {

		if (this.PostProcessing == null && create)
			PostProcessing= new org.odfi.collaboration.docbook.core.ooxoo.ctypes.PostProcessingType();
		return PostProcessing;
	}

	/**
	 *
	 * @return value for the field : PostProcessing
	 */
	public org.odfi.collaboration.docbook.core.ooxoo.ctypes.PostProcessingType getPostProcessing() {
		return getPostProcessing(false);
	}

	/**
	 *
	 * @param value for the field : PostProcessing
	 */
	public void setPostProcessing(org.odfi.collaboration.docbook.core.ooxoo.ctypes.PostProcessingType PostProcessing) {
		this.PostProcessing=PostProcessing;
	}

	/**
	 *
	 * @return value for the field : TransformChain
	 */
	public org.odfi.collaboration.docbook.core.ooxoo.ctypes.TransformChainType getTransformChain(Boolean create) {

		if (this.TransformChain == null && create)
			TransformChain= new org.odfi.collaboration.docbook.core.ooxoo.ctypes.TransformChainType();
		return TransformChain;
	}

	/**
	 *
	 * @return value for the field : TransformChain
	 */
	public org.odfi.collaboration.docbook.core.ooxoo.ctypes.TransformChainType getTransformChain() {
		return getTransformChain(false);
	}

	/**
	 *
	 * @param value for the field : TransformChain
	 */
	public void setTransformChain(org.odfi.collaboration.docbook.core.ooxoo.ctypes.TransformChainType TransformChain) {
		this.TransformChain=TransformChain;
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
