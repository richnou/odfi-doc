<?xml version="1.0" encoding="UTF-8"?>
<!-- 

This is a trivial public domain code
Written for the Computer Architecture Group, University of Heidelberg, Germany (http://ra.ziti.uni-heidelberg.de)

Sorry for the commented debugging code, feel free to clean things out.

 -->
<xsl:stylesheet version="1.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<!-- TODO: Auto-generated template -->
		
		<div>
			<!-- TOC -->
			<div class="toc">Table of content</div>
			<ul>
					<xsl:for-each select="//h1">
						<xsl:call-template name="toc-single-level">
							<xsl:with-param name="node" select="."></xsl:with-param>
							<xsl:with-param name="level" select="1"></xsl:with-param>
						</xsl:call-template>
					</xsl:for-each>

			</ul>

		
			<!-- Content -->
<!-- 			<xsl:copy-of select="child::*"></xsl:copy-of> -->
			<xsl:apply-templates></xsl:apply-templates>
			
		</div>
		
		
	</xsl:template>
	

	
	
	<xsl:template name="toc-single-level">
		<xsl:param name="node"></xsl:param>
		<xsl:param name="level"></xsl:param>
		<xsl:variable name="next-level" select="$level+1"></xsl:variable>
		<xsl:variable name="next-level-name">h<xsl:value-of select="$level+1"/></xsl:variable>
		<xsl:variable name="this-level-name">h<xsl:value-of select="$level"/></xsl:variable>
		
		
		<!-- Output sub -->
		<xsl:variable name="currentPos" select="count(preceding-sibling::*)+1"></xsl:variable>
		<xsl:variable name="nsl" select="count((following-sibling::*[local-name()=$this-level-name][1])/preceding-sibling::*)+1"></xsl:variable>
		
		<xsl:choose>
				<xsl:when test="$nsl=1">
					<xsl:variable name="inbetween" select="following-sibling::*[local-name()=$next-level-name]"/>
					
					<li>
						<a>
							<xsl:attribute name="href">#toc-<xsl:value-of select="$this-level-name"/>-<xsl:value-of select="$currentPos"/></xsl:attribute> 
							<xsl:value-of select="string(.)"></xsl:value-of>
<!-- 							(pos:<xsl:value-of select="$currentPos"/>, -->
<!-- 							nextSlPos:<xsl:value-of select="$nsl"/>,  -->
<!-- 							nextCount: <xsl:value-of select="count($inbetween)"/>, -->
<!-- 							nsearch: <xsl:value-of select="$next-level-name"/>) -->
						</a>
					</li>
						
					<xsl:if test="count($inbetween)>0">
						<ul>
							<xsl:for-each select="$inbetween">
							<xsl:call-template name="toc-single-level">
								<xsl:with-param name="node" select="."></xsl:with-param>
								<xsl:with-param name="level" select="$next-level"></xsl:with-param>
							</xsl:call-template>
							</xsl:for-each>
						</ul>
					</xsl:if>
					
				</xsl:when>
				<xsl:otherwise>
					
					<xsl:variable name="inbetween" select="(following-sibling::*[local-name()=$this-level-name][position()=1])/preceding-sibling::*[local-name()=$next-level-name]"/>
					
					<li>
						<a>
							<xsl:attribute name="href">#toc-<xsl:value-of select="$this-level-name"/>-<xsl:value-of select="$currentPos"/></xsl:attribute> 
							<xsl:value-of select="string(.)"></xsl:value-of> 
<!-- 							(pos:<xsl:value-of select="$currentPos"/>, -->
<!-- 							nextSlPos:<xsl:value-of select="$nsl"/>,  -->
<!-- 							nextCount: <xsl:value-of select="count($inbetween)"/>, -->
<!-- 							nsearch: <xsl:value-of select="$next-level-name"/>) -->
						</a>
					</li>
					

	
					
					
					<xsl:if test="count($inbetween)>0">
					
						<ul>
						<xsl:for-each select="$inbetween">
							<xsl:variable name="nCP" select="count(preceding-sibling::*)+1"></xsl:variable>
							<xsl:if test="$nCP > $currentPos">
								<xsl:call-template name="toc-single-level">
									<xsl:with-param name="node" select="."></xsl:with-param>
									<xsl:with-param name="level" select="$next-level"></xsl:with-param>
								</xsl:call-template>
							
								
							</xsl:if>
						</xsl:for-each>
						</ul>
					
					</xsl:if>
					

				</xsl:otherwise>
			</xsl:choose>
		
	
		
	
	</xsl:template>
	
	
	
	
	<xsl:template match="h1|h2|h3|h4|h5|h6">
	
		<xsl:copy>
			<xsl:variable name="currentPos" select="count(preceding-sibling::*)+1"></xsl:variable>
			<xsl:attribute name="id">toc-<xsl:value-of select="local-name()"/>-<xsl:value-of select="$currentPos"/></xsl:attribute>
			<xsl:apply-templates></xsl:apply-templates>
		</xsl:copy>
	
	</xsl:template>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"></xsl:apply-templates>
		
		</xsl:copy>
		
	</xsl:template>
	
	

</xsl:stylesheet>