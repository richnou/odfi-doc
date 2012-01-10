/**
 * 
 */
package org.odfi.specification.docbook.server;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * 
 * Wrapps a class to ease adding JSF method access to a normal class
 * @author rleys
 *
 */
public class JSFWrapper<T> {

	/**
	 * An eventually wrapped object
	 */
	protected T wrapped = null;
	
	/**
	 * 
	 */
	public JSFWrapper(T wrapped) {
		this.wrapped = wrapped;
	}

	public JSFWrapper() {
		
	}
	
	/**
	 * Inserts a message in the faces context from the provided exception
	 */
	protected void handleExceptionToMessage(Exception e) {
		
		FacesContext.getCurrentInstance().addMessage("", 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),e.getMessage()));
		
	}
	
	protected void addSuccessMessage(String message) {
		FacesContext.getCurrentInstance().addMessage("", 
				new FacesMessage(FacesMessage.SEVERITY_INFO,message,message));
	}
	
}
