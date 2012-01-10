/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.builder.properties;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

import uni.hd.cag.eclipse.tools.docbook.builder.DocBookInfoXMLHandler;

/**
 * 
 * This tester provides properties for IFile XML files in a project.
 * 
 * @author rleys
 *
 */
public class DocbookFilePropertiesTester extends PropertyTester {

	/**
	 * 
	 */
	public DocbookFilePropertiesTester() {
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		//-- NO receiver...fail
		if (receiver==null)
			return false;
		
		System.out.println("Testing property "+property);
		
		//-- First only test XML files
		IFile xmlFile = (IFile) receiver;
		if (xmlFile.getFileExtension()==null || !xmlFile.getFileExtension().equals("xml")) {
			return false;
		}
		
		
		if (property.equals("docbookArticle")) {
			
			
			
			// Is this file a docbook article ?
			//-------------------------------------
			
			//-- Try to parse
			DocBookInfoXMLHandler xmlHandler = new DocBookInfoXMLHandler(xmlFile);
			try {
				
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.newSAXParser().parse(xmlFile.getContents(),xmlHandler);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		
			}
			
			//-- Return property result
			return xmlHandler.isDocbookArticle();
			
			
		}
		
		
		return false;
	}

}
