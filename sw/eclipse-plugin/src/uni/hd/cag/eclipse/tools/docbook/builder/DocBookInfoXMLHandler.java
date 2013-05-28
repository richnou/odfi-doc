/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is a SAX XML handler to test an XML document and gather some base
 * properties on it (like if it is a docbook article or whatever)
 * 
 * @author rleys
 * 
 */
public class DocBookInfoXMLHandler extends DefaultHandler {

	private IFile file;

	public static final String DOCBOOK_NS = "http://docbook.org/ns/docbook";

	public static final String MARKER_TYPE = "cag-tools.docbook.xmlProblem";

	/**
	 * To only process the first encountered element
	 */
	private boolean documentElementSeen = false;

	/**
	 * Do we have a docbook article ?
	 */
	private boolean docbookArticle = false;

	public DocBookInfoXMLHandler(IFile file) {
		this.file = file;
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		// -- Check on first element to ensure we have a docbook
		if (!this.documentElementSeen) {

			this.docbookArticle = uri != null
					&& uri.equals(DocBookInfoXMLHandler.DOCBOOK_NS)
					&& localName.equals("article");

			// -- Only look at document element
			this.documentElementSeen = true;

		}
	}

	public boolean isDocbookArticle() {
		return docbookArticle;
	}

	private void addMarker(SAXParseException e, int severity) {
		this.addMarker(file, e.getMessage(), e.getLineNumber(), severity);
	}

	public void error(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_ERROR);
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_ERROR);
	}

	public void warning(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_WARNING);
	}

	private void addMarker(IFile file, String message, int lineNumber,
			int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
		}
	}

}
