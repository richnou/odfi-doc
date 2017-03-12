package provide odfi::duckdoc::templates::odfi 1.0.0 
package require odfi::duckdoc 1.0.0

odfi::language::nx::new ::odfi::duckdoc::templates::odfi {

    :common : ::odfi::duckdoc::Template name   {
        +exportTo ::odfi::duckdoc::Site odfi
        
        
        ## Location
        +var loc "[file dirname [info script]]"
        
        
        ## Configurable elements
        +var topHtml ""
        +var titleTextElement ""
        +var contentDiv ""
        +var contentTextElement ""
        
        
        +method copyResources targetPath {
            next
            ## Copy Resources
            ###############
            set targetResourcesFolder $targetPath/
            puts "Copying resources in: $targetResourcesFolder to $targetPath"
            odfi::files::mcopy from ${:loc} files {semantic jquery highlight.js common-odfi.js common-odfi.css} to $targetResourcesFolder
        }
        +method doInitTemplate site {
            
       
            
            ## Prepare elements
            ###############
            set :topHtml [:html]
            set :titleTextElement [:text "template" {
                                   
            }]
            set :contentTextElement [:text "" {
            }]
            
            
            
      
            
            ## Create Template
            ###################
            set template [current object]
            ${:topHtml} apply {
                :head {
                    :title {
                        :addChild ${:titleTextElement}
                    }
                    :script /jquery/jquery.min.js
                    
                    :script /semantic/semantic.min.js
                    :stylesheet /semantic/semantic.min.css
                    
                    :stylesheet /highlight.js/styles/default.css
                    :script /highlight.js/highlight.pack.js
                    :script "" {
                        :text "hljs.initHighlightingOnLoad();"
                    }
                    
                    :script /common-odfi.js
                    :stylesheet /common-odfi.css
                }
                :body {
                
                    ## Github ribbon
                    if {[$site hasAttribute ::odfi::common github]} {
                        :a "" [$site getAttribute ::odfi::common github] {
                            :img "https://camo.githubusercontent.com/e7bbb0521b397edbd5fe43e7f760759336b5e05f/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f677265656e5f3030373230302e706e67" {
                                :@ style "position: absolute; top: 0; right: 0; border: 0;"
                                :@ alt "Fork me on Github"
                                :data canonical-src "https://s3.amazonaws.com/github/ribbons/forkme_right_green_007200.png"
                            }
                        }
                    }
                
                    ## Menu
                    :div {
                        :classes "ui menu" 
                        
                        
                        
                        ## Serve Header
                        set menuDiv [current object]
                        $template onAddServePrefix {
                            set menuDiv <% $menuDiv %>
                            puts "Adding serving menu: $prefix to $menuDiv"
                            $menuDiv button "Regenerate" {
                                :onClick "\$.get('$prefix/serve-actions/regenerate')"
                            }
                            $menuDiv button "Stop" {
                                :onClick "\$.get('$prefix/serve-actions/stop')"
                            }
                            #exit
                        }
                        
                        
                        ## Menu Header
                        :div {
                            :classes "header item"  
                            :text "[$site siteName get]"
                        }
                        
                        ## Main Pages
                        set topContainer [$site shade ::odfi::duckdoc::Group firstChild]
                        #$topContainer walk
                        set containerClosure {
                        
                            puts "Doing element: $it"
                            if {[$it isClass ::odfi::duckdoc::Page]} {
                                :div {
                                    :classes "item"
                                    :a [$it shortName get] [$it getHtmlName]
                                   # :text [$it shortName get]
                                }
                            } elseif {[$it isClass ::odfi::duckdoc::Group]} {
                            
                                ## Create Sub Menu
                                :div {
                                    :classes "ui dropdown simple item"
                                    :text [$it shortName get]
                                    
                                    :div {
                                        :classes "menu"
                                        
                                        #set group $it 
                                        $it eachChild $lambda
                                       
                                    }
                                }
                                
                            }
                            
                        }
                        odfi::closures::withITCLLambda $containerClosure  0 {
                            $topContainer  eachChild $lambda
                        }
                        #set CCLambda  [odfi::closures::withITCLLambda $cl 0 $containerClosure]
                        #$topContainer  eachChild $CCLambda
                    }
                    
                    ## Body 
                    set :contentDiv [:div {
                      :@ id content 
                      
                      ## Page
                      #set indexPage [$template @> parent @> findPageByPath /index.md]
                      #puts "FOUND Page: $indexPage"
                      #if {$indexPage!=""} {
                      #  :h1 [$indexPage pageName get] {
                      #  }
                      #}
                      
                      :addChild ${:contentTextElement}
                      #set :contentTextElement [:text "" {
                      #}]
                      
                    }]
                }
            }
        
        }
        
        +method renderPage {site page} {
             
            #:resourcePath /semantic
            set template [current object]
            
            #puts "TTE: ${:titleTextElement} -> [$page info class]"
            ${:titleTextElement} content set "[$site siteName get] :: [$page pageName get]"
            
            set pageContent [$page toHTML]
            #puts "Page content: $pageContent"
            
            ${:contentTextElement} content set "[$page toHTML]"
            
            #puts "Page content: [${:contentTextElement} content get]"
            
            return ${:topHtml}
        }
        
        
    
    }
}


return 
odfi::duckdoc::site commonsite {


    :template odfi-common {
        
        
        :html {
            :head {
                :title {
                    :text "Test" {
                    }
                }
                :script semantic/semantic.min.js
                :stylesheet semantic/semantic.min.css
            }
            :body {
               
               ## Menu
               :div {
               
               }
               
               ## Body 
               :div {
                 :@ id content 
               }
               
            }
        }
       
    
    }


}

if {[odfi::common::isTopScript]} {
 
    $commonsite @> getTemplate default @> toHTML [file dirname [info script]]/default_template.html
 

}