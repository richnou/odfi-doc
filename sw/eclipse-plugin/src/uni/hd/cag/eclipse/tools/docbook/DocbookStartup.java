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
		StylesheetsLoader loader = StylesheetsLoader.getInstance();
		
		
	
		// Load Preferences with Environment variable if there is one, otherwise don't touch it
		//---------------------
		String envExternalReps = System.getenv(DocbookPlugin.ENV_EXTERNAL_REPS);
		if (envExternalReps!=null) {
			DocbookPlugin.getDefault().getPreferenceStore().setDefault("reps.external.env", envExternalReps);
		}
		
		// Do we have any manually added stylesheets repositories?
		//-----------------------
		String externalAddedRepsString = DocbookPlugin.getDefault().getPreferenceStore().getString("reps.external.added");
		String externalEnvRepsString = DocbookPlugin.getDefault().getPreferenceStore().getString("reps.external.env");

		
		// Parse the repositories
		//----------------------------------
		String[] externalEnvReps = externalEnvRepsString.split(";");
		ArrayIterator<String> it = new ArrayIterator<String>(externalEnvReps);
		while (it.hasNext()) {
			String rep = it.next();
			try {
				loader.loadRepository(rep);
			} catch (Exception e) {
				// Remove wrong URL
				//it.remove();
				e.printStackTrace();
			}
		}
		
		String[] externalAddedReps = externalAddedRepsString.split(";");
		it = new ArrayIterator<String>(externalAddedReps);
		while (it.hasNext()) {
			String rep = it.next();
			try {
				loader.loadRepository(rep);
			} catch (Exception e) {
				// Remove wrong URL
				//it.remove();
				e.printStackTrace();
			} 
		}
		

	}

}
