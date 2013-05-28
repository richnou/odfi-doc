/**
 * 
 */
package org.odfi.specification.docbook.server.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.idyria.tools.xml.utils.XMLUtils;
import com.idyria.tools.xml.utils.XPathNSContext;

/**
 * A utility class to access and manipulate DOM Documents, using Xpath and so on
 * @author rleys
 *
 */
public class DocumentManipulator {

	/**
	 * Target Document
	 */
	protected Document document = null;
	
	/**
	 * The XPath context
	 */
	protected XPath xpath;
	
	/**
	 * Creates a default document
	 */
	public DocumentManipulator() {
		try {
			this.document = XMLUtils.buildDocument();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//-- Init Xpath
		init();
	}
	
	/**
	 * Creates from a base document
	 * @param document
	 */
	public DocumentManipulator(Document document) {
		this.document = document;
		init();
	}
	
	/**
	 * Creates the XPath context and so on
	 */
	private void init() {
		this.xpath = XPathFactory.newInstance().newXPath();
		this.xpath.setNamespaceContext(new XPathNSContext());
	}
	
	
	public void addNamespaceBinding(String prefix,String uri) {
		((XPathNSContext)this.xpath.getNamespaceContext()).register(prefix, uri);
	}
	
	/**
	 * Get the text content for a given expression, from document root
	 * @param xpath
	 * @return The value or "" if not found
	 */
	public String getTextContent(String xpath) {
		
		String result = "";
		
		//-- Process
		try {
			result = (String) this.xpath.evaluate(xpath, this.document, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result==null?"":result;
		
	}
	
	

}
