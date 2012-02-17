<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:cag="cag:extra" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<!-- XML Output -->
	<xsl:output method="text" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:template match="//docbook:article">
	
#!/usr/bin/env tcl

<xsl:apply-templates select="descendant::cag:validation" />


	</xsl:template>



	<!-- Template to display sections as real content -->
	<xsl:template match="cag:validation">

		<!-- Apply remaining -->
		<xsl:apply-templates></xsl:apply-templates>

	</xsl:template>

	<!-- Test -->
	<xsl:template match="cag:test">

        <xsl:param name="testfilename"><xsl:value-of select="@name" />.tcl</xsl:param>
        <xsl:result-document href="{$testfilename}">
        
#!/usr/bin/env tcl
      
## Test: <xsl:value-of select="@name" />

<xsl:apply-templates></xsl:apply-templates>        
        
        </xsl:result-document>


	</xsl:template>

	<!-- Steps as List item -->
	<xsl:template match="cag:step">

proc <xsl:value-of select="@name" /> args {


}

	</xsl:template>



</xsl:stylesheet>