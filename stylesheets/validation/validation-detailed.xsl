<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns="http://docbook.org/ns/docbook"
    xmlns:cag="org:odfi:validation"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
	<!-- Template to display sections as real content -->
	<xsl:template match="cag:Validation" mode="validation-detailed">


		<section>
			<title><xsl:value-of select="@name"></xsl:value-of></title>

            <!-- Description of validation -->
            <xsl:call-template name="Description"></xsl:call-template>

			<!-- Global parameters as Table ? -->
			<xsl:call-template name="parameters">
				<xsl:with-param name="base" select="."></xsl:with-param>
			</xsl:call-template>

			<!-- Apply remaining -->
			<xsl:apply-templates mode="validation-detailed"></xsl:apply-templates>

		</section>

	</xsl:template>

	<!-- Test -->
	<xsl:template match="cag:Test"  mode="validation-detailed">

		<!-- Name and description -->
		<section>
			<title>
				<xsl:value-of select="@name" />
			</title>

            <!-- Description -->
            <xsl:call-template name="Description"></xsl:call-template>


			<!-- Parameters -->
			<xsl:call-template name="parameters">
				<xsl:with-param name="base" select="."></xsl:with-param>
			</xsl:call-template>

			<!-- Steps -->
			<xsl:if test="./cag:Step">
				<orderedlist>
					<xsl:apply-templates select="./cag:Step" mode="validation-detailed"></xsl:apply-templates>
				</orderedlist>
			</xsl:if>

		</section>

	</xsl:template>

	<!-- Steps as List item -->
	<xsl:template match="cag:Step"  mode="validation-detailed">

		<!-- -->
		<listitem>
			<para>
				<xsl:value-of select="@name"></xsl:value-of>
			</para>
			
			<!-- Description -->
            <xsl:call-template name="Description"></xsl:call-template>

			<!-- Parameters -->
			<para>
				<xsl:call-template name="parameters">
					<xsl:with-param name="base" select="."></xsl:with-param>
				</xsl:call-template>
			</para>
		</listitem>



	</xsl:template>

	<!-- Display some parameters as a table -->
	<xsl:template name="parameters" >

		<xsl:param name="base" required="yes"></xsl:param>

		<xsl:if test="$base/cag:Parameter">

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
					<xsl:apply-templates select="$base/cag:Parameter"
						mode="parameters"></xsl:apply-templates>
				</tbody>
			</table>

		</xsl:if>
	</xsl:template>


	<!-- A parameter is a table row -->
	<xsl:template match="cag:Parameter" mode="parameters">

		<tr>
			<td>
				<xsl:value-of select="@name" />
			</td>
			<td>
				<!-- Description -->
                <xsl:call-template name="Description"></xsl:call-template>
			</td>
			<td>
				<xsl:value-of select="./cag:Value/text()" />
			</td>
		</tr>


	</xsl:template>
	
	<!-- Description -->
	<!-- ########### -->
	<xsl:template name="Description">
	
	   <xsl:if test="./cag:Description">
	       <para>
                <xsl:copy-of select="./cag:Description"></xsl:copy-of>
            </para>
	   </xsl:if>
        
	</xsl:template>
	
</xsl:stylesheet>