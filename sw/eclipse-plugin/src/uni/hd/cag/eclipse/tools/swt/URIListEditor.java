/**
 * 
 */
package uni.hd.cag.eclipse.tools.swt;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.widgets.Composite;

/**
 * @author rtek
 *
 */
public class URIListEditor extends StringListEditor {

	/**
	 * 
	 */
	public URIListEditor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param labelText
	 * @param parent
	 */
	public URIListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getNewInputObject() {
		// TODO Auto-generated method stub
		String result =  super.getNewInputObject();
		
		
		// Perform some escaping
		//-----------------------------------
		result = result.replace("\\", "/");
		
		//-- Check The URI
		//----------------------------
		//File f = new File("E:\\Common\\CAG\\projects\\phd\\doc\\docbook");
		//System.err.println("**** Fiel to url: "+f.toURI());
		
		
		//-- If no scheme is provided, assume it is file://
		try {
			URI uri = new URI(result);
			if (uri.getScheme()==null) {
				result = "file://"+result;
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	

}
