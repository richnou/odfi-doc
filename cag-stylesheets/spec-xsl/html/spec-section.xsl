<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:docbook="http://docbook.org/ns/docbook"
    xmlns="http://www.w3.org/1999/xhtml">
    
    <!-- Template to display sections as real content -->
    <xsl:template match="docbook:section" mode="content">
    
        <!-- ## Prepare Datas -->
        <!-- ####################################### -->
        <xsl:variable name="toc-hier-pos"><xsl:number from="//docbook:article" level="multiple"></xsl:number></xsl:variable>
        <xsl:variable name="toc-hier-level"><xsl:value-of select="fn:count(fn:tokenize($toc-hier-pos,'\.'))"></xsl:value-of></xsl:variable>
        
        <div>
            <xsl:attribute name="class">section-level-<xsl:value-of select="$toc-hier-level"/></xsl:attribute>
            <xsl:attribute name="id">toc-section-ref-<xsl:value-of select="$toc-hier-pos"/></xsl:attribute>
	        
	        <!-- ## Title -->
	        <!-- ############## -->
	        <xsl:element name="h{$toc-hier-level}">
	           <xsl:value-of select="$toc-hier-pos"/>
	           <xsl:text>. </xsl:text>
	           <xsl:value-of select="./docbook:title/text()"></xsl:value-of>
	        </xsl:element>
        
        
        
        
	        <!-- ## Apply more templates -->
	        <!-- ####################################### -->
	        <xsl:apply-templates mode="content" ></xsl:apply-templates>
        
        
        </div>
    
    </xsl:template>
    
    
    
    <!-- Handle a paragraph -->
    <xsl:template match="docbook:para" mode="content">
        <p>
            <xsl:copy-of select="./text()"></xsl:copy-of>
        </p>
    </xsl:template>
    
    <!-- 
        Figure Handling. only support image at the moment
     -->
     <xsl:template match="docbook:figure" mode="content">
        <figure>     
            <!-- The media Object -->
            <xsl:if test="./docbook:mediaobject/docbook:imageobject/docbook:imagedata">
                <xsl:variable name="imageData" select="./docbook:mediaobject/docbook:imageobject/docbook:imagedata"></xsl:variable>
                <img alt="">
                    <xsl:attribute name="onmouseup">specImageZoom('<xsl:value-of select="$imageData/@fileref"/>')</xsl:attribute>
                    <xsl:attribute name="src">
                        <xsl:value-of select="$imageData/@fileref"/>
                    </xsl:attribute>
                    <xsl:attribute name="style">
                        <xsl:if test="$imageData/@width">width: <xsl:value-of select="$imageData/@width"/>;</xsl:if>
                    </xsl:attribute>
                </img>
            
            </xsl:if>
        
            <!-- Propagate Caption if there is one -->
            <xsl:if test="./docbook:caption">
                <figcaption>
                    <xsl:value-of select="./docbook:caption/text()"/>
                </figcaption>
            </xsl:if>
        </figure>
     </xsl:template>
     
     
     
    <!-- ## IGNORE: title -->
    <xsl:template match="docbook:title" mode="content">
      
    </xsl:template>
    
</xsl:stylesheet>