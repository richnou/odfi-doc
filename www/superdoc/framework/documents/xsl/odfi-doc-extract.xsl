<?xml version="1.0" encoding="UTF-8"?>
<!-- 

This code changes the images paths to allow dynamic loading of the ones only available in the file system
and not under the normal web path

 -->
<xsl:stylesheet version="1.0" xmlns:php="http://php.net/xsl" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
	<xsl:param name="basePath"></xsl:param>

	<xsl:template match="extractsection">
		
		
<!-- 		<xsl:value-of select="php:function('odfiSectionExtract',string(@file),string(@name),$basePath)"></xsl:value-of> -->
		
		<div>
		<xsl:copy-of select="php:function('odfiSectionExtract',string(@file),string(@name),$basePath)">
			
		</xsl:copy-of>
		</div>
	
<!-- 	<xsl:apply-templates select="@*|node()"></xsl:apply-templates> -->
		
	
	</xsl:template>
	
	
	<!-- <listfolder path='' pattern='' />-->
	<xsl:template match="listfolders">
	
		<div>
		<xsl:copy-of select="php:function('odfiExtrasListFolder',string(@path),string(@pattern),$basePath)" />
			
		</div>
		
	
	</xsl:template>
	

	<!-- Identity -->	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"></xsl:apply-templates>
		</xsl:copy>
		
	</xsl:template>
	
	

</xsl:stylesheet>