set :name duckdoc

:requirements {

    :module tcl/devlib
}


## Module Site extension
#########
:onPostBuild {
    package require odfi::duckdoc
    #:mixinToModule ::odfi::duckdoc::SiteBuilder duckdoc
}


## Virtual Command
##########

:command serveModules {

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