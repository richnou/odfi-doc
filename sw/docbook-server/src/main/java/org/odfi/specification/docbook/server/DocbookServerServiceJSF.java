/**
 * 
 */
package org.odfi.specification.docbook.server;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.odfi.specification.docbook.server.docbook.DocbookExtended;

import uni.hd.cag.wsframework.octopus.Octopus;
import uni.hd.cag.wsframework.octopus.core.OctopusCore;

/**
 * @author rleys
 *
 */
@ManagedBean(name="docbookService")
@RequestScoped
public class DocbookServerServiceJSF extends JSFWrapper<DocbookServerService> {

	
	public DocbookServerServiceJSF() {
		super((DocbookServerService) OctopusCore.getInstance().getServicesManager().getService("docbook"));
	}
	
	/**
	 * 
	 * @param path
	 */
	public void addBook(String path) {
		
		try {
			
			super.wrapped.addBook(path);
			
			addSuccessMessage("Successfully added book path");
			
			//return "";
			
		} catch (Exception e) {
			this.handleExceptionToMessage(e);
			//return "";
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public List<DocbookExtended> getAllBooks() {
		return super.wrapped.getAllBooks();
	}
	
	
}
