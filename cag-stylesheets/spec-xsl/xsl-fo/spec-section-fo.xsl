<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:docbook="http://docbook.org/ns/docbook"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.w3.org/1999/XSL/Format
            fop.xsd">
    
    <!-- Template to display sections as real content -->
    <xsl:template match="docbook:section" mode="content">
    
        <!-- ## Prepare Datas -->
        <!-- ####################################### -->
        <xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
        <xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
        
        <!-- Start all sections on a new page for a beginning -->
        <fo:page-sequence master-reference="A4">
            <fo:flow flow-name="xsl-region-body">
            
    	        <!-- ## Title -->
    	        <!-- ############## -->
                <fo:block id="ref-toc-">
                    <xsl:attribute name="id">ref-toc-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute>
                   <xsl:value-of select="$toc-hier-pos"/>
                   <xsl:text>. </xsl:text>
                   <xsl:value-of select="./docbook:title/text()"></xsl:value-of>
                </fo:block>
    	       
            
            
            
            
    	        <!-- ## Apply more templates -->
    	        <!-- ####################################### -->
    	       <!-- <xsl:apply-templates mode="content" ></xsl:apply-templates> -->
            </fo:flow>
        </fo:page-sequence>
    
    </xsl:template>
    
    
    
    <!-- Handle a paragraph -->
    <xsl:template match="docbook:para" mode="content">
       
    </xsl:template>
    
    <!-- 
        Figure Handling. only support image at the moment
     -->
     <xsl:template match="docbook:figure" mode="content">
        
     </xsl:template>
     
     
     
    <!-- ## IGNORE: title -->
    <xsl:template match="docbook:title" mode="content">
      
    </xsl:template>
    
</xsl:stylesheet>