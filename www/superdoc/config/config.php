<?php


// Documentation
//--------------------


//---- ODFI
//------------------
$group = $docMap->newGroup("ODFI");
//$group->docSource = "/nfs/home/rleys/git/odfi/modules/global/scripts-manager/doc";
$group->docSource = "/nfs/home/rleys/git/odfi/modules-manager/";
$group->addIgnore("*private");
$group->addIgnore("*tests");

//---- Extoll R2
//--------------
$group = $docMap->newGroup("extoll2-specification");
$group->docSource = "/nfs/home/extoll/r2/Specifications";

$group = $docMap->newGroup("extoll2-sw");
$group->docSource = "/local/home/rleys/git/svn-cag/sw/extoll_r2/doc/";


//---- Extoll
//--------------
$group = $docMap->newGroup("Driver Generator");
$group->docSource = "/nfs/home/rleys/git/sw/cag_device_driver/doc/";


$group = $docMap->newGroup("Extoll ASIC");
$group->id = "extoll-asic";
$group->docSource = "/local/home/rleys/git/svn-asic/doc";



//---- Ulrich
//--------------------


$groupUlrich2 = $docMap->newGroup("ulrich");
$groupUlrich2->hidden = true;
$groupUlrich2->docSource = "/local/home/rleys/git/phd/diss/ulrich/";


//---- master: Felix
//--------------------------
$groupFelix = $docMap->newGroup("felix");
$groupFelix->hidden = true;
$groupFelix->docSource = "/home/rleys/nfs/students/master-2013-felix";

?>
