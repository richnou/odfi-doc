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

/**
 * @author rleys
 *
 */
public class Stupidtest {

	/**
	 * 
	 */
	public Stupidtest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String uri = "E:\\Common\\CAG\\git\\odfi\\modules\\collaboration\\docbook\\stylesheets\\stylesheets.xml";
		System.out.println("--URI from file: "+new File(uri).getAbsoluteFile().toURI().toString());
		try {
			URI pUri = new URI(uri.replace("\\", "/"));
			
			
			
			System.out.println("Scheme is: "+pUri.getSchemeSpecificPart()
					);
			
			
			if (pUri.getScheme().length()<2) {
				pUri = new URI("file:/"+pUri.toString());
			}
			
			System.out.println("URI transformed to: "+pUri.toString());
			
			InputStream is = pUri.toURL().openStream();
			
			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
