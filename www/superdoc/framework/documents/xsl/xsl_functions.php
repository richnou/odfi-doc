<?php

/**
 * Determing if a <svg tag is for an SVG image
 * @param unknown $url
 * @return boolean
 */
function isSVGImage($url) {
	
	if (preg_match("@^.*svg$@",$url[0])===FALSE)
		return false;
	else
		return true;
	
}


/**
 * Lists the folders in given folder, matching the provided pattern
 * @param unknown $folder
 * @param unknown $pattern
 * @param unknown $basePath
 */
function odfiExtrasListFolder($folder,$pattern,$basePath) {
	
	
}


/**
 * Extracts from the given file the text section between:
 * 
 * sect: $section
 * 
 * eof-sect: $section
 * 
 * @param unknown $file
 * @param unknown $section
 */
function odfiSectionExtract($file,$section,$basePath) {
	
	
	// Adapt file path if relative to basepath
	//--------------------
	if ($file[0]!='/') {
		$file = $basePath.'/'.$file;
	}
	
	error_log("Extracting from: $file, section $section, with base path: $basePath");
	
	
	// Get Content trimmed
	//-----------------
	$matches = array();
	$res = preg_match("/sect:\s*$section\s+(.*)eof-sect:\s*$section\s+/s",file_get_contents($file),$matches);
	
	$doc = $matches[1];
	
	// First character is the comment delimiter
	//------------------
	$commentChar = $doc[0];
	error_log("Matching: $res, comment char: $commentChar");
	
	error_log("Doc: $doc");
	
	$doc = str_replace("$commentChar","",$doc);
	
	$doc = Markdown($doc);
	error_log("Clean Doc: $doc");
	
	if (preg_last_error() == PREG_NO_ERROR) {
		error_log( 'There is no error');
	}
	else if (preg_last_error() == PREG_INTERNAL_ERROR) {
		error_log( 'There is an internal error!');
	}
	else if (preg_last_error() == PREG_BACKTRACK_LIMIT_ERROR) {
		error_log( 'Backtrack limit was exhausted!');
	}
	else if (preg_last_error() == PREG_RECURSION_LIMIT_ERROR) {
		error_log( 'Recursion limit was exhausted!');
	}
	else if (preg_last_error() == PREG_BAD_UTF8_ERROR) {
		error_log( 'Bad UTF8 error!');
	}
	else if (preg_last_error() == PREG_BAD_UTF8_ERROR) {
		error_log( 'Bad UTF8 offset error!');
	}
	
	
	//echo "Matching: $res";
	
	// Clean all line beginings from comment delimiter
	//---------------
	
	$DOM = new DOMDocument;
	$DOM->loadHTML("<div>".$doc."</div>");
	return $DOM;
	
	
}


function transform($xml, $xsl,$parameters = array()) {
	
	//-- Create XSLT
	$xslt = new XSLTProcessor();
	$xslt->importStylesheet(new  SimpleXMLElement($xsl));
	$xslt->registerPHPFunctions();
	
	// Set parameters
	foreach($parameters as $name => $value) {
		
		$xslt->setParameter ("", $name ,  $value );
		
	}
	
	//-- Parse XML
	$DOM = new DOMDocument;
	$DOM->loadHTML($xml);
	
	//-- Transform
	return $xslt->transformToXml($DOM);
}

/**
 * Chains transform the xml using provided map between XSLT and parameters
 * @param unknown $xml
 * @param unknown $xsls
 */
function transformChain($xml,$xsls = array( path =>  array())) {
	
	foreach ($xsls as $xslt => $parameters) {
		
		//-- transform
		$xml = transform($xml,file_get_contents($xslt),$parameters);
		
		
	}
	
	return $xml;
	
}

?>