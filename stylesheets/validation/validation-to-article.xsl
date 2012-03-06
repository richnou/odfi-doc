<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns="http://docbook.org/ns/docbook"
    xmlns:odfi="org:odfi:validation"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
	 <!-- XML Output -->
    <xsl:output
        method="xml"
        version="1.0"
        encoding="UTF-8"
        indent="yes"
        />
	
	<!--  -->
	<xsl:include href="validation-detailed.xsl" />
	<xsl:include href="validation-summary-table.xsl" />
	
	
	<!-- Root -> Generate an article -->
	<xsl:template match="/odfi:validation">
		
        <article>
            
            <info>
                <title><xsl:value-of select="@name"></xsl:value-of></title>
            </info>
        
            <toc></toc>
            
            
            
            <!-- Generate Detailed description -->
            <section>
                <title>Validation procedure details</title>
                
   
                <xsl:apply-templates mode="validation-detailed"></xsl:apply-templates>
                
            </section>
        
            <!-- Generate Summary table  -->
            <section>
                <title>Summary Table</title>
                
                
                <xsl:call-template name="validation-summary-table"></xsl:call-template>
                
            </section>
        
        
        </article>
        

	</xsl:template>
	
	
	
	
	
</xsl:stylesheet>