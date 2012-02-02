package uni.hd.cag.eclipse.tools.docbook.builder;

import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.odfi.collaboration.docbook.core.StylesheetRepository;
import org.odfi.collaboration.docbook.core.StylesheetsLoader;
import org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet;
import org.xml.sax.SAXException;

import uni.hd.cag.eclipse.tools.docbook.DocbookPlugin;
import uni.hd.cag.eclipse.tools.docbook.builder.console.ConsoleFactory;

import com.idyria.utils.java.collections.Pair;
import com.idyria.utils.java.logging.TeaLogging;

public class DocbookBuilder extends IncrementalProjectBuilder {

	
	

	public static final String BUILDER_ID = "uni.hd.cag.tools.docbook.builder";

	

	private SAXParserFactory parserFactory;

	
	
	
	public DocbookBuilder() {
		super();
		
		//-- Configure logging to have the Stylesheet Repository log to console
		Logger.getLogger(StylesheetRepository.class.getCanonicalName()).addHandler(ConsoleFactory.getLoggingHandler());
	}
	
	private class DeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				
				//System.out.println("Added resource "+resource);
				
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				
				ConsoleFactory.logInfo("Changed resource "+resource);
				
				//-- Is it an XML File ?
				if (resource instanceof IFile && resource.getName().endsWith(".xml")) {
					IFile changedFile = (IFile) resource;
					
					// Check XML and maybe also subset that are not supported by Stylesheet and so on
					//-------------------------------------------
					
					//-- Delete error markers
					try {
						changedFile.deleteMarkers(DocBookInfoXMLHandler.MARKER_TYPE, false, IResource.DEPTH_ZERO);
					} catch (CoreException ce) {
					}
					
					//-- Check XML and validate
					DocBookInfoXMLHandler reporter = new DocBookInfoXMLHandler(changedFile);
					try {
						SAXParser parser = DocbookBuilder.this.getParser();
						parser.parse(changedFile.getContents(), reporter);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//-- Get Stylesheets
					String stylesheetProperty = changedFile.getPersistentProperty(new QualifiedName(DocbookPlugin.PLUGIN_ID, "stylesheet"));
					if (stylesheetProperty==null) {
						ConsoleFactory.logError("Stylesheet info not found");
						return true;
					} 
					String[] stylesheets = stylesheetProperty.split(" ");
					
					// Is it a docbook article
					//-------------------------------------
					if (reporter.isDocbookArticle()) {
						
						//-- Loop over stylesheets
						for (String stylesheet : stylesheets) {
						
							//-- Show a building message
							//ConsolePlugin.getDefault().getConsoleManager().getConsoles()
							
							ConsoleFactory.logInfo("We have a docbook article and stylesheet: "+stylesheet);
							
							// Find Back stylesheet
							//-----------------------------
							Pair<StylesheetRepository,Stylesheet> styleSheetPair = StylesheetsLoader.getInstance().getStylesheet(stylesheet);
							
							
							// Apply Transformation
							//------------------------------
							if (styleSheetPair==null) {
								ConsoleFactory.logInfo("Stylesheet or Repository not found");
							} else {
								
								ConsoleFactory.logInfo("Building Article");
								styleSheetPair.getLeft().transform(changedFile.getLocation().toFile(), styleSheetPair.getRight());
								changedFile.refreshLocal(0, null);
							}
						}
						
					} else {
						
						//ConsoleFactory.logError("XML Resource is not a docbook article");
						
					}
					
					
				}
				
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	


	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int buildType, Map args, IProgressMonitor monitor)
			throws CoreException {
		
		// Build has been called on out plugin
		//------------------------------------------
		
		//-- The delta represents the changed datas. if null, it is a full build, else it is normal
		IResourceDelta delta = buildType != FULL_BUILD ?getDelta(getProject()):null ;
		
		//-- In case of a full build, we can just walk over the resources
		if (delta == null) {
			
			//-- Create Visitor and call it
			//this.getProject().accept(new ResourceVisitor());
			
		} 
		//-- If it is an incremental build, we get a Delta, and only walk on the changed resources
		else {
			
			delta.accept(new DeltaVisitor());
		}
		return null;
	}

	/**
	 * Create a SAX Parser for docbook evaluation
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private SAXParser getParser() throws ParserConfigurationException,
			SAXException {
		if (parserFactory == null) {
			parserFactory = SAXParserFactory.newInstance();
			parserFactory.setNamespaceAware(true);
		}
		return parserFactory.newSAXParser();
	}

	
}
