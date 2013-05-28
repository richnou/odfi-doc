/**
 * 
 */
package org.odfi.specification.docbook.server;

import uni.hd.cag.wsframework.octopus.core.OctopusCore;
import uni.hd.cag.wsframework.octopus.core.service.OctopusServiceLifecycleException.LifecycleAlreadyExecutedException;
import uni.hd.cag.wsframework.octopus.core.service.OctopusServiceLifecycleException.LifecycleExecutionException;
import uni.hd.cag.wsframework.octopus.service.swingmonitor.SwingMonitorService;

/**
 * @author rleys
 *
 */
public class DocbookServerMain {

	/**
	 * 
	 */
	public DocbookServerMain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//-- Create octopus
		OctopusCore octopus = OctopusCore.createSingleInstance();
		
		//-- Add Services
		octopus.getServicesManager().registerService(new DocbookServerService());
		octopus.getServicesManager().registerService(new SwingMonitorService());
		
		try {
			octopus.init();
			octopus.start();
		} catch (LifecycleAlreadyExecutedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LifecycleExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

}
