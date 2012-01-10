/**
 * 
 */
package org.odfi.specification.docbook.server;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


import com.idyria.utils.java.logging.TeaLogging;

/**
 * 
 * @author rleys
 *
 */
@FacesValidator("docbook.validator.bookFileValidator")
public class DocbookFileValidator implements Validator {

	/**
	 * 
	 */
	public DocbookFileValidator() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		


	}

}
