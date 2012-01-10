/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook;

import org.eclipse.ui.IStartup;

import uni.hd.cag.eclipse.tools.docbook.stylesheets.StylesheetsLoader;

/**
 * @author rleys
 *
 */
public class DocbookStartup implements IStartup {

	/**
	 * 
	 */
	public DocbookStartup() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup() {
		System.out.println("Starting up CAG's plugin");
		
		
		// Opening Stylesheet loader
		//---------------------------------
		StylesheetsLoader.getInstance().load();
		

	}

}
