<?xml version="1.0" encoding="UTF-8"?>
<!-- 

This code changes the images paths to allow dynamic loading of the ones only available in the file system
and not under the normal web path

 -->
<xsl:stylesheet version="1.0" xmlns:php="http://php.net/xsl" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
	<xsl:param name="basePath"></xsl:param>

	<xsl:template match="img">
		
		<xsl:choose>
		
		
			<!-- SVG -->
			<xsl:when test="php:function('isSVGImage',string(@src))"> 
				
				<embed type="image/svg+xml">
					<xsl:attribute name="src">load_image.php?path=<xsl:value-of select="$basePath"/>/<xsl:value-of select="@src"/></xsl:attribute>
				</embed>
			
			</xsl:when>
			
			<!-- Standard images -->
			<xsl:when test="not(starts-with(@src,'http'))">
				
				<xsl:copy>
							
					<xsl:apply-templates select="@*|node()"></xsl:apply-templates>
					
					<xsl:attribute name="src">load_image.php?path=<xsl:value-of select="$basePath"/>/<xsl:value-of select="@src"/></xsl:attribute>
			
				</xsl:copy>
				
			
			</xsl:when>
			
			<!-- Identity if the path is an online path -->
			<xsl:otherwise>
				<xsl:copy-of select="."/>	
			</xsl:otherwise>
		</xsl:choose>
		
	
	</xsl:template>

	<!-- Identity -->	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"></xsl:apply-templates>
		
		</xsl:copy>
		
	</xsl:template>
	
	

</xsl:stylesheet>