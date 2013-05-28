<?php 
require_once("../framework/superdoc.php");
//error_reporting(E_NONE);
ini_set('display_errors', '0');

// Get Function
//--------------------
$function = $_GET['function'];
if (empty($function)) {
	header('HTTP/1.1 500 Internal Server Error');
	exit(-1);
}

// Define map
//--------------------------
$functions = array(

		"loadDocument" => 1,
		"loadImage" => 1,
		"loadResource" => 1
		
);
if (!array_key_exists($function,$functions)) {
	error_log( "Function:$function not registered");
	header('HTTP/1.1 500 Internal Server Error');
	exit(-1);
}

// Functions
//-----------------------------
header("Content-Type: text/xml",true);

//---- Load Document
//---- $_GET[document]= path to document
//-----------------------------
function loadDocument() {
	
	// Result is XML
	//-------------------------
	
	// Create Document
	//----------------------------------
	$document = new Document($_GET['document']);
	
	$res ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<contents>";
	$res.= "<content id='$document->name' type='text/markdown'><![CDATA[".$document->toHTML()."]]></content>";
	$res.="</contents>";
	
	#error_log("Result: $res");
	
	echo $res;
	//return $res;
}

//---- Load Image
//---- $_GET[path]= path to document
//-----------------------------
function loadImage() {
	
	
	// Get Image path
	//-----------
	$path = $_GET['path'];
	
	// Read to output
	//---------------------
	readfile($path);
	
}

//---- Load Resource
//---- $_GET[document]= path to document
//-----------------------------
function loadResource() {
	
	
	// Get Image path
	//-----------
	$path = $_GET['path'];

	// Content-types
	//---------------------
	$supportedTypes = array (

		"@.+\.pdf$@" => "application/pdf"
	);
	$found=false;
	foreach ($supportedTypes as $expression => $contentType) {
		if(preg_match($expression, $path)==1) {
			header("Content-Type: $path",true);
			$found=true;
			break;
		}
	}

	// If Found, Fail, otherwise set attachment
	//------------------
	if (!$found) {
		header('HTTP/1.1 500 Internal Server Error');
		exit(-1);
	} else {

		//-- Name is last / path


		header('Content-Disposition: attachment; filename="'.basename($path).'"');


	}

	// Read to output
	//---------------------
	readfile($path);
	
}


// Call
//-------------------
$res = call_user_func($function);
#error_log("Result2: $res");
//echo $res;
?>