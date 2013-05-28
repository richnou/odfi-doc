<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	version="2.0"
	xmlns="http://docbook.org/ns/docbook"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<!-- XML Output -->
	<xsl:output
		method="xml"
		version="1.0"
		encoding="UTF-8"
		indent="yes"
		 />


	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>