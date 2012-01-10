<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:docbook="http://docbook.org/ns/docbook"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
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
    
    <!-- Handle title content -->
    <xsl:template name="title-content">
    	
    	<!-- ## Prepare Datas -->
        <!-- ####################################### -->
        <xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
        <xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
    	
    	<!-- ID -->
        <xsl:attribute name="id">ref-toc-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute>
        
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
    
    
    
    <!-- 
        Figure Handling. only support image at the moment
     -->
     <xsl:template match="docbook:figure" mode="content">
        
     </xsl:template>
     
     
    <!--
     	######################
     	Table handling
     	######################
     -->
     <xsl:template match="docbook:table" mode="content">
     
     	
     		<!-- Caption? -->
			<!-- ######## -->
<!-- 			<xsl:if test="./docbook:caption"> -->
<!-- 				<fo:table-caption> -->
<!-- 					<fo:block> -->
<!-- 						<xsl:value-of select="./docbook:caption/text()"></xsl:value-of> -->
<!-- 					</fo:block> -->
<!-- 				</fo:table-caption> -->
<!-- 			</xsl:if> -->
     	
     		<!-- Table -->
     		<!-- ##### -->
			<fo:table xsl:use-attribute-sets="table-margin-attributes table-border-style">

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
				
				<!-- #### Body -->
				<xsl:if test="./docbook:tbody">
					<fo:table-body>
						<xsl:call-template name="table-rowcell">
							<xsl:with-param name="base" select="./docbook:tbody"></xsl:with-param>
						</xsl:call-template>
					</fo:table-body>
				</xsl:if>
				
				<!-- #### Footer -->
				<xsl:if test="docbook:tfoot">
					<fo:table-footer>
						<xsl:call-template name="table-rowcell">
							<xsl:with-param name="base" select="docbook:tfoot"></xsl:with-param>
						</xsl:call-template>
					</fo:table-footer>
				</xsl:if>
			
			</fo:table>

			

        
        
     </xsl:template>
     
     <!-- This template to be called converts tr/td to row/cell -->
     <xsl:template name="table-rowcell">
     	
     	<xsl:param name="base"></xsl:param>

     	
     	<!-- Rows -->
     	<xsl:for-each select="$base/docbook:tr">
     		<fo:table-row>
     			<!-- Cells -->
     			<xsl:for-each select="./docbook:td">
     				
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
    <xsl:template name="table-cell-content" match="docbook:td" mode="content">
    	
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
	     		<fo:block>
	     			<!-- UTF8 bullet character -->
	       			&#8226;
	     		</fo:block>
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
	
     
    <!-- ## IGNORE: title -->
    <xsl:template match="docbook:title" mode="content">
      
    </xsl:template>
    
    <!--
        ######################
        Common formatting
        ######################
    --> 
    
    
    <!-- Transfers class/style attributes to set attribute on called element -->
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
    
    <!-- Handle a paragraph -->
    <xsl:template match="docbook:section/docbook:para" mode="content">
       
       <!--  xsl:use-attribute-sets="block-common" -->
       <fo:block xsl:use-attribute-sets="block-common">
       		<xsl:apply-templates  mode="content"></xsl:apply-templates>
       </fo:block>
       
    </xsl:template>
    
    <xsl:template match="docbook:para" mode="content">
       
       <!--  xsl:use-attribute-sets="block-common" -->
       <fo:block xsl:use-attribute-sets="block-common">
       		<xsl:apply-templates  mode="content"></xsl:apply-templates>
       </fo:block>
       
    </xsl:template>
    
</xsl:stylesheet>