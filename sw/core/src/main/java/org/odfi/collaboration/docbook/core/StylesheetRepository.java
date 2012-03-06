/**
 * 
 */
package org.odfi.collaboration.docbook.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;

import org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheet;
import org.odfi.collaboration.docbook.core.ooxoo.elements.Stylesheets;

import uni.hd.cag.ooxoo.core.buffers.datatypes.XSDStringBuffer;

import com.idyria.utils.java.file.TeaFileUtils;
import com.idyria.utils.java.io.TeaIOUtils;
import com.idyria.utils.java.logging.TeaLogging;
import com.idyria.utils.java.os.OSDetector;
import com.idyria.utils.java.os.OSDetector.OS;


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
	public void transform(File sourceFile,Stylesheet stylesheet) {
		
		
		try {
			
			// Prepare SAXB
			//----------------------

			// Factory
			Configuration saxonConfiguration = Configuration.newConfiguration();
			saxonConfiguration.setXIncludeAware(true);
	
			TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", Thread.currentThread().getContextClassLoader());			
			((net.sf.saxon.TransformerFactoryImpl)factory).setConfiguration(saxonConfiguration);
			
			// Prepare sources && Result
			//---------------------------
			LinkedList<Templates> stylesheetsTemplates = new LinkedList<Templates>();
			URI lastStylsheetPath = null; // Save path of the last stylesheet in the chain
			InputStream sourceStream = sourceFile.toURI().toURL().openStream();
			String sourceNameWithoutExtension = sourceFile.getName().replaceAll("\\.[a-z]+\\z","");
			//TeaLogging.teaLogInfo("Source Name without extension: "+sourceNameWithoutExtension)
			
			//-- Prepare Stylesheets Templates
			for (XSDStringBuffer stylesheetStringPath : stylesheet.getTransformChain().getStylesheet()) {
				
				lastStylsheetPath = new URI(stylesheetStringPath.getValue());
				if (!lastStylsheetPath.isAbsolute()) {
					lastStylsheetPath = new URI(this.getRepositoryURL().toString()+"/../"+stylesheetStringPath.getValue());
					lastStylsheetPath = lastStylsheetPath.normalize();
				}
				
				// File or URL?
				InputStream stylesheetStream = null;
				String systemId = lastStylsheetPath.toURL().toExternalForm();
				if (lastStylsheetPath.getScheme()==null || lastStylsheetPath.getScheme().equals("file")) {
					stylesheetStream = new FileInputStream(lastStylsheetPath.getPath());
					systemId = new File(lastStylsheetPath.getPath()).toURI().toURL().toExternalForm();
				} else 
					stylesheetStream = lastStylsheetPath.toURL().openStream();
		
				// Prepare Templates
				StreamSource stylesheetSource = new StreamSource(stylesheetStream);
				stylesheetSource.setSystemId(systemId);
				Templates stylesheetTemplates = factory.newTemplates(stylesheetSource);
				
				// Add to chain list
				stylesheetsTemplates.add(stylesheetTemplates);

				
			}
			

			// Prepare output
			//------------------
			
			//-- Output base is the folder in which the source file is located		
			File outputBaseContainer = sourceFile.getParentFile();

			
			//---- Target Output is the outputbase, adjusted to Output element in Stylesheet
			//---------------------------
			File outputTargetContainer = outputBaseContainer;
			File outputTargetFile = null;
			if (stylesheet.getOutput()!=null && stylesheet.getOutput().getPath()!=null) {
				
				//-- replace variables
				String tempOutput = stylesheet.getOutput().getPath().getValue().replace("$outputFileName$",sourceNameWithoutExtension);
				
				//-- Build a file
				outputTargetFile = TeaFileUtils.buildPathAsFile(outputBaseContainer,tempOutput);
				
				//-- If it is a non existing directory -> create and set as base container
				if (outputTargetFile.isDirectory() ) {
					outputTargetContainer = outputTargetFile;
					if (!outputTargetFile.exists())
						outputTargetFile.mkdirs();
				}
				//-- If it is a file, mkdirs the parent folder
				else {
					outputTargetFile.getParentFile().mkdirs();
				}
			
			}
			
			//-- Output result depends on output type of the last one
			Templates lastTemplate = stylesheetsTemplates.getLast();
			
			//-- Final Output target is directory+name, or already the outputTargetFile if it is a file
			File outputTarget = null;
			if (outputTargetFile.isDirectory())
				outputTarget = TeaFileUtils.buildPathAsFile(outputTargetContainer,sourceNameWithoutExtension+"."+lastTemplate.getOutputProperties().getProperty("method", "xml"));
			else
				outputTarget = outputTargetFile;

			
			// Do output
			//---------------------
		
			//-- Prepare XML source
			StreamSource sourceXMLSource = new StreamSource(sourceStream);
			sourceXMLSource.setSystemId(sourceFile.toURI().toURL().toExternalForm());
			
			//-- Transform
			
			// Previous Source starts with xml base source
			StreamSource previousSource = sourceXMLSource;
			ByteArrayOutputStream lastResult = null;
			
			// Loop over chain
			int dbgcount = 0;
			for (Templates template : stylesheetsTemplates) {
			
				// Result
				lastResult = new ByteArrayOutputStream();
				StreamResult result = new StreamResult(lastResult);
				
				
				
				// Transform
				template.newTransformer().transform(previousSource, result);
				
				// Turn result into next source
				previousSource = new StreamSource(new ByteArrayInputStream(lastResult.toByteArray()));
				
				// Debug
				//FileOutputStream outputTargetStream = new FileOutputStream(outputTarget+"_dbg_"+dbgcount+".xml");
				//outputTargetStream.write(lastResult.toByteArray());
				
				dbgcount++;
			}
			
			//-- Copy to output with the last result
			FileOutputStream outputTargetStream = new FileOutputStream(outputTarget);
			outputTargetStream.write(lastResult.toByteArray());
			
		
			//---- Copy additional resources (depending on last stylesheet path)
			//-------------------------
			if (stylesheet.getOutput()!=null) {
				for (XSDStringBuffer fileToCopy : stylesheet.getOutput().getFile()) {
					
		
					
					try {
						//-- Resource is absolute or relative to 
						URI resourceURI = new URI(fileToCopy.getValue());
						if (!resourceURI.isAbsolute()) {
							resourceURI = new URI(lastStylsheetPath.toString()+"/../"+resourceURI.getPath());
							resourceURI=resourceURI.normalize();
						}
						
						//-- IF it is a file
						if (resourceURI.getScheme()==null || resourceURI.getScheme().equals("file")) {
							
							//-- Copy Folder content if it is a folder
							File resourceFile = new File(resourceURI.getPath());
							if (resourceFile.isDirectory()) {
								
								//-- Create Target container
								
								File copiedFolder = TeaFileUtils.buildPathAsFile(outputTargetContainer,resourceFile.getName());
								if (!copiedFolder.exists()) {
									copiedFolder.mkdirs();
								}
								
								//-- Create all files in source Folder
								for (File f : resourceFile.listFiles()) {
									
									if (f.isDirectory())
										continue;
									
									//-- Prepare file
									File targetFile = TeaFileUtils.buildPathAsFile(copiedFolder,f.getName());
									if (targetFile.exists()) {
										targetFile.delete();
									}
									
									//-- Create
									FileOutputStream targetFileStream = new FileOutputStream(targetFile);
									targetFileStream.write(TeaIOUtils.swallowStream(new FileInputStream(f)));
									targetFileStream.close();
								}
								
							}
							//-- Copy File if it is a file
							else if(resourceFile.isFile() && resourceFile.exists()) {
								
								//-- Create 
								File outputFile = TeaFileUtils.buildPathAsFile(outputTargetContainer,resourceFile.getName());
								if (outputFile.exists()) {
									outputFile.delete();
								}
								
								//-- Copy data
								FileOutputStream outputFileStream = new FileOutputStream(outputFile);
								outputFileStream.write(TeaIOUtils.swallowStream(new FileInputStream(resourceFile)));
								outputFileStream.close();
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
					command = command.replace("$stylesheetFolderPath$", new File(lastStylsheetPath.toURL().getFile()).getParentFile().getAbsolutePath());
					command = command.replace("$outputFile$",outputTarget.getAbsolutePath());
					command = command.replace("$outputFileFolder$",outputTarget.getParentFile().getAbsolutePath());
					command = command.replace("$outputFileName$",outputTarget.getName().replaceAll("\\..*$", ""));
					
					//-- Separate command string on spaces to create an array
					String[] cmdArray = command.split(" ");
					
					//-- The first argument if the exe, adapt the name multi OS support
					//-- Windows: add .bat if not ending with .bat
					//-- Linux: Don't touch, if file does not exist, otherwise add sh
					if (OSDetector.getOS()==OS.LINUX) {
						//-- The rest should be windows
						File cmdPath= new File(cmdArray[0]);
						if (!cmdPath.exists())
							cmdArray[0] = cmdArray[0]+".sh";
					} else {
						//-- The rest should be windows
						cmdArray[0] = cmdArray[0].endsWith(".bat") ? cmdArray[0] : cmdArray[0]+".bat";
					}
					
					for (String str : cmdArray) {
						TeaLogging.teaLogInfo("-- Post Processing command arg: "+str);
						//ConsoleFactory.logInfo("-- Post Processing command arg: "+str);
					}
					//-- Prepare process builder
					ProcessBuilder pb = new ProcessBuilder(cmdArray);
					pb.directory(outputTargetContainer);
					pb.redirectErrorStream(true);
					

					//-- Start and redirect output to 
					Process p = pb.start();
					byte[] commandResult = TeaIOUtils.swallowStream(p.getInputStream());
					
					//-- Output to console
					TeaLogging.teaLogInfo("-- Post Processing command: "+command);
					TeaLogging.teaLogInfo(new String(commandResult));
					//ConsoleFactory.logInfo("-- Post Processing command: "+command);
					//ConsoleFactory.logInfo(new String(commandResult));
					
				}
				
			}
			
		} catch (URISyntaxException e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
		} catch (MalformedURLException e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
		} catch (IOException e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
			//throw new RuntimeException(e);
		} catch (TransformerException e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
			//throw new RuntimeException(e);
		} catch (Throwable e) {
			TeaLogging.teaLogSevere(e);
			e.printStackTrace();
		}
	}
	

	public URI getRepositoryURL() {
		return repositoryURI;
	}

	public void setRepositoryURL(URI repositoryURL) {
		this.repositoryURI = repositoryURL;
		
		
		
		
	}

	
	
	
}
