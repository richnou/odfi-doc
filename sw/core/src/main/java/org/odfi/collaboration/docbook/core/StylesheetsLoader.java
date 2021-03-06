/**
 * 
 */
package org.odfi.collaboration.docbook.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.tree.iter.ArrayIterator;

import org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet;
import org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheets;
import org.xml.sax.SAXException;

import uni.hd.cag.ooxoo.core.UnwrapException;
import uni.hd.cag.ooxoo.core.WrappingContext;
import uni.hd.cag.ooxoo.core.io.dom.DOMXMLIO;

import com.idyria.tools.xml.utils.XMLUtils;
import com.idyria.utils.java.collections.Pair;

/**
 * @author rleys
 *
 */
public class StylesheetsLoader {

	/**
	 * Static reference for an eventual Singleton
	 */
	private static StylesheetsLoader ref = null;
	
	/**
	 * Map repository id to the matching object
	 */
	protected HashMap<String,StylesheetRepository> repositories = new HashMap<String,StylesheetRepository>();
	
	/**
	 * 
	 */
	public StylesheetsLoader() {
		
		//-- Load the stylesheets
		
	}
	
	/**
	 * Loads the stylesheets from the different sources
	 */
	public void load() {
		
		//-- Clean
		this.repositories.clear();
		
		loadInternal();
		loadExternalRepositories();
		
	}
	
	private void loadInternal() {
		
	}
	
	public void loadExternalRepositories() {
		
		// Load Preferences with Environment variable if there is one, otherwise don't touch it
		//---------------------
		String envExternalRepsString = System.getenv("ODFI_DOCBOOK_STYLESHEETS_REPS");
		if (envExternalRepsString!=null) {
			String[] externalEnvReps = envExternalRepsString.split(""+File.pathSeparatorChar);
			for (String rep : externalEnvReps) {
				try {
					this.loadRepository(rep);
				} catch (Exception e) {
					// Remove wrong URL
					//it.remove();
					e.printStackTrace();
				}
			}
		}
		
		/*
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
		
		// Resave in case we cleaned them
		//-------------------------------------
		*/
		
		
	}
	
	private void loadExternalSingle() {
		
	}
	
	/**
	 * Load a single repository of stylesheets
	 * @param url The URL of the xml stylesheets file describing the repository
	 * @throws MalformedURLException 
	 * @throws URISyntaxException 
	 */
	public void loadRepository(String url) throws MalformedURLException, URISyntaxException {
		
		//-- Elimiate empty strings
		if (url.length()==0)
			return;
		
		//-- Try to open the URL
		try {
			
			//-- all \ from windows like path must be converted to /
			url = url.replace("\\", "/");
			
			//-- Get input stream: All URLss have to point to an xml file
			URI repositoryURI = new URI(url);
			
			//-- If not an XML file, add a default name
			if (!repositoryURI.toString().endsWith(".xml") 
					&& (repositoryURI.getScheme()==null || repositoryURI.getScheme().length()==1)) {
				
				// (add file scheme also)
				repositoryURI = new URI(repositoryURI.toString()+"/"+"stylesheets.xml");
				
			} else if (!repositoryURI.toString().endsWith(".xml")) {
				
				repositoryURI = new URI(repositoryURI.toString()+"/stylesheets.xml");
				
			}
			
			//-- Add file scheme if a normal file path is provided
			// (scheme length will be 1 under windows because Drive letter is taken as such)
			if (repositoryURI.getScheme()==null || repositoryURI.getScheme().length()==1) {
				repositoryURI = new URI("file://"+repositoryURI.toString());
			}
			
			//-- Open
			InputStream repositoryDescriptorStream = null;
			try {
				repositoryDescriptorStream = repositoryURI.toURL().openStream();
			} catch (IOException ioex) {
				throw new MalformedURLException("Provided Stylesheet URI: "+repositoryURI.toString()+"(original: "+url+") could not be opened: "+ioex.getMessage());
			}
			//System.err.println();
			
			//-- Try to unoox
			/*if (repositoryURI.getScheme().equals("file") && new File(repositoryURI.toURL().getFile()).isDirectory()) {
				repositoryURI = TeaFileUtils.buildPathAsFile(new File(repositoryURI.toURL().getFile()),"stylesheets.xml").toURI();
			}
			InputStream repositoryDescriptorStream = (repositoryURI.getScheme()==null || repositoryURI.getScheme().equals("file")) ? new FileInputStream(new File(repositoryURI.getPath())):repositoryURI.toURL().openStream();
			*/
			
			//-- Unwrap and read
			WrappingContext ctx = new WrappingContext();
			ctx.getReplacementBuffersClassMap().put(Stylesheets.class, StylesheetRepository.class);
			
			StylesheetRepository stylesheets = new StylesheetRepository(repositoryURI);
			stylesheets.setNextBuffer(new DOMXMLIO(XMLUtils.buildDocument(repositoryDescriptorStream,null)));
			stylesheets.unwrap(ctx);
			
			//-- Add to map
			this.repositories.put(stylesheets.getId().getValue(),stylesheets);
			
			
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnwrapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	
	/**
	 * 
	 * @param qualifiedName  : repositoryId:stylesheetname
	 * @return null if not found or qualified name is not correct
	 */
	public Pair<StylesheetRepository, Stylesheet> getStylesheet(String qualifiedName) {
		String[] components = qualifiedName.split(":");
		if (components.length!=2)
			return null;
		return this.getStylesheet(components[0], components[1]);
	}
	
	
	/**
	 * 
	 * @param repositoryId
	 * @param stylesheetName
	 * @return null if not found
	 */
	public Pair<StylesheetRepository, Stylesheet> getStylesheet(String repositoryId,String stylesheetName) {
		
		// Get Repository
		//--------------------
		StylesheetRepository repository = this.repositories.get(repositoryId);
		if (repository==null)
			return null;
		
		// Get Stylesheet
		//-------------------
		Stylesheet stylesheet = null;
		for (Stylesheet candidate : repository.getStylesheet()) {
			if (candidate.getName().getValue().equals(stylesheetName)) {
				stylesheet = candidate;
				break;
			}
		}
		if (stylesheet==null)
			return null;
		
		// Found :)
		return new Pair<StylesheetRepository, Stylesheet>(repository, stylesheet);
		
	}
	
	
	public Collection<StylesheetRepository> getRepositories() {
		return repositories.values();
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public String[] getPossibleStylesheetsArray() {
		
		//-- Prepare possible values
		Vector<String> values = new Vector<String>();
		for (StylesheetRepository rep : this.repositories.values()) {
			
			for (Stylesheet stylesheet : rep.getStylesheet()) {
				values.add(rep.getId()+":"+stylesheet.getName().getValue());
			}
			
		}
		
		return values.toArray(new String[values.size()]);
		
	}

	/**
	 * Getter for the singleton access
	 * User can still instanciate this class as he whishes
	 * @return
	 */
	public static synchronized StylesheetsLoader getInstance() {
		
		if (ref==null)
			ref = new StylesheetsLoader();
		return ref;
		
	}


	
	
}
