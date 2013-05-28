<?php

require_once("framework/superdoc.php");

?>
<!DOCTYPE html>
<html>
<head>
	<title>SuperDoc</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">


	<!-- Bootstrap -->
	<!-- ############## -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">

    <!-- LESS   -->
    <!-- ###### -->
    <script src="js/less-1.3.3.min.js" type="text/javascript"></script>


    <!-- Highlight -->
    <!-- ############## -->
    <link rel="stylesheet" href="http://yandex.st/highlightjs/7.3/styles/default.min.css">
	<script src="http://yandex.st/highlightjs/7.3/highlight.min.js"></script>
	<script>
  		hljs.tabReplace = '    ';
  		//hljs.initHighlightingOnLoad();
  	</script>


  	<!-- SuperDoc Styling -->
  	<!-- ################## -->
  	<link rel="stylesheet/less" type="text/css" href="css/superdoc.less" />


    <style type="text/css">

    </style>

    <script type="text/javascript">



    </script>

    <style type="text/css">

    	table {
    		border-collapse: collapse;
    		margin-bottom: 15px;

    	}
	    td,th {

	    	border: 1px gray solid;
	    	padding: 3px;
	    }
	    th {
	    	text-align:center;
	    	background-color:black;
	    	color:white;
	    	font-weight:  bold;
	    }
	    #menu-span {
	    	position: fixed;
	    	overflow:scroll;
	    	height: 800px;
	    	margin-top: 60px;
	    }
	    #content-span {
	    	margin-top: 70px;
	    	margin-left: 500px;
	    }

        h1 {
            font-size: 17pt;
        }
        h2 {
            font-size: 15pt;
            margin-left: 10pt;
        }
        h3 {
            font-size: 13pt;
            margin-left: 20pt;
        }
        h4,h5,h6 {
            font-size: 11pt;
            margin-left: 30pt;
        }

    </style>

</head>
<body>

	<script src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="js/superdoc.js"></script>


    <!-- Init -->
	<!-- ##### -->
	<script>
		$(document).ready(function() {

			console.log("Loaded");

			// Menu
			//------------------

			//--- Make all empty group invisible
			//-------------------------
			$("#menu-span li:not(:has(a))").each(function(i,e) {

				// Is there a a somewhere under e ?
				//-----------------
				//console.log("Found something to hide: ")
				$(e).css("display","none")


			});

			$("#menu-span ul:has(a)").each(function(i,e) {

				// Is there a a somewhere under e ?
				//-----------------
				//console.log("Found something to show: ")
				//$(e).css("display","block")


			});

			//---- Make as high as the window
			//-----------------------------------
			var menuHeight= $(document).height();
			console.log("Main page height: "+menuHeight);
			$("#menu-span").height(menuHeight-60);

			// Content
			//----------------------

			//---- Move on the right by the width of the menu
			//-------------------------
			var menuWidth = $("#menu-span").width();

			//console.log("Menu width: "+menuWidth);

			$("#content-span").css("margin-left",(menuWidth+20)+"px");


		});

	</script>


    <!-- Top navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">

		<!-- Navboar content -->
        <div class="container-fluid">

			<!-- Icons -->
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
			</a>
			<!-- Name -->
          	<a class="brand" href="#">SuperDoc</a>

          	<!-- Links for real navigation -->
          	<div class="nav-collapse collapse">
<!--             	<p class="navbar-text pull-right"> -->
<!--               		Logged in as <a href="#" class="navbar-link">Username</a> -->
<!--             	</p> -->
            	<ul class="nav">
              		<li class="active"><a href="#">Home</a></li>
            	</ul>
         	</div><!--/.nav-collapse -->
        </div>
      </div>
    </div>


	<div class="container-fluid" id="main-page">
      <div class="row-fluid">

        <!-- Sidebar Navigation -->
        <!-- ########################## -->
        <div class="span3" id="menu-span">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header">Modules</li>

              <?php

              // Per default, show all top groups that are not hidden
              //---------------------------
              $groups = array();


              // If there is a $_GET[group], then select only this group
              if (!empty($_GET['group'])) {



              	$groups[] = $docMap->getGroup($_GET['group']);


              } else {

					foreach($docMap->getGroups() as $group) {
						if ($group->hidden==false) {
							$groups[] = $group;
						}
					}

			  }

              // Show
              //---------------------------
              function groupContent(DocGroup $group) {



					echo "<li class=\"nav-header\">$group->name";
					echo "<ul>";
					// Show Documents
					//-------------------
					foreach ($group->getDocuments() as $document) {

						if ($document->isLink()) {
							echo "<li><a href=\"file://$document->documentFile\" target=\"_blank\">$document->name</a></li>";
						} else {
							echo "<li><a href=\"javascript:loadDocument('$document->documentFile')\">$document->name</a></li>";
						}


					}

					// Show Groups
					//---------------------
					foreach ($group->getGroups() as $subgroup) {
						groupContent($subgroup);
						//echo "<li><a href=\"#\">$document->name</a></li>";
						//echo
					}
					echo "</ul>";
					echo "</li>";
			 }

              foreach($groups as $group) {
					groupContent($group);

			 }

              ?>


            </ul>
          </div><!--/.well -->
        </div><!--/span-->

        <!-- Content -->
        <!-- ########################## -->
        <div class="span6" id="content-span">


        </div>
        <!--/content span-->

      </div>
      <!--/row-->



      <footer>
        <p>&copy; SuperDoc 2013</p>
      </footer>

    </div>
    <!--/.fluid-container-->


</body>
</html>
