## Provides the base API interface for OSYS Register File Generator
package provide osys::rfg 1.0.0
package require Itcl 3.4
package require odfi::common
package require odfi::list 2.0.0

namespace eval osys::rfg {


    #############################
    ## Generator Search
    ##############################

    ## Tries to instanciate a generator using provided full name class
    ## If not found, tries to load a package using: package require $name
    proc getGenerator {name registerFile} {

       ## Search for class 
        set generators [itcl::find classes $name]

        if {[llength $generators]==0} {

            ## Not Found, try to load package 
            #########
            set packageName "osys::rfg::generator::[string tolower $name]"
            set generatorName "::${packageName}::$name"
            if {[catch "package require $packageName"]} {
                
                ## Error 
                error "Generator class $name was not found, and no package having conventional name $packageName could be found"

            } else {

                ## Research and fail if not found
                set generators [itcl::find classes $generatorName]
                if {[llength $generators]==0} {

                    ## Error
                    error "After loading conventionally named package $packageName, conventional generator $generatorName could not be found "

                } else {

                    return [::new $generatorName #auto $registerFile]
                }
            }
        } else {

            ## Found -> instanciate 
            ############
            return [::new $name #auto $registerFile]
        }
        
        odfi::list::each $test {
        
        }
        
        if {$arg} {  } else { }
        
        foreach {val} $t {
        
        }
        
        foreach v $t {
        
        }

    }
    
    ## This class contains the base informations for a RF modelisation like name etc...
    itcl::class Common {

        odfi::common::classField public name ""

        odfi::common::classField public description ""
        
        odfi::common::classField public parent ""

        constructor {cName} {

            set name $cName
        }

        ## Returns a list of the parents, from top to this (first is top)
        public method parents args {

            set parents {}
            set current $this
            while {$current!=""} {
                lappend parents $current
                set current [$current parent]
            }
            set parents [lreverse $parents]
            return $parents

        }

        #################################
        ## Attributes Interface 
        #################################

        ## Attributes
        public variable attributes {}


        ## name format: attributeGroupName.attributeQualified name 
        ## Example: hardware.rw
        public method hasAttribute qname {

            set components [split $qname .]

            ## Look for attribute 
            set groupName [lindex $components 0]
            set attributeName [join [lrange $components 1 end] .]

            #::puts "Has attribute $groupName $attributeName"

            #puts "Available attributes: $attributes"
            
            set foundAttributes [lsearch -glob -inline $attributes *$groupName]
            if {$foundAttributes!=""} {

                ## Found attributes group, look for attribute 
                if {[$foundAttributes contains $attributeName]} {
                    return 1
                } else {
                    return 0
                }

            } else {
                return 0
            }



        }
        public method attributes {fName closure} {
            ## Create 
            set newAttribute [::new [namespace parent]::Attributes $name.$fName $fName $closure]


            ##puts "Created field: $newField"

            ## Add to list
            lappend attributes $newAttribute 

            ## Return 
            return $newAttribute
        }  

        ## Execute closure on each Attributes, with variable name: $attrs
        public method onEachAttributes closure {

            foreach attrs $attributes {
                odfi::closures::doClosure $closure 1
            }
            #odfi::list::each $attributes {

            #    odfi::closures::doClosure $closure 1


            #}
        }

        ## Execute closure on each Attributes, with variable name: $attrs
        public method onEachAttributes2 closure {

            odfi::list::each $attributes {

               odfi::closures::doClosure $closure 1


            }
        }

    }


}
