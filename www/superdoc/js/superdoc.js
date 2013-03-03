function loadDocument(documentPath) {

			var url = "rest/api.php?function=loadDocument&document="+documentPath;
			console.log("calling: "+url);
			
			
			$.get(url,function(responseContent,statusTxt,xhr) {

			    if(statusTxt=="success") {

					//-- Clear doc content
					var docContent = $("#content-span");
					docContent.empty();

			
					var response = xhr.responseXML;
					console.log("resp: "+response);
					
					//-- Prepare nav bar
					var navbar = $("<ul class=\"nav nav-tabs\"></ul>");
					docContent.append(navbar);

					//-- Prepare content holder
					var contentHolder = $("<div id='content-holder'></div>");
					docContent.append(contentHolder);
					
					$(response.getElementsByTagName("content")).each(function (i,e) {
						
						//console.log("Found content : "+e.textContent);

						// prepare id for content
						var contentId = "content-"+$(e).attr("id");

						// Prepare real content
						
						// Add navbar tab
						var navelt = $("<li class='active'><a href=\"#\">"+$(e).attr("id")+"</a></li>");
						navbar.append(navelt); 

						// Define nav click function
						navelt.click(function() {
							

							// For all contents, hide unless id matches the one of this function
							$("#content-span #content-holder div[id^='content']").each(function(i,e) {
								//console.log("Checking on content: "+$(e).attr('id'));
								if ($(e).attr('id')==contentId)
									$(e).fadeIn();
								else
									$(e).fadeOut();
							});
							
						});


						// Add content
						var content = $("<div id='"+contentId+"' style='display:none'>"+$(e).text()+"</div>"); 
						contentHolder.append(content);

						// In mediawiki case, recapsulate the code blocks
						/*$('code').each(function (i,e) {
			    		
				    		// Wrap code in pre to allow highlighting
			        	  	var precode=$("<pre></pre>");
				            $(e).after(precode);
				            $(e).remove();
				            $(precode).append(e)
				         
			          	

		          		});*/

						//-- Click on first navigation element to view content
						if (i==0)
							navelt.click();	
						
					});
					// Eof each content
					
					// Highlight all code
					$('#content-span #content-holder code').each(function(i, e) {
	  		  			hljs.highlightBlock(e);
  						
	  	 			});
					
			
			    }
			    if(statusTxt=="error")
			        alert("Error: "+xhr.status+": "+xhr.statusText);
		    });
			
}