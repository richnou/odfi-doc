/**
 * 
 */
package org.odfi.specification.docbook.server.docbook;

import java.io.File;

import org.odfi.specification.docbook.server.ooxoo.elements.Docbook;
import org.odfi.specification.docbook.server.utils.DocumentManipulator;

import com.idyria.tools.xml.utils.XMLUtils;

/**
 * Extended Docbook class to manipulate docbook
 * FIXME: only article is supported at the moment
 * @author rleys
 *
 */
public class DocbookExtended extends Docbook {


	private DocumentManipulator docbookDocument = null;
	
	public DocbookExtended() {
		super();
		this.docbookDocument = new DocumentManipulator();
	}
	
	/**
	 * @param str
	 */
	public DocbookExtended(String str) {
		super(str);
		initDocbook(str);

	}
	
	
	
	@Override
	public void _setValueFromString(String value) {
		// TODO Auto-generated method stub
		super._setValueFromString(value);
		initDocbook(value);
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		super.setValue(value);
		initDocbook(value);
	}
	
	private void initDocbook(String path) {
		// Try to prepare docbook document
		//----------------------------------
		try {
			this.docbookDocument = new DocumentManipulator(XMLUtils.buildDocument(new File(path)));
		
		
			// Register Namespace
			//--------------------------
			this.docbookDocument.addNamespaceBinding("docbook", "http://docbook.org/ns/docbook");
			
			// FIXME Detect base element
			//---------------------------
		
		} catch(Exception e) {
			// Exception : fail silently
			e.printStackTrace();
			this.docbookDocument = new DocumentManipulator();
		}
	}
	
	
	/**
	 * Returns parent folder path as file
	 */
	public File getFolder() {
		return new File(this.getValue()).getParentFile();
	}
	
	/**
	 * Returns the file as path
	 * @return
	 */
	public File getFile() {
		return new File(this.getValue());
	}

	public String getTitle() {
		return this.docbookDocument.getTextContent("/docbook:article/docbook:info/docbook:title");
	}
	
	public String getInfoAbstract() {
		return this.docbookDocument.getTextContent("/docbook:article/docbook:info/docbook:abstract");
	}
	
	

}
