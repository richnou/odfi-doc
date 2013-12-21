<?php


// Documentation
//--------------------


//---- ODFI
//------------------
$group = $docMap->newGroup("ODFI");
//$group->docSource = "/nfs/home/rleys/git/odfi/modules/global/scripts-manager/doc";
$group->docSource[] = "/nfs/home/rleys/git/odfi/modules-manager/install";
$group->docSource[] = "/home/rleys/local/git/odfi/modules-manager/install/";
$group->addIgnore("*private");
$group->addIgnore("*tests");
$group->addIgnore("*www-app");
$group->addIgnore("*external");
$group->addIgnore("*target");

$group = $docMap->newGroup("Quality Validation");
$group->docSource[] = "/local/home/rleys/git/odfi/modules-manager/install/testing-quality-validation/validation-scala-lib/target/generated-sources/markdown";



//---- Extoll R2
//--------------
$group = $docMap->newGroup("extoll2-specification");
$group->docSource[] = "/nfs/home/extoll/r2/Specifications";

$group = $docMap->newGroup("extoll2-sw");
$group->docSource[] = "/local/home/rleys/git/svn-cag/sw/extoll_r2/doc/";


//---- Extoll
//--------------
$group = $docMap->newGroup("Driver Generator");
$group->docSource[] = "/nfs/home/rleys/git/sw/cag_device_driver/doc/";


$group = $docMap->newGroup("Extoll ASIC");
$group->id = "extoll-asic";
$group->docSource[] = "/local/home/rleys/git/svn-asic/doc";


$group = $docMap->newGroup("MEX");
$group->id = "extoll-mex";
$group->docSource[] = "/local/home/rleys/git/extoll2/mex/doc";
$group->docSource[] = "/local/home/rleys/git/extoll2/mex/backend/doc";


//---- OSI
//-------------------
$group = $docMap->newGroup("OSI");
$group->id = "osi";
$group->addIgnore("*private");
$group->addIgnore("*tests");
$group->addIgnore("*www-app");
$group->addIgnore("*external");
$group->addIgnore("*build");
$group->addIgnore("*target");
$group->docSource[] = "/local/home/rleys/git/osi/";
$group->docSource[] = "/local/home/rleys/git/osi/virtualui-core/";


//---- Airborn
$group = $docMap->newGroup("Airborn");
$group->id = "airborn";
//$group->hidden = true;
$group->addIgnore("*private");
$group->addIgnore("*tests");
$group->addIgnore("*www-app");
$group->addIgnore("*external");
$group->addIgnore("*build");
$group->addIgnore("*target");
//$group->docSource[] = "/home/rleys/projects/airborn-cabletester/";
$group->docSource[] = "/home/rleys/local/git/airborn-cabletester/";

//---- Ulrich
//--------------------


$groupUlrich2 = $docMap->newGroup("ulrich");
$groupUlrich2->hidden = true;
$groupUlrich2->docSource[] = "/local/home/rleys/git/phd/diss/ulrich/";


//---- master: Felix
//--------------------------
$groupFelix = $docMap->newGroup("felix");
$groupFelix->hidden = true;
$groupFelix->docSource[] = "/home/rleys/nfs/students/master/ss13-felix";
$groupFelix->docSource[] = "/nfs/home/fzahn/master_thesis/";
//$groupFelix->docSource[] = "/local/home/rleys/git/extoll2/mex/doc";
?>
