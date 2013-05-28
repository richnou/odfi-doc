<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="http://docbook.org/ns/docbook"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:docbook="http://docbook.org/ns/docbook" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:cag="cag:extra" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<!-- XML Output -->
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />


    <!-- Produce for bytes -->



	<!-- Produce tables -->
	<!-- ######################### -->
	
	<!-- Compact more than one BitArray under one table -->
	<xsl:template match="cag:BitsArrays" >
		
	<section>
          <title>
               <xsl:value-of select="@name"/>
          </title>
          
          <bridgehead>Definition</bridgehead>
		
		<!-- First column will be for bitarray -->
		<table style="text-align:center">
		
		    <!-- Add ID Attribute -->
            <xsl:attribute 
                 name="xml:id" 
                 namespace="www.w3.org/XML/1998/namespace"
                 select="fn:replace(@name,' ','')"
                 ></xsl:attribute> 
		
			<caption>Bit array for: <xsl:value-of select="@name"></xsl:value-of></caption>
			<thead>
				<tr>
					<th colspan="{fn:number(@size)+1}"><xsl:value-of select="@name"></xsl:value-of></th>
				</tr>
			</thead>
			<tbody>
			
			     <!-- Definitions Lines -->
				<xsl:apply-templates mode="tablecontent"></xsl:apply-templates>
				
				<!-- Bit count lines -->
				<tr>
				    <td>Byte</td>
				    
				    <!-- Call Recursively counter -->
				    <xsl:call-template name="putElementCountColumns">
				        <xsl:with-param name="count" select="fn:number(@size) - 1"></xsl:with-param>
				    </xsl:call-template>
				    
				</tr>
			</tbody>
		</table>
		
		
		<bridgehead>Fields Details</bridgehead>
		
		
		<!-- Descriptions list -->
        <itemizedlist>
            <xsl:apply-templates mode="descriptionlist"></xsl:apply-templates>
        </itemizedlist>
        
	</section>	
	
	</xsl:template>
	
	<!-- A table for a single Bitarray -->
	<xsl:template match="cag:BitsArray" >
	
	   <section>
	       <title>
	            <xsl:value-of select="@name"/>
	       </title>
	       
	       <bridgehead>Definition</bridgehead>
           
            <!-- Show Table of fields -->
			<table style="text-align:center">
			
			    <!-- Add ID Attribute -->
			    <xsl:attribute 
			         name="xml:id" 
			         namespace="www.w3.org/XML/1998/namespace"
			         select="fn:replace(@name,' ','')"
			         ></xsl:attribute> 
			     
				<caption>Bit array for: <xsl:value-of select="@name"></xsl:value-of></caption>
				<thead>
					<tr>
						<th colspan="{@size}"><xsl:value-of select="@name"></xsl:value-of></th>
					</tr>
				</thead>
				
				
				<tfoot>    
                     <!-- Bit count lines -->
                    <tr>
                    
                        <!-- Call Recursively counter -->
                        <xsl:call-template name="putElementCountColumns">
                            <xsl:with-param name="count" select="fn:number(@size) - 1"></xsl:with-param>
                        </xsl:call-template>
                    
                    </tr>
                     
                </tfoot> 
				
				<tbody>
				     <tr>
				        <!-- Generate column for each sub field definition. If none -> add ours -->
				        <xsl:choose>
				            <xsl:when test="fn:count(child::cag:BitsArray) = 0">
                                <td><xsl:value-of select="fn:string(.)"></xsl:value-of></td>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates mode="bits-columns"></xsl:apply-templates>
                            </xsl:otherwise>
				        </xsl:choose>
				     </tr>
				</tbody>
				
			</table>
			
			<bridgehead>Fields Details (hum)</bridgehead>
			
			<!-- Descriptions list (only if subfields) -->
			<xsl:choose>
                <xsl:when test="fn:count(child::cag:BitsArray) = 0">
                    <para><xsl:value-of select="fn:string(.)"></xsl:value-of></para>
                </xsl:when>
                <xsl:otherwise>
                   <itemizedlist>
                        <xsl:apply-templates mode="descriptionlist"></xsl:apply-templates>
                    </itemizedlist>
                </xsl:otherwise>
            </xsl:choose>
			
	   
	   </section>
	
	</xsl:template>
	
	<!-- Produce only the table content : a row indeed -->
	<xsl:template match="cag:BitsArray" mode="tablecontent">
	
	   <!-- Are we using local bits array or referenced one -->
	   <xsl:variable name="base" select="."></xsl:variable>
	   <xsl:choose>
	       <xsl:when test="@ref">
	           <xsl:variable name="base" select="cag:BitsArray[@name='@ref']"></xsl:variable>
	       </xsl:when>
	       <xsl:otherwise>
	           <xsl:variable name="base" select="."></xsl:variable>
	       </xsl:otherwise>
	   </xsl:choose>

		<tr>
		      <!-- First Column is the name  -->
			 <td><xsl:value-of select="$base/@name"/></td>
			 
			 <!-- Then show column contents -->
			 <xsl:apply-templates mode="bits-columns"></xsl:apply-templates>
		</tr>
	
	</xsl:template>

    <!-- #### Show every single element into one column -->
    <xsl:template match="cag:BitsArray" mode="bits-columns">
          
	      <!-- Call template on local node or on referenced -->
<!-- 	      <xsl:choose> -->
<!--             <xsl:when test="@ref"> -->
<!--               <xsl:call-template name="doBitsArrayColumns"> -->
<!--                   <xsl:with-param name="base" select="//docbook:article/descendant::cag:BitsArray[@name='@ref']"></xsl:with-param> -->
<!--               </xsl:call-template> -->
<!--             </xsl:when> -->
<!--             <xsl:otherwise> -->
<!--                 <xsl:call-template name="doBitsArrayColumns"> -->
<!--                      <xsl:with-param name="base" select="."></xsl:with-param> -->
<!--                  </xsl:call-template> -->
<!--             </xsl:otherwise> -->
<!--           </xsl:choose> -->

         <td colspan="{@size}">
            <xsl:value-of select="@name | @ref"></xsl:value-of>
         </td>
          
    </xsl:template>
    
<!--     <xsl:template name="doBitsArrayColumns"> -->
<!--         <xsl:param name="base" required="yes"/> -->
        
<!--          Define Column -->
<!--           -->
<!--          <td colspan="{$base/@size}"> -->
<!--             <xsl:value-of select="$base/@name"></xsl:value-of> -->
<!--          </td> -->
         
         
<!--     </xsl:template> -->

    <xsl:template name="putElementCountColumns">
        <xsl:param name="count" required="yes"/>
        
         <!-- Define Column -->
         <!--  -->
         <td>
            <xsl:value-of select="$count"></xsl:value-of>
         </td>

         <!-- Recursively call next one -->
         <xsl:if test="$count > 0">
	         <xsl:call-template name="putElementCountColumns">
	             <xsl:with-param name="count" select="$count - 1"></xsl:with-param>
	         </xsl:call-template>
         </xsl:if>
         
         
    </xsl:template>
    


	<!-- Description List -->
	<!-- ###################### -->
	
	<!-- Generate a list item to describe a bits array -->
	<xsl:template match="cag:BitsArray" mode="descriptionlist">
	   
	   <!-- List item -->
	   <listitem>
	       <!-- Name -->
	       <para>
		       <xsl:choose>   
		           <xsl:when test="@ref">
		           
		               <!-- Name -->
		               <!-- If it is a reference, generate a link -->
		               <link linkend="{@ref}">
		               <xsl:value-of select="@ref"></xsl:value-of>
		               </link>
		               
		           </xsl:when>
		           <xsl:otherwise>
		               
		               <!-- Set id attribute for referencing -->
		               <xsl:attribute name="xml:id" namespace="www.w3.org/XML/1998/namespace" select="fn:string-join((fn:replace(../@name,' ',''),fn:replace(@name,' ','')),'.')"></xsl:attribute>
<!-- 		               <xsl:attribute name="xml:id" namespace="www.w3.org/XML/1998/namespace" select="fn:generate-id()"></xsl:attribute> -->
		               <!-- Name -->
		               <xsl:value-of select="@name"></xsl:value-of>
		               
		               <!-- Otherwise, just copy content -->
		               <xsl:copy-of select="./*" />
		               
		           </xsl:otherwise>
		       </xsl:choose>
	       </para>
	       <!-- recursive listing if nested definitions -->
	       <xsl:if test="./cag:BitsArray">
		       <para>
		           <itemizedlist>
		               <xsl:apply-templates mode="descriptionlist"></xsl:apply-templates>
		           </itemizedlist>
		        </para>
	       </xsl:if>
	   </listitem>
	</xsl:template>


	<!-- Identity function to preserve nodes -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>