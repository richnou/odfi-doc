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
    <xsl:template match="/odfi:ValidationProcess">#!/usr/bin/env tclsh
  
#############################################################
## Validation Procedure for: <xsl:value-of select="@name"/>
#############################################################

## Pre-Require packages
#############################

## TCL Must be 8.5
puts "Detected TCL version: [info tclversion]"
if {[info tclversion] != 8.5} {
    error "TCL version 8.5 is required"
}

## We need ITCL
package require Itcl 3.3

## We need ODFI TCl utilities
package require odfi.common 1.0.0

## Source validation tools
source ../../sw/tcl/validation_xml_utils.tcl

## Preset some variables and tools
####################################

#### Command Line arguments
## resume: to resume the test procedure at some point
## tester (mandatory) : to provide the name of the tester to be reported
## testerBoardID : to provide the ID of the board beeing tested per hand (for the tester side)
odfi::common::getOptionsFromArgumentOrIni "test_run.ini" "tester:" "resume?:" "testerBoardID:?"

#### Validation ID is the id for the complete process
set validationId <xsl:value-of select="@id"/>

#### Prepare validation report
ValidationProcessReport validationProcessReport
validationProcessReport setTester $odfi::common::argv_tester
validationProcessReport setId $validationId

#### Prepare test resume (resume argument saves which test the process last started, to allow interruption for non fully automatic steps)
set resumeAt -1
if {$odfi::common::argv_resume != false } {
    set resumeAt $odfi::common::argv_resume
    ::odfi::common::logInfo "Test will be resumed at $resumeAt"
}

# Resume found variable is used to allow detection of resumeAt value not matching any test
# and thus not finishing the test successfully
set resumeFound false

#### Test run variables

# Global indication of failing of a test
set testRunSuccess true

# Fatal fail of a test
set testRunFatal false

## Verifications
#############################

## Source pre and post run
source test_pre_run.tcl
source test_post_run.tcl

## Source and call precondition proc to ensure the ressources needed by the test are available
## The procedure might call error to fail
puts "Verifying test suite preconditions..."
puts "-------------"
if {[catch {test_pre_run} err]} {
    validationProcessReport addError $err
    puts "Report: [validationProcessReport toString]"
    exit -1
}
puts "-------------"

## Display the test structure
#######################################
puts "The validation procedure has the following structure"
set allScriptsFound true
<xsl:apply-templates mode="test-structure"></xsl:apply-templates>

## Error if all scripts not found
if {$allScriptsFound==false} {
    validationProcessReport addError "Some test scripts were not found"
    puts "Report: [validationProcessReport toString]"
    exit -1
}

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
puts "Test Result: [validationProcessReport toIndentedString]"   

## Calling post run
odfi::common::logInfo "Calling post run procedure...."
test_post_run

</xsl:template>




<!-- Display the structure of the validation -->
<!-- ####################################### -->
<xsl:template match="odfi:Validation" mode="test-structure">
puts "<xsl:number count="odfi:Validation" from="/" level="multiple" ></xsl:number> Validation: <xsl:value-of select="@name"/>"
<!-- Show validation tests && sub validations -->
<xsl:apply-templates mode="test-structure" select="odfi:Test|odfi:Validation"></xsl:apply-templates>
puts ""
</xsl:template>

    
<!-- Show test Name -->
<xsl:template match="odfi:Test" mode="test-structure">
puts -nonewline "<xsl:number count="odfi:Validation|odfi:Test" from="/" level="multiple" ></xsl:number> Test: <xsl:value-of select="@name"/>"
puts -nonewline " ... <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>.tcl "
if {[file exists <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>.tcl]} {
    puts "found"
} else {
    puts "not found...creating"
    file mkdir <xsl:value-of select="../@name"/>
    exec touch <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>.tcl
    set allScriptsFound true
}
</xsl:template>
    
    
<!-- Source the corresponding scripts -->
<!-- ################################ -->

<xsl:template match="odfi:Validation" mode="source-scripts">
## <xsl:number count="odfi:Validation" from="/" level="multiple" ></xsl:number>  <xsl:value-of select="@name"/>
<!-- Show validation tests && sub validations -->
<xsl:apply-templates mode="source-scripts" select="odfi:Test|odfi:Validation"></xsl:apply-templates>
</xsl:template>

    
<!-- Show test Name -->
<xsl:template match="odfi:Test" mode="source-scripts">
source <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>.tcl
</xsl:template>


<!-- Run the tests -->
<!-- ################################ -->

<!-- Validation -->
<xsl:template match="odfi:Validation" mode="run">
## <xsl:number count="odfi:Validation" from="/" level="multiple" ></xsl:number>  <xsl:value-of select="@name"/>
puts "<xsl:number count="odfi:Validation" from="/" level="multiple" ></xsl:number> Validation: <xsl:value-of select="@name"/>"
set lastValidation [validationProcessReport addValidationReport <xsl:value-of select="@name"/>]
<!-- Show validation tests && sub validations -->
<xsl:apply-templates mode="run" select="odfi:Test|odfi:Validation" ></xsl:apply-templates>
</xsl:template>

    
<!-- Test -->
<xsl:template match="odfi:Test" mode="run">
puts "Test <xsl:value-of select="../@name"/>/test_<xsl:value-of select="@name"/>"
puts "-------------"
set testReport [validationProcessReport addTestReport $lastValidation <xsl:value-of select="@name"/>]
if {$testRunFatal==true} {
    ## In case of previous fatal error -> Fail strictly
    validationProcessReport addError "Test not run because of previous fatal error" $testReport
} elseif {[catch {set testResult [test_<xsl:value-of select="@name"/>]} err]} {
    validationProcessReport addError $err $testReport
    set testRunSuccess false
    <xsl:if test="./@fatal='true'">
     ## If Fatal test, register error
    set testRunFatal true
    </xsl:if>
} else {
    validationProcessReport addTestReportResult $testReport $testResult
}
puts "-------------"
</xsl:template>


<xsl:template match="@*|*" mode="#all"></xsl:template>

</xsl:stylesheet>