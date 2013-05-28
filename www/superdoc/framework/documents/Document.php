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
	
	public $link = false;
	
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
	 * Return true if this document should be just jumped to
	 */
	function isLink() {
		return $this->link;
	}
	
	
	/**
	 * Returns HTML version
	 */
	function toHTML() {
		
		//-- Output XHTML
		$xhtml = "<div>".Markdown(file_get_contents($this->documentFile))."</div>";
		
		error_log("markdown output:".substr( $xhtml, -500));
		
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