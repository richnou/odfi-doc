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

		"loadDocument" => 1
		
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
	
	error_log("Result: $res");
	
	//echo $res;
	return $res;
}

// Call
//-------------------
$res = call_user_func($function);
error_log("Result2: $res");
echo $res;
?>