<?php 
require_once("../framework/superdoc.php");
//error_reporting(E_ALL);
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


//---- Load Document
//---- $_GET[document]= path to document
//-----------------------------
function loadDocument() {
	
	// Result is XML
	//-------------------------
//	error_log("Loading document: $_GET[document]");

	// Create Document
	//----------------------------------

	$document = new Document($_GET['document']);
	
	header("Content-Type: text/xml",true);

	$res ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<contents>";
	$res.= "<content id='$document->name' type='text/html'><![CDATA[ ".$document->toHTML()." ]]></content>";
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

		"@.+\.pdf$@" => "application/pdf",
		"@.+\.png$@" => "image/png",
		"@.+\.svg$@" => "image/svg"
	);
	$found=false;
	$type = "";
	foreach ($supportedTypes as $expression => $contentType) {
		if(preg_match($expression, $path)==1) {
			header("Content-Type: $path",true);
			$found=true;
			$type=$path;
			break;
		}
	}

	// If Found, Fail, otherwise set attachment
	//------------------
	if (!$found || !(file_exists($path))) {
		header('HTTP/1.1 500 Internal Server Error');
		exit(-1);
	} else if ($type=="application/pdf") {

		//-- Name is last / path


		header('Content-Disposition: attachment; filename="'.basename($path).'"');


	} else {
		header('Content-Disposition: filename="'.basename($path).'"');
	}

	// Read to output
	//---------------------
	readfile($path);
	
}


// Call
//-------------------

//error_log("Calling API: $function");

$res = call_user_func($function);
#error_log("Result2: $res");
//echo $res;
?>
