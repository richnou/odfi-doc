<?php 

// Load Classes
//---------------------
function __autoload($class_name) {
	
	$locations = array("","wikiconvert","docmap");
	foreach ($locations as $loc) {
		
		$incFile = dirname(__FILE__)."/$loc/$class_name.php";
		
		if (is_file($incFile)) {
			require_once($incFile);
			break;
		}
	}

}


// Setup
//-----------
$docMap = new DocMap();



// Load Configuration
//-----------------------
require_once("config/config.php")




?>