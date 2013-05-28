/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook;

import org.eclipse.core.internal.utils.ArrayIterator;
import org.odfi.collaboration.docbook.core.StylesheetsLoader;

/**
 * @author rleys
 *
 */
public class PluginStylesheetsLoader extends StylesheetsLoader {

	/**
	 * 
	 */
	public PluginStylesheetsLoader() {
		// TODO Auto-generated constructor stub
	}

	 
	/**
	 * This method reanalyses the preferences to reload all stylesheets
	 */
	public void reloadFromPreferences() {
		
		//-- Clear loaded stylesheets
		this.repositories.clear();
		
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
				this.loadRepository(rep);
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
				this.loadRepository(rep);
			} catch (Exception e) {
				// Remove wrong URL
				//it.remove();
				e.printStackTrace();
			} 
		}
		
	}
	
}
