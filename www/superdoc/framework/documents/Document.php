<?php 

require_once dirname(__FILE__).'/markdown/markdown.php';

/**
 * A Document file that can be converted to various formats
 * 
 * @author rleys
 *
 */
class Document {
	
	/**
	 * Path to file for the document
	 * @var unknown
	 */
	public $documentFile = null;
	
	/**
	 * Name of document. Name of the documentFile per default
	 * @var unknown
	 */
	public $name = null;
	
	function __construct( $filePath) {
		$this->documentFile = $filePath;
		$this->name = basename($this->documentFile);
	}
	
	
	/**
	 * Returns HTML version
	 */
	function toHTML() {
		
		//-- Output XHTML
		$xhtml = "<div>".Markdown(file_get_contents($this->documentFile))."</div>";
		
		//-- Parameters for images
		$imagesParameters = array("basePath" => dirname($this->documentFile));
		
		//-- Add TOC + images
		$transforms = array(
					
				dirname(__FILE__)."/xsl/markdown_toc.xsl" => array(),
				dirname(__FILE__)."/xsl/images.xsl" => $imagesParameters,
				dirname(__FILE__)."/xsl/odfi-doc-extract.xsl" => $imagesParameters,
					
					
		);
		$xhtml =  transformChain($xhtml,$transforms);
		
		return  $xhtml;
		
	}
	
}


?>