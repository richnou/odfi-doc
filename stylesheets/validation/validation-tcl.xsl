<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns="http://docbook.org/ns/docbook" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:odfi="org:odfi:validation" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

     <xsl:import  href="validation-summary-table.xsl" />

	<!-- XML Output -->
	<xsl:output  method="text" encoding="UTF-8"
		indent="no" media-type="text/plain" use-character-maps="xml"  />

    <xsl:character-map name="xml">
	    <xsl:output-character character="«" string="&lt;"/>   
        <xsl:output-character character="»" string="&gt;"/>
	    <xsl:output-character character="§" string='"'/>
    </xsl:character-map>
   


    <!-- Match on validation -->
    <xsl:template match="/odfi:validation">#!/usr/bin/env tclsh
  
#############################################################
## Validation Procedure for: <xsl:value-of select="@name"/>
#############################################################

## Pre-Require packages and verifications
#############################

## TCL Must be 8.5
if {[info tclversion] != 8.5} {
    error "TCL version 8.5 is required"
}

## Source and call precondition proc to ensure the ressources needed by the test are available
## The procedure might call error to fail
puts "Verifying test suite preconditions..."
source test_verify_preconditions.tcl
test_verify_preconditions


## Display the test structure
#######################################
puts "The validation procedure has the following structure"
<xsl:apply-templates mode="test-structure"></xsl:apply-templates>
## Source The sub validation scripts to have the corresponding methods
##############################
puts "Sourcing the implementation scripts..."
<xsl:apply-templates mode="source-scripts"></xsl:apply-templates>
## Call The tests
###############################
puts "Running tests"
<xsl:apply-templates mode="run"></xsl:apply-templates>

## Res
####################
    


</xsl:template>




<!-- Display the structure of the validation -->
<!-- ####################################### -->
<xsl:template match="odfi:validation" mode="test-structure">
puts "<xsl:number count="odfi:validation" from="/" level="multiple" ></xsl:number> Validation: <xsl:value-of select="@name"/>"
<!-- Show validation tests && sub validations -->
<xsl:apply-templates mode="test-structure" select="odfi:test|odfi:validation"></xsl:apply-templates>
</xsl:template>

    
<!-- Show test Name -->
<xsl:template match="odfi:test" mode="test-structure">
puts "<xsl:number count="odfi:validation|odfi:test" from="/" level="multiple" ></xsl:number> Test: <xsl:value-of select="@name"/>"
</xsl:template>
    
    
<!-- Source the corresponding scripts -->
<!-- ################################ -->

<xsl:template match="odfi:validation" mode="source-scripts">
## <xsl:number count="odfi:validation" from="/" level="multiple" ></xsl:number>  <xsl:value-of select="@name"/>
<!-- Show validation tests && sub validations -->
<xsl:apply-templates mode="source-scripts" select="odfi:test|odfi:validation"></xsl:apply-templates>
</xsl:template>

    
<!-- Show test Name -->
<xsl:template match="odfi:test" mode="source-scripts">
source <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>.tcl
</xsl:template>


<!-- Run the tests -->
<!-- ################################ -->

<xsl:template match="odfi:validation" mode="run">
## <xsl:number count="odfi:validation" from="/" level="multiple" ></xsl:number>  <xsl:value-of select="@name"/>
puts "Running validation: <xsl:number count="odfi:validation" from="/" level="multiple" ></xsl:number>  <xsl:value-of select="@name"/>"
<!-- Show validation tests && sub validations -->
<xsl:apply-templates mode="run" select="odfi:test|odfi:validation" ></xsl:apply-templates>
</xsl:template>

    
<!-- Show test Name -->
<xsl:template match="odfi:test" mode="run">
puts "Running test <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>"
set res [test_<xsl:value-of select="@name"/>]

<!-- Result thing -->
set tableRes "@ x &lt; &gt;"

</xsl:template>


<xsl:template match="@*|*" mode="#all"></xsl:template>

</xsl:stylesheet>