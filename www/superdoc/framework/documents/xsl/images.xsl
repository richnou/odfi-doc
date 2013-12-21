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
					<xsl:attribute name="src">/superdoc/rest/api.php?function=loadImage&amp;path=<xsl:value-of select="$basePath"/>/<xsl:value-of select="@src"/></xsl:attribute>
				</embed>

			</xsl:when>

			<!-- Standard images non svg -->
			<!-- ######################## -->
			<xsl:when test="not(starts-with(@src,'http'))">

				<xsl:copy>

					<xsl:apply-templates select="@*|node()"></xsl:apply-templates>

					<!-- Relative path or absolute path -->
					<xsl:choose>
						<xsl:when test="not(starts-with(@src,'/'))">
							<xsl:attribute name="src">/superdoc/rest/api.php?function=loadImage&amp;path=<xsl:value-of select="$basePath"/>/<xsl:value-of select="@src"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="src">/superdoc/rest/api.php?function=loadImage&amp;path=<xsl:value-of select="@src"/></xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:copy>


			</xsl:when>

			<!-- Identity if the path is an online path -->
			<xsl:otherwise>
				<xsl:copy-of select="."/>
			</xsl:otherwise>
		</xsl:choose>


	</xsl:template>


	<!-- Resolve Links -->
	<xsl:template match="a">

		<xsl:choose>

			<xsl:when test="contains(@href,'#')">
				<xsl:copy-of select="."/>
			</xsl:when>

			<!--Server / paths, append application path -->
			<!-- ######################## -->
			<xsl:when test="starts-with(@href,'/')">

				<xsl:copy>

					<xsl:apply-templates select="@*"></xsl:apply-templates>

					<xsl:attribute name="href">/superdoc/<xsl:value-of select="@href"/></xsl:attribute>
					<xsl:attribute name="target">_blank</xsl:attribute>

					<xsl:apply-templates select="node()"></xsl:apply-templates>

				</xsl:copy>

			</xsl:when>

			<!-- Local Relative paths -->
			<!-- ######################## -->
			<xsl:when test="not(starts-with(@href,'http')) and not(starts-with(@href,'/'))">

				<xsl:variable name="fullPath"><xsl:value-of select="$basePath"/>/<xsl:value-of select="@href"/></xsl:variable>
				<xsl:copy>

					<xsl:apply-templates select="@*"></xsl:apply-templates>

					<xsl:attribute name="href">/superdoc/rest/api.php?function=loadResource&amp;path=$fullPath</xsl:attribute>
					<xsl:attribute name="target">_blank</xsl:attribute>

					<xsl:apply-templates select="node()"></xsl:apply-templates>

					
				</xsl:copy>

				<!-- Add WWarning File does not exist -->
				<xsl:if test="not(php:function('fileExists',string($fullPath)))">

					<span class=".alert"><xsl:text>!! WARNING LINK BROKEN !!</xsl:text></span>

				</xsl:if>

				


			</xsl:when>

			<!-- Identity if the path is an online path -->
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="@*"></xsl:apply-templates>
					<xsl:attribute name="target">_blank</xsl:attribute>
					<xsl:apply-templates select="node()"></xsl:apply-templates>
				</xsl:copy>
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
