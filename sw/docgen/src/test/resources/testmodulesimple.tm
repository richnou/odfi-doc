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
        
        set ls {a b c d {d f e }}
        
        odfi::list::each $test {
        
        }
        
        if {$arg} {  } else { }
        
        foreach {val} $t {
        
        }
        
        foreach v $t {
        
        }

    }
    
  


}
