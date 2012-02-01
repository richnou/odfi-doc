<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:docbook="http://docbook.org/ns/docbook"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:cag="cag:extra"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
    <!-- Template to display sections as real content -->
    <xsl:template match="cag:plan" mode="content">
    
    	<fo:table xsl:use-attribute-sets="table-margin-attributes table-border-style">
    	
    		<fo:table-header>
    			<fo:table-row>
					<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-header"><fo:block>Item</fo:block></fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-header"><fo:block>Description</fo:block></fo:table-cell>
					<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-header"><fo:block>Duration</fo:block></fo:table-cell>
				</fo:table-row>
			</fo:table-header>
    	
    		<fo:table-body>
				<!--  ## Apply more templates -->
				<!--  ####################################### -->
	      		<xsl:apply-templates mode="extra-plan" ></xsl:apply-templates>
    	
    		</fo:table-body>
    
		</fo:table>
       
       
            
          
    </xsl:template>
    
    <xsl:template match="cag:workpackage" mode="extra-plan">
    
    	<fo:table-row>
    		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default" number-columns-spanned="2">
    			<fo:block>
    				<xsl:value-of select="./cag:title/text()"></xsl:value-of>
    			</fo:block>
    		</fo:table-cell>
    		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
    			<fo:block>
    				<xsl:value-of select="./cag:duration/text()"></xsl:value-of>
    			</fo:block>
    		</fo:table-cell>
    	</fo:table-row>
    
    	<!--  ## Apply more templates -->
		<!--  ####################################### -->
   		<xsl:apply-templates mode="extra-plan" ></xsl:apply-templates>
    
    </xsl:template>
    
    <xsl:template match="cag:item" mode="extra-plan">
    
    	<fo:table-row>
    		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
    			<fo:block>
    				<xsl:value-of select="./cag:title/text()"></xsl:value-of>
    			</fo:block>
    		</fo:table-cell>
    		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
    			<fo:block>
    				<xsl:value-of select="./cag:description/text()"></xsl:value-of>
    			</fo:block>
    		</fo:table-cell>
    		<fo:table-cell xsl:use-attribute-sets="table-cell-style-common table-cell-style-default">
    			<fo:block>
    				<xsl:value-of select="./cag:duration/text()"></xsl:value-of>
    			</fo:block>
    		</fo:table-cell>
    	</fo:table-row>
    
    </xsl:template>
    
</xsl:stylesheet>