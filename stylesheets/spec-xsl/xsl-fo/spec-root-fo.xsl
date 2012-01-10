<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 
    <!-- XML Output -->
    <xsl:output
        method="xml"
        version="1.0"
        encoding="UTF-8"
        indent="yes"
        />
        
       
    <!-- 
    	
    	Parameters to control output
    	
     -->
    
    <!-- #### Color -->
    
    <!-- Definitions -->
    <xsl:param name="colors-darkblue">#084B8A</xsl:param>
    <xsl:param name="colors-lightblue">#E0E6F8</xsl:param>
    <xsl:param name="colors-lightgray">#CCCCCC</xsl:param>
    
    <!-- Element specific -->
    <xsl:param name="colors-title"><xsl:value-of select="$colors-darkblue"/></xsl:param>
    
    <xsl:param name="colors-table-header-cell-background"><xsl:value-of select="$colors-darkblue"/></xsl:param>
    <xsl:param name="colors-table-header-cell-text">white</xsl:param>
    
    
    <!-- #### Text -->
    <xsl:param name="document-text-font-size-default">10.5pt</xsl:param>
    <xsl:param name="document-text-font-family-default">sans-serif</xsl:param>
    
    <!-- #### Block spacing -->
    <xsl:param name="block-margin-above">0px</xsl:param>
    <xsl:param name="block-margin-under">5px</xsl:param>
    <xsl:attribute-set name="block-common">
    	<xsl:attribute name="margin-top"><xsl:value-of select="$block-margin-above"/></xsl:attribute>
    	<xsl:attribute name="margin-bottom"><xsl:value-of select="$block-margin-under"/></xsl:attribute>
    </xsl:attribute-set>
    
    <!-- #### Formatting -->
    <xsl:attribute-set name="format-emphasis">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- #### Cover page  -->
    <xsl:attribute-set name="cover-page-title">
    	<xsl:attribute name="font-size">22pt</xsl:attribute>
    	<xsl:attribute name="font-weight">bold</xsl:attribute>
    	<xsl:attribute name="color"><xsl:value-of select="$colors-title"/></xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="cover-page-subtitle">
    	<xsl:attribute name="font-size">14pt</xsl:attribute>
    	<xsl:attribute name="font-weight">bold</xsl:attribute>
    	<xsl:attribute name="color">black</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="cover-page-block-margins">
    	<xsl:attribute name="margin-top">10px</xsl:attribute>
    	<xsl:attribute name="margin-bottom">10px</xsl:attribute>
    </xsl:attribute-set>
    
    
   	<!-- #### Titles  -->
    
    <!-- Padding before/after the title of a new section (padding above) -->
    <xsl:param name="section-title-style-level-padding-above">5px</xsl:param>
    <xsl:param name="section-title-style-level-padding-under">5px</xsl:param>
    
    <!-- Indent before the title of a new Section (line indenting): not unit because multiplied -->
    <xsl:param name="section-title-style-level-indent">5</xsl:param>
    
    <!-- Levels specific styling -->
    <xsl:attribute-set name="section-title-style-level-common">
    	<xsl:attribute name="color"><xsl:value-of select="$colors-title"/></xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section-title-style-level-1" use-attribute-sets="section-title-style-level-common">
    	<xsl:attribute name="font-size">18pt</xsl:attribute>
    	<xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section-title-style-level-2" use-attribute-sets="section-title-style-level-common">
    	<xsl:attribute name="font-size">14pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section-title-style-level-3" use-attribute-sets="section-title-style-level-common">
    	<xsl:attribute name="font-size">12pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section-title-style-level-4" use-attribute-sets="section-title-style-level-common">
    	<xsl:attribute name="font-size">10.5pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section-title-style-level-5" use-attribute-sets="section-title-style-level-common">
    	<xsl:attribute name="font-size">10.5pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section-title-style-level-6" use-attribute-sets="section-title-style-level-common">
    	<xsl:attribute name="font-size">10.5pt</xsl:attribute>
    </xsl:attribute-set>
    
    
    <!-- #### Table -->
    <xsl:param name="table-margin-top-bottom">5mm</xsl:param>
    <xsl:attribute-set name="table-margin-attributes">
    	<xsl:attribute name="margin-top"><xsl:value-of select="$table-margin-top-bottom"></xsl:value-of></xsl:attribute>
    	<xsl:attribute name="margin-bottom"><xsl:value-of select="$table-margin-top-bottom"></xsl:value-of></xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="table-border-style">
    	<xsl:attribute name="border">1px black solid</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="table-cell-style-common">
    	<xsl:attribute name="border">1px black solid</xsl:attribute>
    	<xsl:attribute name="padding">3px</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="table-cell-style-default">
    	<xsl:attribute name="padding">3px</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="table-cell-style-header">
    	<xsl:attribute name="background-color"> <xsl:value-of select="$colors-table-header-cell-background"/> </xsl:attribute>
    	<xsl:attribute name="color"><xsl:value-of select="$colors-table-header-cell-text"/></xsl:attribute>
    	<xsl:attribute name="text-align">center</xsl:attribute>
    	<xsl:attribute name="vertical-align">middle</xsl:attribute>
    </xsl:attribute-set>
    

    <!-- Apply classes -->
    <xsl:template name="classes">
        <xsl:param name="class" required="yes"></xsl:param>
    
        <!-- ##### Formatting -->
        <xsl:choose>
            <xsl:when test="$class='spec-format-bg-lightblue'">
                <xsl:attribute name="background-color" select="$colors-lightblue"></xsl:attribute>
            </xsl:when>
            <xsl:when test="$class = spec-format-bold">
                <xsl:attribute name="font-weight">bold</xsl:attribute>
            </xsl:when>
        </xsl:choose>
    
        <!-- ##### Table -->
         <xsl:choose>
            <xsl:when test="$class='spec-table-alternate1'">
                <xsl:attribute name="background-color" select="$colors-lightgray"></xsl:attribute>
                <xsl:attribute name="text-align">center</xsl:attribute>
            </xsl:when>
         </xsl:choose>
    
    </xsl:template>
    
    
    
	<xsl:template match="//docbook:article">

		<!-- Document Root -->
        <fo:root>
            
            <!-- #### Styling -->
        
        	<!-- Font -->
        	<xsl:attribute name="font-size" select="$document-text-font-size-default"></xsl:attribute>
        	<xsl:attribute name="font-family" select="$document-text-font-family-default"></xsl:attribute>
        
        	<!-- #### Page masters -->
        
        	<!-- A4 Page master -->
            <fo:layout-master-set>
            
               <fo:simple-page-master 
               		master-name="A4" 
               		page-width="210mm"
                	page-height="297mm" 
                	margin-top="1cm" 
                	margin-bottom="1cm"
                	margin-left="1cm" 
                	margin-right="1cm">
                	
                	<fo:region-body margin="2cm 1cm 2cm 1cm"/>
                	
                	<!-- Above / Under -->
                	<fo:region-before extent="2cm"/>
                  	<fo:region-after extent="2cm"></fo:region-after>
                  
                  	<!-- Left / Right -->
                  	<fo:region-start extent="1cm"/>
                  	<fo:region-end extent="1cm">
     
                  </fo:region-end>
                </fo:simple-page-master>
            </fo:layout-master-set>
            
            <!-- ## Pre-document things (bookmarks etc...) -->
            <!-- ########################### -->   

            <!-- #### Build a toc bookmarks -->
            <xsl:call-template name="toc-bookmark"></xsl:call-template>
           
     		<fo:page-sequence master-reference="A4">
     			<fo:flow flow-name="xsl-region-body">
     			
		            <!-- ## Cover Page -->
		            <!-- ########################### -->   
		            
	                <!-- #### Title of the spec -->
                    <fo:block xsl:use-attribute-sets="cover-page-title">
	                	<xsl:value-of select="./docbook:info/docbook:title/text()"/>
                    </fo:block>
                    
                    <!-- #### Generation info -->
	               	<fo:block>
	                	Generated on <xsl:value-of select="fn:current-dateTime()"></xsl:value-of>
                    </fo:block>
		             
					<!-- ## Info box: Author, Revison history etc... -->
		            <!-- ###################### -->
					<xsl:apply-templates mode="info"></xsl:apply-templates>
					
					<!-- ## Build a TOC? -->
					<!-- ########################## -->
		            
		            
		            <!-- #### Build a TOC Page? -->
		            <xsl:if test="./docbook:toc">
				      <xsl:call-template name="toc"></xsl:call-template>
		            </xsl:if>
				</fo:flow>
			</fo:page-sequence>
			
			<!-- ## Normal Content -->
			<!-- ########################## -->
			<fo:page-sequence master-reference="A4">
			
				<!-- #### Header -->
				<!-- #### Footer -->
				<fo:static-content flow-name="xsl-region-after">
   					<fo:block text-align="right">
    					Page <fo:page-number/> of <fo:page-number-citation-last ref-id="last-page"/>
   					</fo:block>
   				</fo:static-content>
			
				<!-- ### Content in body -->
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates mode="content"></xsl:apply-templates>
					
					<!-- Last page detector -->
					<fo:block id="last-page"/>
					
				</fo:flow>

			</fo:page-sequence>
					
        </fo:root>		
			
		


	</xsl:template>
	
	<!-- Generate a toc as Bookmarks -->
    <xsl:template name="toc-bookmark">
       
           <!-- ## Go through Sections to build outline -->
           <!-- ######################################## -->
           <fo:bookmark-tree>
           		
           		<!-- Table of contents -->
           		<fo:bookmark>
            		<xsl:attribute name="internal-destination">table-of-contents</xsl:attribute>
		            <fo:bookmark-title>Table of Contents</fo:bookmark-title>
        		</fo:bookmark>
        
				<!-- Section Bookmarks -->
				<xsl:for-each select="docbook:section"  >
	            	<xsl:call-template name="toc-bookmark-section">
	            	</xsl:call-template>
				</xsl:for-each>
				
           </fo:bookmark-tree>
    </xsl:template>
    
    <xsl:template name="toc-bookmark-section" match="docbook:section" mode="bookmark">
        
        <!-- ## Prepare Datas -->
        <!-- ####################################### -->
        <xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
        <xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
        
        <fo:bookmark>
            <xsl:attribute name="internal-destination">ref-toc-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute>
            <fo:bookmark-title>
                <xsl:value-of select="$toc-hier-pos"/>
                <xsl:text>. </xsl:text>
                <xsl:value-of select="./docbook:title/text()"></xsl:value-of>
            </fo:bookmark-title>
            
            <!-- Call recursively -->
            <xsl:for-each select="docbook:section"  >
                 <xsl:call-template name="toc-bookmark-section">
                 </xsl:call-template>
             </xsl:for-each>

        </fo:bookmark>
    </xsl:template>
    

    <xsl:template name="toc" match="docbook:toc" mode="toc">
       
           	<!-- ## Go through Sections to build outline -->
           	<!-- ######################################## -->
		    <fo:block 
		    	xsl:use-attribute-sets="section-title-style-level-1 block-common"
		    	page-break-before="always"
		    	id="table-of-contents">
		        Table of contents
		    </fo:block>
            
       		<xsl:for-each select="docbook:section"  >
      			<xsl:call-template name="toc-section"/>
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
		<fo:block xsl:use-attribute-sets="block-common">
			
			<!-- Title indent if not a level 1 title -->
			<xsl:if test="$toc-hier-level > 1">
				<xsl:attribute name="start-indent"><xsl:value-of select="$section-title-style-level-indent*($toc-hier-level - 1)"/>mm</xsl:attribute>
			</xsl:if>
			
			<!-- ## Anchor link to section -->
			<fo:basic-link>			
			
				<!-- Reference -->
				<xsl:attribute name="internal-destination">ref-toc-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute>
				
				<!-- Text -->
				<fo:inline>
					<xsl:value-of select="$toc-hier-pos"/>
		      		<xsl:text>. </xsl:text>
		      		<xsl:value-of select="./docbook:title/text()"></xsl:value-of>
				</fo:inline>
				
			</fo:basic-link>
		</fo:block>
		
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
	 
		<!--#### Author and global infos tab --> 
	 	<!--#### Author -->
	 	<!-- # Support: personname and email -->
	 	<fo:block xsl:use-attribute-sets="cover-page-block-margins">
		 	<fo:block xsl:use-attribute-sets="cover-page-subtitle block-common" >Author(s):</fo:block>
		 	<fo:block xsl:use-attribute-sets="block-common">
				<xsl:for-each select="docbook:author">
					<xsl:call-template name="author-name-email">
		        		<xsl:with-param name="extended">true</xsl:with-param>
		            </xsl:call-template>
				</xsl:for-each>
			</fo:block>
		</fo:block>
		
		<!-- #### Abstract -->
       	<xsl:if test="./docbook:abstract">
       		<fo:block xsl:use-attribute-sets="cover-page-block-margins">
       			<fo:block xsl:use-attribute-sets="cover-page-subtitle block-common">Abstract:</fo:block>
       			<fo:block xsl:use-attribute-sets="block-common">
       				<xsl:apply-templates select="./docbook:abstract" mode="content"/>
       			</fo:block>
       		</fo:block>
		</xsl:if>
		
		<!--#### Revision History -->
        <xsl:if test="docbook:revhistory">
        	<fo:block xsl:use-attribute-sets="cover-page-block-margins">
	        	<fo:block xsl:use-attribute-sets="cover-page-subtitle">Revision History</fo:block>
	        	<fo:block xsl:use-attribute-sets="block-common">
	        		<fo:table xsl:use-attribute-sets="table-margin-attributes table-border-style">
	        		
	        			<!-- #### Styling -->
	        			<xsl:attribute name="table-layout">fixed</xsl:attribute>
						<xsl:attribute name="width">100%</xsl:attribute>
						<xsl:attribute name="border-collapse">collapse</xsl:attribute>
						
	        			<fo:table-header>
	        				<fo:table-row>
	        					<fo:table-cell 
	        						xsl:use-attribute-sets="table-cell-style-common table-cell-style-header">
	     							<fo:block>Date</fo:block>
	     						</fo:table-cell>
	     						<fo:table-cell 
	        						xsl:use-attribute-sets="table-cell-style-common table-cell-style-header">
	     							<fo:block>Author</fo:block>
	     						</fo:table-cell>
	     						<fo:table-cell 
	        						xsl:use-attribute-sets="table-cell-style-common table-cell-style-header">
	     							<fo:block>Description</fo:block>
	     						</fo:table-cell>
	        				</fo:table-row>
	        			</fo:table-header>
	        			<fo:table-body>
	        				<xsl:for-each select="./docbook:revhistory/docbook:revision">
	                                      
		                        <!-- Try to sort -->
		                        <xsl:sort select="./docbook:date" order="descending"></xsl:sort>
		                                    
		                		<!-- Revison Line -->
							   	<fo:table-row>
							   		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
							   			<fo:block><xsl:value-of select="./docbook:date"/></fo:block>
							   		</fo:table-cell>
							   		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
							   			<fo:block>
							   			<xsl:if test="./docbook:author">
							               <xsl:call-template name="author-name-email"/>
							           	</xsl:if>
							           	</fo:block>
							   		</fo:table-cell>
							   		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
							   			<xsl:apply-templates select="./docbook:revdescription" mode="content"/>
							   		</fo:table-cell>
							   	</fo:table-row>
							</xsl:for-each>
	        			</fo:table-body>
	        		</fo:table>
	        	</fo:block>
        	</fo:block>
        </xsl:if>
	 	<!-- EOF Revhistory -->
	 
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
	   
	   <fo:block>
       <xsl:choose>
       
            <!-- ##### With email -->
            <xsl:when test="./docbook:email">
                <fo:basic-link>
                  <xsl:attribute name="external-destination">mailto:<xsl:value-of select="./docbook:email/text()"/></xsl:attribute>
                  <!-- ## Personname !-->
                  <fo:inline>
                  	<xsl:value-of select="./docbook:personname/text()"></xsl:value-of>
                  </fo:inline>
                </fo:basic-link>
                
                <!-- ## Extended ? -->
                <xsl:if test="$extended">
                    &lt;<xsl:value-of select="./docbook:email/text()"></xsl:value-of>&gt;
                </xsl:if>
            </xsl:when>
            
            <!-- ##### Without email -->
            <xsl:otherwise>
                <!-- ## Personname !-->
                  <xsl:value-of select="./docbook:personname/text()"></xsl:value-of>
            </xsl:otherwise>
        </xsl:choose> 
	   </fo:block>
	</xsl:template>
	
	
	<!-- ## Include subsheets for various content handling splitting -->
	<!-- #################################################### -->
	<!-- Include Section content processing -->
	<xsl:include href="spec-section-fo.xsl"/>
	

    <!-- Ignores -->
    <xsl:template match="*" mode="info">
    </xsl:template>
    <xsl:template match="docbook:info" mode="content">
    </xsl:template>
    <xsl:template match="docbook:revhistory" mode="#all">
    </xsl:template>

	
	
</xsl:stylesheet>