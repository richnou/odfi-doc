<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://docbook.org/ns/docbook"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:cag="cag:extra" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<!-- XML Output -->
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />


	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>


	<!-- Template to display sections as real content -->
	<xsl:template match="cag:validation">


		<section>
			<title>Validation Procedure</title>

			<!-- Global parameters as Table ? -->
			<xsl:call-template name="parameters">
				<xsl:with-param name="base" select="."></xsl:with-param>
			</xsl:call-template>

			<!-- Apply remaining -->
			<xsl:apply-templates></xsl:apply-templates>

		</section>

	</xsl:template>

	<!-- Test -->
	<xsl:template match="cag:test">

		<!-- Name and description -->
		<section>
			<title>
				<xsl:value-of select="@name" />
			</title>

			<para>
				<xsl:value-of select="./cag:description/text()" />
			</para>

			<!-- Parameters -->
			<xsl:call-template name="parameters">
				<xsl:with-param name="base" select="."></xsl:with-param>
			</xsl:call-template>

			<!-- Steps -->
			<xsl:if test="./cag:step">
				<orderedlist>
					<xsl:apply-templates select="./cag:step"></xsl:apply-templates>
				</orderedlist>
			</xsl:if>

		</section>

	</xsl:template>

	<!-- Steps as List item -->
	<xsl:template match="cag:step">

		<!-- -->
		<listitem>
			<para>
				<xsl:value-of select="@name"></xsl:value-of>
			</para>
			<para>
				<xsl:value-of select="./cag:description/text()"></xsl:value-of>
			</para>

			<!-- Parameters -->
			<para>
				<xsl:call-template name="parameters">
					<xsl:with-param name="base" select="."></xsl:with-param>
				</xsl:call-template>
			</para>
		</listitem>



	</xsl:template>

	<!-- Display some parameters as a table -->
	<xsl:template name="parameters">

		<xsl:param name="base" required="yes"></xsl:param>

		<xsl:if test="$base/cag:parameter">

			<table>
				<thead>
					<tr>
						<td colspan="3">Parameters</td>
					</tr>
					<tr>
						<td>Name</td>
						<td>Description</td>
						<td>Value</td>
					</tr>
				</thead>

				<tbody>
					<xsl:apply-templates select="$base/cag:parameter"
						mode="parameters"></xsl:apply-templates>
				</tbody>
			</table>

		</xsl:if>
	</xsl:template>


	<!-- A parameter is a table row -->
	<xsl:template match="cag:parameter" mode="parameters">

		<tr>
			<td>
				<xsl:value-of select="@name" />
			</td>
			<td>
				<xsl:value-of select="./cag:description/text()" />
			</td>
			<td>
				<xsl:value-of select="./cag:value/text()" />
			</td>
		</tr>


	</xsl:template>


</xsl:stylesheet>