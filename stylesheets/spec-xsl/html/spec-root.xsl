<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook"
	xmlns="http://www.w3.org/1999/xhtml">
 
    <!-- HTML5 Output -->
    <xsl:output
        method="html"
        />
    
	<xsl:template match="//docbook:article">

		<!-- ## HTML structure -->
		<!-- ########################### -->
		<html class="dj_webkit dj_chrome dj_contentbox">
			<head>
			
            
				<!-- Spec -->
				<!--  <link href="http://lebleu/docbook/spec-xsl/html/css/spec-default.css" rel="stylesheet/less" type="text/css"/>-->
                <link href="./spec-default.css" rel="stylesheet/less" type="text/css"/>
				<script src="./js/spec.js" type="text/javascript"></script>
                
				<!-- Less -->
				<script src="./js/less-1.1.4.js" type="text/javascript"></script>
				
				<!-- Dojo -->
				<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dijit/themes/claro/claro.css"/>
				<script src="http://ajax.googleapis.com/ajax/libs/dojo/1.6.0/dojo/dojo.xd.js" data-dojo-config="isDebug: true,parseOnLoad: true" ></script>
				<script>
					dojo.require("dijit.Tooltip");
					dojo.require("dijit.layout.BorderContainer");
					dojo.require("dijit.layout.TabContainer");
					dojo.require("dijit.layout.ContentPane");
                    dojo.require("dijit.TitlePane");
                    dojo.require("dijit.Dialog");
				</script>  
			</head>
			<body class="claro" onload="specLoaded()">
				
			<!-- ## Title of the spec -->
			<!-- ###################### -->
			<div>
				<h1><xsl:value-of select="./docbook:info/docbook:title/text()"></xsl:value-of></h1>
				<div>
					Generated on <xsl:value-of select="fn:current-dateTime()"></xsl:value-of>
				</div>
			</div>
            
            <!-- ## Legalnotice -->
			
			<!-- ## Info box: Author, Revison history etc... -->
            <!-- ###################### -->
			<xsl:apply-templates mode="info"></xsl:apply-templates>
			
            <!-- ## Flying menu -->
            <!-- #################### -->
			<div class="spec-flying-menu" 
                 id="spec-flying-menu"
                 dojoType="dijit.TitlePane" 
                 title="Menu">
                  
                 <!-- Put a TOC in the flying menu :-) -->
                 <div 
                    id="spec-flying-menu-toc"
                    dojoType="dijit.TitlePane" 
                    title="Table of Contents">
                    
                    <xsl:call-template name="toc"/>
                </div> 
            </div>
                        
            
			<!-- ## Build a TOC? -->
			<!-- ########################## -->
            <xsl:if test="./docbook:toc">
                <div class="toc">
                    <div class="toc-title">Table of contents</div>
    			     <xsl:call-template name="toc"></xsl:call-template>
    			</div>
            </xsl:if>
			
			<!-- ## Normal Content -->
			<!-- ########################## -->
			<xsl:apply-templates mode="content"></xsl:apply-templates>
			
				
				
				
			
			</body>
		</html>


	</xsl:template>
	
	<!-- Generate a toc -->
	<xsl:template name="toc" match="docbook:toc" mode="toc">
	   
    	   <!-- ## Go through Sections to build outline -->
    	   <!-- ######################################## -->
           <xsl:for-each select="docbook:section"  >
               <xsl:call-template name="toc-section">
               </xsl:call-template>
           </xsl:for-each>

	
	</xsl:template>
	
	
	<!-- TOC-Section, analyses sections to generate the TOC -->
	<xsl:template name="toc-section" match="docbook:section" mode="toc">
	
		
		<!-- ## Prepare Datas -->
		<!-- ####################################### -->
		<xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
		<xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
        
		<!-- ## Display title with anchor link -->
		<!-- ####################################### -->
		<xsl:element name="div">
		
		  <!-- ## Class of title -->
          <xsl:attribute name="class">toc-level-<xsl:value-of select="$toc-hier-level"/>
          </xsl:attribute>
		  <a href="">
		      <!-- ## Anchor link to section -->
		      <xsl:attribute name="href">#toc-section-ref-<xsl:value-of select="$toc-hier-pos"/>
                </xsl:attribute>
		      <xsl:value-of select="$toc-hier-pos"/>
		      <xsl:text>. </xsl:text>
		      <xsl:value-of select="./docbook:title/text()"></xsl:value-of>
		  </a>
		</xsl:element>
	
		
		<!-- Call Sub Section template -->
		<xsl:for-each select="docbook:section"  >
			<xsl:call-template name="toc-section"></xsl:call-template>
		</xsl:for-each>
		
		
	</xsl:template>
	
	<!-- 
        To handle the output of an info box for the document 
        
        - Revision history Should be formated like this:
        
        <revhistory>
            <revision>
                <date>2011-12-11</date>
                <authorinitials>Richard Leys</authorinitials>
                <revdescription>
                    <para>First draft of this document</para>
                </revdescription>
            </revision>
            <revision>
                <date>2011-14-11</date>
                <revdescription>
                    <para>First draft of this document</para>
                </revdescription>
            </revision>
            <revision>
                <date>2011-13-11</date>
                <revdescription>
                    <para>First draft of this document</para>
                </revdescription>
            </revision>
        </revhistory>
        
        - Abstract :
        
        <abstract>
            <para>
                This is the Exelerate Spec intended to provide full
                details on using/designing an accelerator for the
                extolls exelerate interface
            </para>
        </abstract>
        
        
    -->
	<xsl:template match="docbook:info" mode="info">
	 
	   <div class="spec-info">
	
		   <!--## Make it a tab container -->
		   <!--##################################  -->
		   <div dojoType="dijit.layout.TabContainer" style="width: 100%;" doLayout="false">
		   
		      <!-- Abstract Tab -->
              <xsl:if test="./docbook:abstract">
                <div dojoType="dijit.layout.ContentPane" title="Abstract" selected="true">
                    <div class="spec-info-abstract">
                    
                        <xsl:apply-templates select="./docbook:abstract" mode="content"/>
                    
                    </div>
                    
                    
                </div>
              </xsl:if>
           
			   <!--#### Author and global infos tab -->
		       <div dojoType="dijit.layout.ContentPane" title="Info" selected="true">
	                
	                <!--#### Author -->
	                <div class="spec-info-authors">
	                    <h1>Authors:</h1>
	                    
	                    <!-- # Support: personname and email -->
						<xsl:for-each select="docbook:author">
					      <xsl:call-template name="author-name-email">
                            <xsl:with-param name="extended">true</xsl:with-param>
                          </xsl:call-template>
						</xsl:for-each>
	                </div>	
		        </div>
	        
		        <!--#### Revision History -->
		        <xsl:if test="docbook:revhistory">
			        <div dojoType="dijit.layout.ContentPane" title="Revision History">
			            <div class="spec-info-revhistory">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Author</th>
                                        <th>Description</th>
                                    </tr>
                                </thead>
                            
			                    <tbody>
									<xsl:for-each select="./docbook:revhistory/docbook:revision">
                                        
                                        <!-- Try to sort -->
                                        <xsl:sort select="./docbook:date" order="descending"></xsl:sort>
                                        
                                        <!-- Revison Line -->
									   <tr>
									       <td>
                                                <xsl:value-of select="./docbook:date"></xsl:value-of>
                                           </td>
									       <td>
									           <xsl:if test="./docbook:author">
									               <xsl:call-template name="author-name-email"/>
									           </xsl:if>
									       </td>
                                           <td>
                                                <xsl:apply-templates select="./docbook:revdescription" mode="content"/>
                                           </td>
									   </tr>
									    
									</xsl:for-each>
								</tbody>
			                </table>
			            </div>
			        </div>  
		        </xsl:if>  
            </div>
            <!-- EOF tab container -->
            
        </div>
        <!-- EOF Spec info -->
	 
	 
    </xsl:template>
	
	<!-- 
	Template Outputing the author nama with email link from an <author> element
	 <author>
            <personname>Richard Leys</personname>
            <email>richard.leys@ziti-uni-heidelberg.de</email>
     </author>
     
     becomes
     
     <a href="mailto:richard.leys@ziti-uni-heidelberg.de">Richard Leys</a>
     
     with extended=true param
     
     <a href="mailto:richard.leys@ziti-uni-heidelberg.de">Richard Leys</a> <richard.leys@ziti-uni-heidelberg.de>
	
	-->
	<xsl:template name="author-name-email" match="docbook:author">
	   <xsl:param name="extended" required="no" select="false()"></xsl:param>
	   
	   <div> 
        <xsl:choose>
            <!-- ##### With email -->
            <xsl:when test="./docbook:email">
                <a>
                  <xsl:attribute name="href">mailto:<xsl:value-of select="./docbook:email/text()"/></xsl:attribute>
                  <!-- ## Personname !-->
                  <xsl:value-of select="./docbook:personname/text()"></xsl:value-of>
                </a>
                
                <!-- ## Extended ? -->
                <xsl:if test="$extended">
                    &lt;
                    <xsl:value-of select="./docbook:email/text()"></xsl:value-of>
                    &gt;
                </xsl:if>
            </xsl:when>
            
            <!-- ##### Without email -->
            <xsl:otherwise>
                <!-- ## Personname !-->
                  <xsl:value-of select="./docbook:personname/text()"></xsl:value-of>
            </xsl:otherwise>
        </xsl:choose> 
	   </div> 
	</xsl:template>
	
	
	<!-- ## Include subsheets for various content handling splitting -->
	<!-- #################################################### -->
	<!-- Include Section content processing -->
	<xsl:include href="spec-section.xsl"/>
	

    <!-- Ignores -->
    <xsl:template match="*" mode="info">
    </xsl:template>
    <xsl:template match="docbook:info" mode="content">
    </xsl:template>
    <xsl:template match="docbook:revhistory" mode="#all">
    </xsl:template>

	
	
</xsl:stylesheet>