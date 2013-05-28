<?php 
error_reporting(E_ALL);
ini_set('display_errors', '1');
ini_set('log_errors_max_len',0);

// Load Classes
//---------------------
function __autoload($class_name) {
	
	$locations = array("","wikiconvert","docmap","documents","utils");
	foreach ($locations as $loc) {
		
		$incFile = dirname(__FILE__)."/$loc/$class_name.php";
		
		if (is_file($incFile)) {
			require_once($incFile);
			break;
		}
	}

}

// Load functions
//----------------------------
require_once dirname(__FILE__).'/utils/utils.php';
require_once dirname(__FILE__).'/documents/xsl/xsl_functions.php';

// Setup
//-----------
$docMap = new DocMap();



// Load Configuration
//-----------------------
require_once( dirname(__FILE__)."/../config/config.php")




?>