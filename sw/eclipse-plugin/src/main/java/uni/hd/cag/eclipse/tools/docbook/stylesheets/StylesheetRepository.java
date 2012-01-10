/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.stylesheets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import uni.hd.cag.eclipse.tools.docbook.builder.console.ConsoleFactory;
import uni.hd.cag.eclipse.tools.docbook.stylesheets.ooxoo.elements.Stylesheet;
import uni.hd.cag.eclipse.tools.docbook.stylesheets.ooxoo.elements.Stylesheets;
import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;

import com.idyria.utils.java.io.TeaIOUtils;

/**
 * @author rtek
 *
 */
public class StylesheetRepository extends Stylesheets {

	/**
	 * The URL where this repository descriptor is located
	 */
	protected transient URI repositoryURI = null;
	
	/**
	 * 
	 */
	public StylesheetRepository() {
		// TODO Auto-generated constructor stub
	}
	
	

	public StylesheetRepository(URI repositoryURI) {
		super();
		this.setRepositoryURL(repositoryURI);
	}


	/**
	 * Do transformation
	 * @param sourceFile IFile to tranform
	 * @param stylesheet Stylesheet to apply
	 */
	public void transform(IFile sourceFile,Stylesheet stylesheet) {
		
		
		try {
			// Prepare sources && Result
			//---------------------------
			InputStream stylesheetStream = null;
			InputStream sourceStream = sourceFile.getContents();
			String sourceNameWithoutExtension = sourceFile.getName().replace(sourceFile.getFileExtension(), "");
			
			//-- Stylesheet
			URI stylesheetPath = new URI(stylesheet.getPath().getValue());
			if (!stylesheetPath.isAbsolute()) {
				stylesheetPath = new URI(this.getRepositoryURL().toString()+"/../"+stylesheet.getPath().getValue());
				stylesheetPath = stylesheetPath.normalize();
			}
			
			// File or URL?
			String systemId = stylesheetPath.toURL().toExternalForm();
			if (stylesheetPath.getScheme()==null || stylesheetPath.getScheme().equals("file")) {
				stylesheetStream = new FileInputStream(stylesheetPath.getPath());
				systemId = new File(stylesheetPath.getPath()).toURI().toURL().toExternalForm();
			} else 
				stylesheetStream = stylesheetPath.toURL().openStream();
		
			ConsoleFactory.logInfo("Resolved stylesheet path: "+stylesheetPath+" scheme : "+stylesheetPath.getScheme()+"// systemid: "+systemId);
			
			
			//-- Create Template
			
			// Factory
			Configuration saxonConfiguration = Configuration.newConfiguration();
			saxonConfiguration.setXIncludeAware(true);
	
			TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", Thread.currentThread().getContextClassLoader());			
			((net.sf.saxon.TransformerFactoryImpl)factory).setConfiguration(saxonConfiguration);

			// Create
			StreamSource stylesheetSource = new StreamSource(stylesheetStream);
			stylesheetSource.setSystemId(systemId);
			Templates stylesheetTemplates = factory.newTemplates(stylesheetSource);
			
			// Prepare output
			//------------------
			
			//-- Output base is the folder in which the source file is located		
			IContainer outputBaseContainer = sourceFile.getParent();

			
			//-- Target Output is the outputbase, adjusted to Output element in Stylesheet
			IContainer outputTargetContainer = outputBaseContainer;
			if (stylesheet.getOutput()!=null && stylesheet.getOutput().getPath()!=null) {
				IFolder outputTargetFolder = outputBaseContainer.getFolder(new Path(stylesheet.getOutput().getPath().getValue()));
				outputTargetContainer = outputTargetFolder;
				if (!outputTargetFolder.exists()) {
					outputTargetFolder.create(true, true, null);
					outputTargetContainer.setDerived(true, null);
				}
			}
			
			//-- Output result depends on output type
			IFile outputTarget = outputTargetContainer.getFile(new Path(sourceNameWithoutExtension+stylesheetTemplates.getOutputProperties().getProperty("method", "xml")));
			
			// Do output
			//---------------------
		
			//-- Prepare XML source
			StreamSource sourceXMLSource = new StreamSource(sourceStream);
			sourceXMLSource.setSystemId(sourceFile.getLocationURI().toURL().toExternalForm());
			
			//-- Transform
			ByteArrayOutputStream resultingBytes = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(resultingBytes);
			stylesheetTemplates.newTransformer().transform(sourceXMLSource, result);
		
			//-- Copy to output
			if (!outputTarget.exists()) {
				outputTarget.create(new ByteArrayInputStream(resultingBytes.toByteArray()), true,null);
			} else
				outputTarget.setContents(new ByteArrayInputStream(resultingBytes.toByteArray()), true,false, null);
			outputTarget.setDerived(true, null);
			
			//---- Copy additional resources
			//-------------------------
			if (stylesheet.getOutput()!=null) {
				for (XSDStringBuffer fileToCopy : stylesheet.getOutput().getFile()) {
					
					
					try {
						//-- Resource is absolute or relative to 
						URI resourceURI = new URI(fileToCopy.getValue());
						if (!resourceURI.isAbsolute()) {
							resourceURI = new URI(stylesheetPath.toString()+"/../"+resourceURI.getPath());
							resourceURI=resourceURI.normalize();
						}
						
						//-- IF it is a file
						if (resourceURI.getScheme()==null || resourceURI.getScheme().equals("file")) {
							
							//-- Copy Folder content if it is a folder
							File resourceFile = new File(resourceURI.getPath());
							if (resourceFile.isDirectory()) {
								
								//-- Create Target container
								IFolder copiedFolder = outputTargetContainer.getFolder(new Path(resourceFile.getName()));
								if (!copiedFolder.exists()) {
									copiedFolder.create(true, true, null);
								}
								
								//-- Create all files in source Folder
								for (File f : resourceFile.listFiles()) {
									
									if (f.isDirectory())
										continue;
									
									//-- Prepare file
									IFile targetFile = copiedFolder.getFile(f.getName());
									if (targetFile.exists()) {
										targetFile.delete(true, null);
									}
									
									//-- Create
									targetFile.create(new FileInputStream(f), true, null);
									
								}
								
							}
							//-- Copy File if it is a file
							else if(resourceFile.isFile() && resourceFile.exists()) {
								
								//-- Create 
								IFile outputFile = outputTargetContainer.getFile(new Path(resourceFile.getName()));
								if (outputFile.exists()) {
									outputFile.delete(true, null);
								}
								
								//-- Copy data
								outputFile.create(new FileInputStream(resourceFile), true, null);
								outputFile.setDerived(true,null);
							}
						}
							
						
					
						
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			} // EOF Copy additional output
			
			// Post processing
			//-----------------------
			if (stylesheet.getPostProcessing()!=null) {
				
				//---- Execute All commands in target output directory
				//-------------------------------
				for (XSDStringBuffer cmd : stylesheet.getPostProcessing().getCommand()) {
					
					//-- Replace variables
					String command = cmd.getValue();
					command = command.replace("$repositoryPath$", new File(this.getRepositoryURL().toURL().getFile()).getParentFile().getAbsolutePath());
					command = command.replace("$stylesheetFolderPath$", new File(stylesheetPath.toURL().getFile()).getParentFile().getAbsolutePath());
					command = command.replace("$outputFile$",outputTarget.getLocation().toFile().getAbsolutePath());
					command = command.replace("$outputFileFolder$",outputTarget.getLocation().toFile().getParentFile().getAbsolutePath());
					command = command.replace("$outputFileName$",outputTarget.getLocation().toFile().getName().replaceAll("\\..*$", ""));
					
					//-- Separate command string on spaces to create an array
					String[] cmdArray = command.split(" ");
					
					for (String str : cmdArray)
						ConsoleFactory.logInfo("-- Post Processing command arg: "+str);
					
					//-- Prepare process builder
					ProcessBuilder pb = new ProcessBuilder(cmdArray);
					pb.directory(outputTargetContainer.getLocation().toFile());
					pb.redirectErrorStream(true);

					//-- Start and redirect output to 
					Process p = pb.start();
					byte[] commandResult = TeaIOUtils.swallowStream(p.getInputStream());
					
					//-- Output to console
					ConsoleFactory.logInfo("-- Post Processing command: "+command);
					ConsoleFactory.logInfo(new String(commandResult));
					
				}
				
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ConsoleFactory.logThrowable(e);
			//throw new RuntimeException(e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ConsoleFactory.logThrowable(e);
			//throw new RuntimeException(e);
		}
	}
	

	public URI getRepositoryURL() {
		return repositoryURI;
	}

	public void setRepositoryURL(URI repositoryURL) {
		this.repositoryURI = repositoryURL;
		
		
		
		
	}

	
	
	
}
