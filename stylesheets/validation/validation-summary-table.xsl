<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://docbook.org/ns/docbook"
	xmlns:cag="org:odfi:validation" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


    <xsl:output method="xml" encoding="UTF-8"></xsl:output>

	<xsl:template name="validation-summary-table">

		<table>
			<caption>Validation Procedure Summary Table</caption>
			<thead>
				<tr>
					<th colspan="4">Validation Procedure Summary Table</th>
				</tr>
				<tr>
					<th>Test</th>
					<th>Success</th>
					<th>Fail</th>
					<th>Information</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates mode="validation-summary-table"></xsl:apply-templates>
			</tbody>

		</table>

	</xsl:template>



	<!-- Template to display Validation as a big summary table -->
	<xsl:template match="cag:validation" mode="validation-summary-table">

		<!-- Spanning row for validation Name -->
		<tr>
			<td colspan="4">
				<xsl:value-of select="@name" />
			</td>
		</tr>

		<!-- Rows for tests -->
		<xsl:apply-templates mode="validation-summary-table"></xsl:apply-templates>

	</xsl:template>


	<!-- A test is converted to a row -->
	<xsl:template match="cag:test" mode="validation-summary-table">

		<tr>
			<td>
				<xsl:value-of select="@name" />
			</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>

	</xsl:template>


</xsl:stylesheet>