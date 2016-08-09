set :name duckdoc

:requirements {

    :module tcl/devlib
}


## Module Site extension
#########
:onPostBuild {
    #package require odfi::duckdoc
    #:mixinToModule ::odfi::duckdoc::SiteBuilder duckdoc
}


## Virtual Command
##########

:command serveModules {


    ## Prepare Serving Environment
    #################
    package require odfi::ewww 3.0.0
    package require odfi::duckdoc 1.0.0

    set server [[:getODFI] shade ::odfi::ewww::Server firstChild]
    if {$server != ""} {
        :log:raw "Found WWW Server to deploy DuckDoc to"
    } else {
        :log:raw "No Server Available, creating one"
    }

    ## Serve main Duckdoc application
    #################

    set duckdocApp [$server application duckdoc /duckdoc {

        #:htmlView index / {

         #   :html {
         #       :head {

          #      }   

          #      :body {
           #         :h1 "Welcome to DuckDoc!!"
           #     } 
           # }

        #}

    }]


    ## Get Sites
    ##################
    [:getODFI] @> getAllPhysicalModules @> foreach {
        
        ## Determine Site Folder
        ## Default moduledirectory/site
        ## If attribute is present on module, use it

        set siteFolder [$it directory get]/site

        if {[$it hasAttribute ::odfi::duckdoc siteFolder]} {

            ## If Attribute site folder is not absolute, it is relative to module directory
            set siteFolder [$it getAtttribute ::odfi::duckdoc siteFolder]

            if {[file pathtype $siteFolder]!="absolute"} {

                set siteFolder [$it directory get]/$siteFolder
            }
            
        }

        ## Search site/*.site.tcl file to make sure it is a site folder
        #:log:raw "Searching $siteFolder"
        set siteFiles [glob -nocomplain -type f -directory $siteFolder *site.tcl]

        if {[llength $siteFiles]>0} {

    
            :log:raw "Found Site at $siteFolder"

            ## Create Site
            set moduleSite [::odfi::duckdoc::site $siteFolder]

            ## Save site as child to this command
            :addChild $moduleSite 

            ## Gather Pages
            $moduleSite gather
            

            ## Add SubApp Handler
            $duckdocApp application [$moduleSite siteName get] /[$moduleSite sitePath get] {

                puts "Inside sub application"

                ## What we are serving are registered Templates not the site itself
                ## Go through each template, and generate using a _serve folder, because the generated output is adapted to our local webserver anyway
                [$moduleSite shade ::odfi::duckdoc::Template children] foreach {

                    puts "Init and serve template: [$it name get]"

                    ## Init  Template
                    $it initTemplate $moduleSite

                    ## Change Resource Prefix for serving
                    $it addResourceServingPrefix "/duckdoc/[$moduleSite sitePath get]/[$it name get]/"

                    ## Rewrite output to serving folder
                    $it toHTML [$moduleSite outputFolder get]/_serve/[$it name get]

                    :fileHandler /[$it name get] [$moduleSite outputFolder get]/_serve/[$it name get]  {

                    }
                }
                
            }
            



        } elseif {[file exists $siteFolder]} {

            :log:raw "Site Folder $siteFolder exists but no *site.tcl file was found"

        }

        ## Create Simple basic DuckDoc GUI
        ############
        set cmd [current object]
        $duckdocApp htmlView index / {

           :html {
                :head {

                }   

                :body {
                    :h1 "Welcome to DuckDoc!!"

                    foreach site [odfi::nx::getAllNXObjectsOfType ::odfi::duckdoc::Site] {

                        :ul {
                            :li {
                                :text "Site: [$site siteName get]"
                                :ul {
                                    [$site shade ::odfi::duckdoc::Template children] foreach {
                                        :li {
                                            :text "Template: [$it name get]:"
                                            :a http://localhost:[$server port get]/duckdoc/[$site sitePath get]/[$it name get] http://localhost:[$server port get]/duckdoc/[$site sitePath get]/[$it name get]
                                        }
                                    }
                                }
                            }
                        }

                    }

               } 
           }


        }
        
        ## 
        #$it call "duckdoc.site" {
        #
        #}
    }

    

    return 0

    puts "Serving WebSite for all Modules which have a dued description"
    [:getODFI] @> getAllModules @> foreach {
        
        ## Search site/*.site.tcl file
        puts "Searching [$it directory get]/site"
        
        ## 
        #$it call "duckdoc.site" {
        #
        #}
    }


}

:command serve {


}

:command hello {

    puts "Hello from duckdoc"
}
