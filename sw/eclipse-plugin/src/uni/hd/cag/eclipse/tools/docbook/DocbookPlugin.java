package uni.hd.cag.eclipse.tools.docbook;

import java.util.logging.Logger;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.odfi.collaboration.docbook.core.StylesheetRepository;
import org.odfi.collaboration.docbook.core.StylesheetsLoader;
import org.osgi.framework.BundleContext;

import uni.hd.cag.eclipse.tools.docbook.builder.console.ConsoleFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class DocbookPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "odfi-eclipse-diagrams-plugin"; //$NON-NLS-1$

	public static final String ENV_EXTERNAL_REPS = "DOCBOOK_STYLESHEETS_REPS";
	
	// The shared instance
	private static DocbookPlugin plugin;
	
	/**
	 * Extended docbook core StylesheetLoader for Plugin context specific things
	 */
	private PluginStylesheetsLoader stylesheetsLoader = new PluginStylesheetsLoader();
	
	/**
	 * The constructor
	 */
	public DocbookPlugin() {
	}

	
	
	
	/**
	 * @return the stylesheetsLoader
	 */
	public PluginStylesheetsLoader getStylesheetsLoader() {
		return stylesheetsLoader;
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		//-- Configure logging to have the Stylesheet Repository log to console
		Logger.getLogger(StylesheetRepository.class.getCanonicalName()).addHandler(ConsoleFactory.getLoggingHandler());
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static DocbookPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
