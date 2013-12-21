<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:docbook="http://docbook.org/ns/docbook"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
    
    <!-- 
        ######################
        Sectioning
        ######################
     -->
    
    <!-- Template to display sections as real content -->
    <xsl:template match="docbook:section" mode="content">
    
        <!-- ## Prepare Datas -->
        <!-- ####################################### -->
        <xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
        <xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
        
        
        
        
		<!-- ## Title -->
		<!-- ############## -->
		<xsl:choose>
			<xsl:when test="$toc-hier-level = 1">
				<fo:block xsl:use-attribute-sets="section-title-style-level-1">
					<xsl:call-template name="title-content"></xsl:call-template>
				</fo:block>
			</xsl:when>
			<xsl:when test="$toc-hier-level = 2">
				<fo:block xsl:use-attribute-sets="section-title-style-level-2">
					<xsl:call-template name="title-content"></xsl:call-template>
				</fo:block>
			</xsl:when>
			<xsl:when test="$toc-hier-level = 3">
				<fo:block xsl:use-attribute-sets="section-title-style-level-3">
					<xsl:call-template name="title-content"></xsl:call-template>
				</fo:block>
			</xsl:when>
			<xsl:when test="$toc-hier-level = 4">
				<fo:block xsl:use-attribute-sets="section-title-style-level-4">
					<xsl:call-template name="title-content"></xsl:call-template>
				</fo:block>
			</xsl:when>
			<xsl:when test="$toc-hier-level = 5">
				<fo:block xsl:use-attribute-sets="section-title-style-level-5">
					<xsl:call-template name="title-content"></xsl:call-template>
				</fo:block>
			</xsl:when>
			<xsl:when test="$toc-hier-level = 6">
				<fo:block xsl:use-attribute-sets="section-title-style-level-6">
					<xsl:call-template name="title-content"></xsl:call-template>
				</fo:block>
			</xsl:when>
		</xsl:choose>
        
        <!-- ## Apply more templates -->
      	<!-- ####################################### -->
      	<xsl:apply-templates mode="content" ></xsl:apply-templates>
       
            
          
    </xsl:template>
    
    <!-- #### Handle title content -->
    <xsl:template name="title-content">
    	
    	<xsl:call-template name="preserve-attributes"></xsl:call-template>
    	
    	<!-- ## Prepare Datas -->
        <!-- ####################################### -->
        <xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
        <xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
    	
    	<!-- ID (either already set, or added)-->
<!--     	<xsl:variable name="title-id" select="@id"></xsl:variable> -->
    	<xsl:if test="not(@id|@xml:id)">
    	   <xsl:attribute name="id">ref-toc-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute>
    	</xsl:if>
<!--         <xsl:attribute name="id">ref-toc-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute> -->
        
        <!-- #### Styling -->
        
        <!-- Start all top level sections on a new page for a beginning -->
        <xsl:if test="$toc-hier-level = 1">
        		<xsl:attribute name="page-break-before">always</xsl:attribute>
        </xsl:if>
        
        <!-- Paddings and such -->
        <xsl:attribute name="padding-before"><xsl:value-of select="$section-title-style-level-padding-above"/></xsl:attribute>
        <xsl:attribute name="padding-after"><xsl:value-of select="$section-title-style-level-padding-under"/></xsl:attribute>
        
        <!-- Title indent if not a level 1 title -->
        <xsl:if test="$toc-hier-level > 1">
        		<xsl:attribute name="start-indent"><xsl:value-of select="$section-title-style-level-indent*($toc-hier-level - 1)"/>mm</xsl:attribute>
        </xsl:if>
        
        <!-- Title output -->
        <xsl:value-of select="$toc-hier-pos"/>
        <xsl:text>. </xsl:text>
        <xsl:value-of select="./docbook:title/text()"></xsl:value-of>
    
    
    </xsl:template>
    
    <xsl:template match="docbook:bridgehead">
       <fo:block xsl:use-attribute-sets="format-emphasis">
       <xsl:value-of select="text()"></xsl:value-of>
       </fo:block> 
    
    </xsl:template>
    
    <!-- 
        ######################
        Figure Handling. only support image at the moment
        ######################
     -->
     <xsl:template match="docbook:figure" mode="content">
        <fo:block margin-top="2em" margin-bottom="2em" text-align="center">
        	
        	<fo:block>
	        <!-- The media Object -->
			<xsl:if test="./docbook:mediaobject/docbook:imageobject/docbook:imagedata">
				<xsl:variable name="imageData"
					select="./docbook:mediaobject/docbook:imageobject/docbook:imagedata"></xsl:variable>
				
				<fo:external-graphic  content-width="50em" content-height="auto" scaling="uniform">
		        	<xsl:attribute name="src"><xsl:value-of select="$imageData/@fileref"/></xsl:attribute>               
                </fo:external-graphic>
	
			</xsl:if>
	
			</fo:block>
			
			
			<!-- Propagate Caption if there is one -->
			<xsl:if test="./docbook:caption">
				<fo:block xsl:use-attribute-sets="caption">			
					<xsl:value-of select="./docbook:caption/text()" />
				</fo:block>
			</xsl:if>
		  
		</fo:block>
     </xsl:template>
     
  
     
    <!--
     	######################
     	Table handling
     	######################
     -->
     <xsl:template match="docbook:table" mode="content">
            
     	<fo:table-and-caption>

     		<!-- Caption? -->
			<!-- ######## -->
			<xsl:if test="./docbook:caption">
				<fo:table-caption>
 					<fo:block> 
 						<xsl:value-of select="./docbook:caption/text()"></xsl:value-of> 
 					</fo:block> 
 				</fo:table-caption>
 			</xsl:if> 
     	
     		<!-- Table -->
     		<!-- ##### -->
			<fo:table xsl:use-attribute-sets="table-margin-attributes table-border-style">

                <!-- #### Caption -->
                <!-- <xsl:if test="./docbook:caption">
                    <fo:table-caption>
                      <fo:block>
                        <xsl:value-of select="./docbook:caption/text()"></xsl:value-of> 
                        </fo:block> 
                    </fo:table-caption> 
                </xsl:if>
                -->


                    <xsl:call-template name="preserve-attributes"></xsl:call-template>

    				<!-- #### Styling -->
    				
    				<!-- Layout -->
    				<xsl:attribute name="table-layout">fixed</xsl:attribute>
    				<xsl:attribute name="width">100%</xsl:attribute>
    		
    				
    				<!-- Border -->
    				<xsl:attribute name="border-collapse">collapse</xsl:attribute>
    				
    				<!-- Spacing (att set) -->
    			
                    

    				<!-- #### Header -->
    				<xsl:if test="./docbook:thead">
    					<fo:table-header>
    						<xsl:call-template name="table-rowcell">
    							<xsl:with-param name="base" select="./docbook:thead"></xsl:with-param>
    						</xsl:call-template>
    					</fo:table-header>
    				</xsl:if>
    				
    				<!-- #### Footer -->
                    <xsl:if test="docbook:tfoot">
                        <fo:table-footer>
                            <xsl:call-template name="table-rowcell">
                                <xsl:with-param name="base" select="docbook:tfoot"></xsl:with-param>
                            </xsl:call-template>
                        </fo:table-footer>
                    </xsl:if>
    				
    				<!-- #### Body -->
    				<xsl:if test="./docbook:tbody">
    					<fo:table-body>
    						<xsl:call-template name="table-rowcell">
    							<xsl:with-param name="base" select="./docbook:tbody"></xsl:with-param>
    						</xsl:call-template>
    					</fo:table-body>
    				</xsl:if>
				
				</fo:table>
			     
                <!--<xsl:if test="./docbook:caption">
                      <fo:block>
                        <xsl:value-of select="./docbook:caption/text()"></xsl:value-of> 
                      </fo:block> 
                </xsl:if>-->
	

		</fo:table-and-caption>	

        
        
     </xsl:template>
     
     <!-- This template to be called converts tr/td to row/cell -->
     <xsl:template name="table-rowcell">
     	
     	<xsl:param name="base"></xsl:param>

     	
     	<!-- Rows -->
     	<xsl:for-each select="$base/docbook:tr">
     		<fo:table-row>
     			<!-- Cells -->
     			<xsl:for-each select="(./docbook:td|./docbook:th)">
     				
     				<xsl:choose>
     					<xsl:when test="local-name($base)='thead'">
     						<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-header">
     							<xsl:call-template name="style"></xsl:call-template>
     							<xsl:apply-templates select="." mode="content"></xsl:apply-templates>
     						</fo:table-cell>
     					</xsl:when>
     					<xsl:otherwise>
     						<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
     							
     							<xsl:apply-templates select="." mode="content"></xsl:apply-templates>
     						</fo:table-cell>
     					</xsl:otherwise>
     				</xsl:choose>
     				
     			</xsl:for-each>
     			
     		</fo:table-row>
     		
     	</xsl:for-each>
     	
     	
     </xsl:template>
     
    <!-- table cell  -->
    <xsl:template name="table-cell-content" match="docbook:td|docbook:th" mode="content">
    	
    	<!-- Col span -->
		<xsl:if test="@colspan">
			<xsl:attribute name="number-columns-spanned "><xsl:value-of select="@colspan"></xsl:value-of></xsl:attribute>
		</xsl:if>
		
		<!--Style -->
		<xsl:call-template name="style" ></xsl:call-template>
		
		<!-- Content -->
		<fo:block>
		  <xsl:apply-templates mode="content"></xsl:apply-templates>
<!-- 		  <xsl:value-of select="./text()"></xsl:value-of> -->
		</fo:block>
    	
    </xsl:template>
     
	<!--
     	######################
     	List handling
     	######################
	-->
	<xsl:template match="docbook:itemizedlist" mode="content">
			<fo:list-block  
				xsl:use-attribute-sets="block-common"
				provisional-label-separation="15mm" 
				provisional-distance-between-starts="5mm">
    			<xsl:apply-templates mode="content"/>
  			</fo:list-block>

	</xsl:template>
	<xsl:template match="docbook:orderedlist" mode="content">
		<fo:list-block 
			xsl:use-attribute-sets="block-common"
			provisional-label-separation="15mm" 
			provisional-distance-between-starts="5mm">
    		<xsl:apply-templates mode="content"/>
  		</fo:list-block>
	</xsl:template>
	
	
    
    <!-- List item --> 
    <xsl:template match="docbook:itemizedlist/docbook:listitem" mode="content">
		<fo:list-item>
	   		<fo:list-item-label>
	   		    <!-- UTF8 bullet character -->
	     		<fo:block>&#8226;</fo:block>
	   		</fo:list-item-label>
	   		<fo:list-item-body start-indent="body-start()">
	      		<fo:block>
	        		<xsl:apply-templates mode="content"/>
	      		</fo:block>
    		</fo:list-item-body>
	  </fo:list-item>
	</xsl:template>
	
	<xsl:template match="docbook:orderedlist/docbook:listitem" mode="content">
		<fo:list-item>
	   		<fo:list-item-label>
	     		<fo:block>
	     			<xsl:number format="1."/>   
	     		</fo:block>
	   		</fo:list-item-label>
	   		<fo:list-item-body start-indent="body-start()">
	      		<fo:block>
	        		<xsl:apply-templates mode="content"/>
	      		</fo:block>
    		</fo:list-item-body>
	  </fo:list-item>
	</xsl:template>
	
	<xsl:template match="docbook:listitem/docbook:para" mode="content">
       
       <!--  xsl:use-attribute-sets="block-common" -->
       <fo:block xsl:use-attribute-sets="block-common">
            <xsl:apply-templates  mode="content"></xsl:apply-templates>
       </fo:block>
       
       
       
    </xsl:template>
	
     
    <!-- ## IGNORE: title -->
    <xsl:template match="docbook:title" mode="content">
      
    </xsl:template>
    
    
    <!-- 
        ######################
        Link And XREF
        ######################
     -->
    <xsl:template match="docbook:link" mode="content">
    
        <fo:basic-link internal-destination="{@linkend}" xsl:use-attribute-sets="link-common">
            <xsl:apply-templates mode="content"></xsl:apply-templates>
        </fo:basic-link>
    
    </xsl:template> 
    
    <xsl:template match="docbook:xref" mode="content">
        
        <xsl:variable name="le" select="@linkend"></xsl:variable>
        
        <fo:basic-link internal-destination="{@linkend}" xsl:use-attribute-sets="link-common">
	        <!-- Get xref Target title -->
	        <!-- ##### -->
	       
	        <xsl:variable name="target" select="fn:id(@linkend)"></xsl:variable>
<!--             <xsl:variable name="target" select="id(@linkend)"></xsl:variable>  -->
	        <xsl:choose>
	           <!--  Section: title/text or @title -->
	           <xsl:when test="fn:local-name($target)='section' and $target/docbook:title/text()">
	               <xsl:value-of select="$target/docbook:title/text()"></xsl:value-of>
	           </xsl:when>
	           <xsl:when test="fn:local-name($target)='section' and $target/@title">
                   <xsl:value-of select="$target/@title"></xsl:value-of>
               </xsl:when>
	           
	           <!-- Fallback -->
	           <xsl:otherwise>
	            Could not determine text-output for link <xsl:value-of select="$target"></xsl:value-of>
	           </xsl:otherwise>
	        </xsl:choose>
	        <!-- Get Target -->
       
        </fo:basic-link>
    
    </xsl:template> 
     
   
    <!--
        ######################
        Common formatting
        ######################
    --> 
    
    <!-- #### Apply classes -->
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
    
    
    <!-- #### Transfers class/style attributes to set attribute on called element -->
    <xsl:template name="style">
        <xsl:if test="@style">
            <xsl:attribute name="style" select="@style"></xsl:attribute>
        </xsl:if>
        <xsl:if test="@class">
            <!-- to expand class attributes, call a special template -->
            <xsl:variable name="callclass">
                <xsl:value-of select="@class"></xsl:value-of>
            </xsl:variable>
            <xsl:call-template name="classes">
                <xsl:with-param name="class" select="$callclass"></xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    
    <xsl:template match="docbook:emphasis" mode="content">
        <fo:inline xsl:use-attribute-sets="format-emphasis">
            <xsl:apply-templates mode="content"></xsl:apply-templates>
        </fo:inline>
    </xsl:template>
    
    <!-- #### Handle a paragraph -->
    <xsl:template match="docbook:section/docbook:para" mode="content">
       
       <!--  xsl:use-attribute-sets="block-common" -->
       <!--  white-space-collapse="false" white-space-treatment="ignore-if-before-linefeed" linefeed-treatment="preserve" -->
       <fo:block xsl:use-attribute-sets="block-common" >
       		<xsl:apply-templates  mode="content"></xsl:apply-templates>
       </fo:block>
       
       
       
    </xsl:template>
    
  
    
    
    <xsl:template match="docbook:para" mode="content">
       
       <!--  xsl:use-attribute-sets="block-common" -->
<!--        <fo:block xsl:use-attribute-sets="block-common" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="ignore-if-before-linefeed"> -->
<!--        		<xsl:apply-templates  mode="content"></xsl:apply-templates> -->
<!--        </fo:block> -->

           <fo:block xsl:use-attribute-sets="block-common">
               <xsl:apply-templates  mode="content"></xsl:apply-templates>
           </fo:block>

       
    </xsl:template>
    
    
    <!-- ###################
         Code formating 
          #######################
    -->
    
    <!-- For code, create a box with blue background -->
    <xsl:template match="docbook:code" mode="content">
        
        <fo:block xsl:use-attribute-sets="style-info-box" relative-position="relative">
            
            <xsl:apply-templates mode="content"></xsl:apply-templates>
            
        </fo:block>
    </xsl:template>
    
    
     <!-- ## Propagate all id attributes -->
    <xsl:template match="@id|@xml:id" mode="content"> 
        
        <xsl:copy><xsl:apply-templates mode="content"></xsl:apply-templates></xsl:copy>
<!--         <xsl:copy-of select="."></xsl:copy-of> -->
    
<!--        <xsl:copy select="."></xsl:copy> -->
    </xsl:template>
    
</xsl:stylesheet>
