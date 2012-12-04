/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook;

import java.util.logging.Logger;

import org.eclipse.core.internal.utils.ArrayIterator;
import org.eclipse.ui.IStartup;
import org.odfi.collaboration.docbook.core.StylesheetRepository;
import org.odfi.collaboration.docbook.core.StylesheetsLoader;

import uni.hd.cag.eclipse.tools.docbook.builder.console.ConsoleFactory;

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
		
		//-- Configure logging to have the Stylesheet Loader log to console
		Logger.getLogger(StylesheetsLoader.class.getCanonicalName()).addHandler(ConsoleFactory.getLoggingHandler());
		
		
		// Opening Stylesheet loader
		//---------------------------------
		DocbookPlugin.getDefault().getStylesheetsLoader().reloadFromPreferences();
		
		
	

	}

}
