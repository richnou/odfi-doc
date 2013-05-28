/**
 * 
 */
package org.odfi.specification.docbook.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.odfi.specification.docbook.server.docbook.DocbookExtended;
import org.odfi.specification.docbook.server.ooxoo.elements.Docbook;
import org.odfi.specification.docbook.server.ooxoo.elements.DocbookServer;

import uni.hd.cag.aib.core.bus.main.AIBMainBus;
import uni.hd.cag.ooxoo.core.WrappingContext;
import uni.hd.cag.ooxoo.utils.aib.OOXOOEventsHandler;
import uni.hd.cag.ooxoo.utils.aib.OOXOOGetDOMEvent;
import uni.hd.cag.ooxoo.utils.aib.UserHomeGetOOXOOFile;
import uni.hd.cag.ooxoo.utils.aib.UserHomeWriteOOXOOFile;
import uni.hd.cag.ooxoo.utils.octopus.OOXOOService;
import uni.hd.cag.wsframework.octopus.core.OctopusCore;
import uni.hd.cag.wsframework.octopus.core.service.OctopusService;
import uni.hd.cag.wsframework.octopus.core.service.OctopusServiceLifecycleException.LifecycleExecutionException;
import uni.hd.cag.wsframework.octopus.service.jetty.JettyService;
import uni.hd.cag.wsframework.octopus.service.jetty.WebAppApplication;
import uni.hd.cag.wsframework.octopus.service.jetty.el.ELRegistered;
import uni.hd.cag.wsframework.octopus.service.jetty.events.LoadDevWebbApp;

/**
 * Requires and loaded:
 * 
 *  OOXOO Service
 *  Jetty Service
 * 
 * @author rleys
 *
 */
@ELRegistered("docbook")
public class DocbookServerService extends OctopusService {

	protected OOXOOEventsHandler ooxooUtils = new OOXOOEventsHandler();
	
	protected DocbookServer configuration = new DocbookServer();
	
	protected WrappingContext ooxooContext = null;
	
	/**
	 * @param name
	 */
	public DocbookServerService() {
		super("docbook");
		
		//-- Load other services
		OctopusCore.getInstance().getServicesManager().registerService(new JettyService());
		OctopusCore.getInstance().getServicesManager().registerService(new OOXOOService());
		
		// Wrapping Context
		//-----------------------	
		this.ooxooContext = new WrappingContext();
		this.ooxooContext.getReplacementBuffersClassMap().put(Docbook.class,DocbookExtended.class);
	}

	/* (non-Javadoc)
	 * @see uni.hd.cag.wsframework.octopus.core.service.OctopusService#initImpl()
	 */
	@Override
	protected void initImpl() throws LifecycleExecutionException {
		
		
		
		// Load configuration
		//---------------------
		UserHomeGetOOXOOFile<DocbookServer> getConfigEvent = new UserHomeGetOOXOOFile<DocbookServer>(this.configuration,new File(".odfi/specification-docbook-server/config.xml"));
		getConfigEvent.setOoxooContext(this.ooxooContext);
		AIBMainBus.writeEventAndWait(getConfigEvent);
		getConfigEvent.waitFinished();
		this.configuration = getConfigEvent.getCarried();
		
		//-- Verify there is at least one group
		if (this.configuration.getDocbookGroup()==null) {
			this.configuration.getDocbookGroup(true).getName(true).setValue("base");
		}
		
		
		// Load webapp
		//---------------------
		
		//-- Prepare webapp
		WebAppApplication<File> webapp = new WebAppApplication<File>();
		webapp.setApplicationSource(new File("src/main/webapp"));
		webapp.setName("docbook");
		WebAppContext ctx = new WebAppContext(webapp.getApplicationSource()
				.getAbsolutePath(), "/docbook");
		webapp.setContext(ctx);
		

		//-- Try to define handlers
		
		//-- Go through configuration and just add
		
		for (Docbook docbook : this.configuration.getDocbookGroup().getDocbook()) {
			
			ContextHandler dbctx = new ContextHandler();
			dbctx.setContextPath("/docbook/view/html/"+((DocbookExtended) docbook).getFile().getName().replaceAll("(.*)\\.[a-z]+", "$1"));
			ctx.setHandler(dbctx);
			
			ResourceHandler rh = new ResourceHandler();
			rh.setDirectoriesListed(true);
			rh.setResourceBase(((DocbookExtended) docbook).getFolder().getAbsolutePath()+"/html/");
			rh.setWelcomeFiles(new String[]{((DocbookExtended) docbook).getFile().getName().replaceAll("(.*)\\.[a-z]+", "$1")+".html"});
			dbctx.setHandler(rh);
			
		}
		
		
		
		LoadDevWebbApp loadWebAppEvent = new LoadDevWebbApp();
		loadWebAppEvent.setApplication(webapp);
		AIBMainBus.writeEventAndWait(loadWebAppEvent);
		
		
	}

	/* (non-Javadoc)
	 * @see uni.hd.cag.wsframework.octopus.core.service.OctopusService#reloadImpl()
	 */
	@Override
	protected void reloadImpl() throws LifecycleExecutionException {
		
		

	}

	/* (non-Javadoc)
	 * @see uni.hd.cag.wsframework.octopus.core.service.OctopusService#startImpl()
	 */
	@Override
	protected void startImpl() throws LifecycleExecutionException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see uni.hd.cag.wsframework.octopus.core.service.OctopusService#stopImpl()
	 */
	@Override
	protected void stopImpl() throws LifecycleExecutionException {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Rewrittes the config out through OOXOO
	 */
	protected synchronized void syncConfig() {

		// -- Write
		UserHomeWriteOOXOOFile<DocbookServer> writeEvent = new UserHomeWriteOOXOOFile<DocbookServer>(
				this.configuration, new File(".odfi/specification-docbook-server/config.xml"));
		AIBMainBus.writeEvent(writeEvent);
		writeEvent.waitFinished();
	}
	
	
	/**
	 * Add the path to a new docbook
	 * @param path
	 * @throws FileNotFoundException 
	 */
	public void addBook(String path) throws FileNotFoundException {
		
		//-- Check provided path
		File pathFile = new File(path);
		if (!pathFile.exists()  ) {
			throw new FileNotFoundException("Path does not point to a valid XML file: "+path);
		}
		
		//-- FIXME Check docbook format
		OOXOOGetDOMEvent ev = new OOXOOGetDOMEvent(new File(path));
		
		//-- Create a Docbook Extended
		DocbookExtended dbext = new DocbookExtended(path);

		//-- Add
		this.configuration.getDocbookGroup().getDocbook().add(dbext);
		
		//-- Sync config
		this.syncConfig();
		
	}

	
	/**
	 * FIXME Groups should be supported
	 * Returns the list of all registered books
	 * @return
	 */
	public List<DocbookExtended> getAllBooks() {
		
		LinkedList<DocbookExtended> results = new LinkedList<DocbookExtended>();
		
		
		//-- Go through configuration and just add
		for (Docbook docbook : this.configuration.getDocbookGroup().getDocbook()) {
			results.add((DocbookExtended) docbook);
		}
		

		return results;
		
		
	}
	

}
