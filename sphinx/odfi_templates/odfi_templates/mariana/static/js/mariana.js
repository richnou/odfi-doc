var odfi = {

	mariana: {

		attributeStack: []
	}
}





$(function() {

	// Semantic UI Support
	//-----------------
	console.log("Setting up table");
	$("#mariana-page  table").each(function(i,e) {
		if (! $(e).hasClass("highlighttable")) {
			$(e).addClass("ui compact table");
		}
		
	});

	$("#mariana-page blockquote").each(function(i,e) {
		$(e).addClass("ui existing segment");
	});

	//--- Steps 
	$("#mariana-page .steps .step .caption").each(function(i,e) {
		$(e).addClass("ui  info message");
	});

	// Highlight Support
	//---------------------
 	$('pre code').each(function(i, block) {
 		console.log("Highlighting block...");
    	hljs.highlightBlock(block);
  	});
	//hljs.initHighlightingOnLoad();

	// Sticky Header
	//------------------------
	$('#mariana-page .mariana-header').sticky();

	// Sticky Menu Setup
	//------------------------
	$('#mariana-page .mariana-menu').sticky({
	    context: '#mariana-page-content'
	  });

	// TOC Conversion
	//---------------------
	console.info("Converting TOC 2");

	

	$("#toc > ul").wrap("<div class='ui vertical menu'/>").contents().unwrap();
	$('.toctree-l1>a').unwrap().wrap("<div class='item'/>").wrap("<div class='header'/>");


	// Add Animations and SVg manipulations Functions
	//------------------------------
	$.fn.svg = {};
	$.fn.pattr = function(name,value) {

		$(this).each(function (i , target) {

			var attrId = $(target).attr('id')+"."+name;
			var oldValue = odfi.mariana.attributeStack[attrId];
			console.log("pattr "+name+" -> "+value+" , old= "+oldValue+" on "+$(target).attr("id"));
			if (value) {
				odfi.mariana.attributeStack[attrId] = $(target).attr(name);
				$(target).css(name,value);
			} else if (odfi.mariana.attributeStack[attrId] ) {
				$(target).css(name,odfi.mariana.attributeStack[attrId]);
			} else {
				$(target).css(name,"");
			} 

			//$(target).trigger('resize');

		});


		 
	
	};

	$.fn.stroke = function(name,width) {

		$(this).each(function (i , target) {
			$(target).pattr("stroke",name);
			$(target).pattr("stroke-width",width);
		});

	};

	$.fn.fadein = function() {
		$(this).animate({opacity: 1.0},500);
	};
	$.fn.fadeout = function() {
		$(this).animate({opacity: 0.0},500);
	}; 
	$.fn.svgHide = function() {
		$(this).attr("opacity","0.0");
	};
	$.fn.move = function(dx,dy) {

		dx = dx || 0;
		dy = dy || 0;
		var targetX = parseFloat($(this).attr("x")) + dx;
		var targetY = parseFloat($(this).attr("y")) + dy;

		console.log("Moving: "+targetX+" by "+dx);
		$(this).animate({x: targetX , y: targetY},500);
	};

	$.fn.also = function() {

		var calledOn = this[0];
		console.log("Called with on: "+this[0]);
		
			
		if (arguments) {
			
			var newelements = [];
			newelements.push(calledOn);
			var targetElements= $(arguments).map(function(i,targetLabel) {

				var found = $(calledOn).parent().find("*[id='"+targetLabel+"']:first")[0];
				newelements.push(found);
				console.log("Mapping "+targetLabel+" -> "+found+ ", search on: "+$(calledOn).parent());
				return found;
			});

			return $(newelements);
		

		} else {
			return this;
		}
	
		return this;

	};



	// SVG Setup
	// Trigger onload, set initial displays
	// At the end display pictures
	//----------------------------------------


	$('svg').each(function(i,svg) {

		// Trigger On Load
		//----------
		$(svg).find("*[onload]").each(function(i,withOnLoad) {
			$(withOnLoad).trigger("load");
		});

		// Look for animation
		//-------------------
		var topGroup = $(svg).find("g:first");
		var descOfGroup = $(topGroup).find("desc");

		console.log("Desc id: "+$(descOfGroup).attr("id"));
		console.log("Desc of troup: "+$(descOfGroup).text());

		//-- Split at line 
		var allLines = $($(descOfGroup).text().split("\n")).filter(function() { return this.trim().length>0; });

		//-- Set animation if some is defined
		if (allLines.length>0) {


			// Create Animation Stack
			var animations = [];

			// parse line
			allLines.each(function(i,line) {

				line = line.trim();

				console.info("Animation Line: "+line);

				// Init Animation

				// SNAP Animation
				if (line.startsWith("S:")) {
					var snapLine = line.replace("S:","").trim();
					//console.info("Snap Line: "+snapLine);

					// Take first NAME.
					// Name is a label
					var labelmatch = snapLine.match(/\w+/g);
					if (labelmatch) {
						var targetLabel = labelmatch[0];
						var animationLine = snapLine.replace(targetLabel+".","");
						//console.info("Label: "+targetLabel);

						//-- Search Element matching label inkscape\\:
						//var searchCode = "$(\"*[inkscape\\\\:label='"+targetLabel+"']:first\")";
						//var targetElement = $("*[label='"+targetLabel+"']:first");
						var searchCode = "$(\"*[inkscape\\\\:label='"+targetLabel+"']\")";
						var targetElement = $("*[inkscape\\:label='"+targetLabel+"']");
						if (targetElement) {

							//console.info("Found target SVG Element");

							//-- animations[animations.length] 
							animations.push( function() {

								var finalCode = searchCode+".each(function(i,e) { $(e)."+animationLine+"; });";
								console.info("Doing Animation Step: "+finalCode);
								eval(finalCode);
								
								//console.info("Element: "+targetElement);
								//$(targetElement).animate({opacity: 0.5},1000);
							});


						} else {
							console.warn("Impossible to find element for label: "+targetLabel);
						}
					} 
					

				}

			});

			// Set mouseclick handler
			$(topGroup).data("animation-map",animations);
			$(topGroup).on("click",function() {

				var func = $(this).data("animation-map").shift();
				if (func) {
					func.apply();
				}
				

			});

		}
		

		// Display Container
		//---------------
		var parent = $(svg).parent();
		parent.css("visibility","visible");

	});
	// EOF SVG Special Support
	

	// PDF JS support
	//-----------------------
	console.log("Init pdf js....");
	//PDFJS.imageResourcesPath = '../web/images/';
	//PDFJS.workerSrc = 'pdf.worker.js';
	//PDFJS.cMapUrl = '../web/cmaps/';
	PDFJS.cMapPacked = true;
	$(".pdfjs").each(function(i,pdfCanvas) {

		var url = $(pdfCanvas).data("file");
		PDFJS.getDocument(url).then(function(pdf) {
			console.log("Loaded PDF");
			if ($(pdfCanvas).data("page")) {
				pdfjs.changeToPage(pdfCanvas,pdf,$(pdfCanvas).data("page"));
			} else {
				pdfjs.changeToPage(pdfCanvas,pdf,1);
			}
			//indesign.pdfjs.pdfDoc[$(canvas).attr("id")] = pdf; 
			//indesign.pdfjs.changeToPage($(canvas).attr("id"),$(canvas).attr("page"));
		});


	});




	

});

// PDF JS stuff
//-------------------
pdfjs={};
pdfjs.changeToPage =  function(canvas,doc,p) {
	
	p = parseInt(p);
	
	//-- Get Doc
	//var doc = indesign.pdfjs.pdfDoc[id];
	console.log("Changing page  to "+p+ " with available "+doc.numPages);
	
	
	if (p<1 || p > doc.numPages) {
		return;
	}
	
	// Change Local Page
	// --------------------
	doc.getPage(p).then(function(page) {
		
		// Update Page
		//----------
		var pdfPage = p;
		console.log("PDF Page...");
		//var canvas = document.getElementById(id);
		$(canvas).attr("page",p);
		var context = canvas.getContext('2d');
		
		// Remote Update
		//-------------
		//$.get("/h2dl/action/pdfjs.updatePage?page="+p);
		
		/*
		 * var desiredWidth = 100; var viewport = page.getViewport(1);
		 * var scale = desiredWidth / viewport.width; var scaledViewport =
		 * page.getViewport(scale);
		 */
		
		
		
		var desiredWidth = $(canvas).parent().width();
		//$(canvas).width(desiredWidth);
		 var viewport = page.getViewport(1);
		 var scale = desiredWidth / viewport.width; 
		 var scaledViewport = page.getViewport(scale);
		 canvas.height = scaledViewport.height;
		 canvas.width = scaledViewport.width;
		 /* var scale = 1.0;
		var viewport = page.getViewport(scale);
		var context = canvas.getContext('2d');
		canvas.height = viewport.height;
		canvas.width = viewport.width;*/
		
		var renderContext = {
		  canvasContext: context,
		  viewport: scaledViewport
		};
		page.render(renderContext);
	});
	
}

pdfjs.nextPage =  function(id) {
	
	// -- Get Current Page
	indesign.pdfjs.changeToPage("pdfjs-"+id,parseInt($("#pdfjs-"+id).attr("page"))+1);
}
pdfjs.previousPage =  function(id) {
	
	// -- Get Current Page
	indesign.pdfjs.changeToPage("pdfjs-"+id,parseInt($("#pdfjs-"+id).attr("page"))-1);
}