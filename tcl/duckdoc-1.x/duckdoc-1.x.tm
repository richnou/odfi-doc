package provide odfi::duckdoc 1.0.0
package require odfi::language::nx 1.0.0
package require odfi::richstream 3.0.0
package require odfi::attributes 2.0.0

source [file dirname [info script]]/markdown.tm

odfi::language::nx::new ::odfi::duckdoc {

    ## Site
    ###############
    :site baseFolder {
        +exportToPublic
        +mixin ::odfi::attributes::AttributesContainer
        
        #+expose name
        +var contentFolder ""
        +var outputFolder ""
        +var siteName "default"
        +var sitePath "/default"
        +var fileMonitor ""
        
        +builder {
        
            ## Defaults
            set :baseFolder [file normalize ${:baseFolder}]
            set :contentFolder ${:baseFolder}
            set :outputFolder ${:baseFolder}/_out
            set :siteName [file tail  [file normalize ${:baseFolder}/../]]
            set :sitePath ${:siteName}
            
            ## Source *site.duckdoc
            foreach siteFile [glob -nocomplain -types f -directory ${:baseFolder} *site.tcl] {
                source  $siteFile
            }
            
            ## File Moniroting
            ## Update file content, remove and add pages
            :registerEventPoint fileModified f
            set :fileMonitor [::odfi::files::newFileMonitor [current object] {} {
                    
                    ## Relative path
                    set relativePath [string map [list [${:target} contentFolder get]/ "" ] $f ]
                    puts "A file has an updated content: $f -> $relativePath -> [split $relativePath /]"
                    
                    ## Find Child
                    set child [[${:target} shade ::odfi::duckdoc::Group firstChild] findInSubTreeAlongPath name [split $relativePath /]]
                    if {$child!=""} {
                        
                        if {![file exists $f]} {
                            $child detach
                        } else {
                            puts "updating content"
                            $child updateContent
                        }
                    }
                    #${:target} findChild
                    
                    ${:target} callFileModified $f
            
            }]
           
            
        }
        
        ####################
        ## Clean
        ####################
        
        
        +method cleanPages args {
            
            ## Detach all top groups
            [:shade ::odfi::duckdoc::Page children] foreach {
                $it detach
            }
            [:shade ::odfi::duckdoc::Group children] foreach {
                $it detach
            }
        
        }
        
        ###################
        ## Run
        ##################
    
        +method startFileMonitorWith eventClosure {
        
            ${:fileMonitor} start 
            
            :onFileModified $eventClosure
        }
        
        ## Get the documents
        +method gather args {
        
            set results {}
            set processList [list ${:contentFolder} [current object]]

            while {[llength $processList]>0} {
                set currentFolder [lindex $processList 0]
                set currentParent [lindex $processList 1]
                set processList [lrange $processList 2 end]
                
                ## Create Group for Folder
                ###########
                if {[string match ${:outputFolder}* $currentFolder]} {
                    continue
                }
                puts "Created Group: $currentFolder"
                set group [$currentParent group $currentFolder ]
                
                
                ## Get next folders
                ####################
                set nextFolders [glob -nocomplain -directory $currentFolder -types d -- *]
                foreach nextFolder $nextFolders {
                    if {![string match "__*" [file tail $nextFolder]]} {
                        lappend processList $nextFolder $group
                    } 
                    
                }
                
                ## Get Docs
                ###################
                set mdDocs [glob -nocomplain -directory $currentFolder -types f -- *.md]
                foreach mdDoc $mdDocs {
                
                    puts "Found: $mdDoc"
                    
                    ${:fileMonitor} addFiles $mdDoc
                    
                    $group markdownFile $mdDoc {
                    
                    }
                }
                
                ## Remove if empty
                #############
                if {[$group shade ::odfi::duckdoc::Page isEmpty]} {
                    puts "Removed Group: $currentFolder"
                    #$group detach
                }
            }
           
        
        
        }
        
        +method produce args {
        
        }
        
        #############################
        ## Page 
        ############################
        +method findPageByPath path {
            
            return [:shade ::odfi::duckdoc::Page findChildByProperty path $path]
            
        }
        
        #############################
        ## MD Doc
        #############################
        
        ###################
        ## Container
        ###################
        +type Named {
            +var name ""
            +var shortName ""
        }
        :group : Named groupFolder {
            +exportTo Group
            
            
            
            +builder {
                
                set :name [file tail ${:groupFolder}]
                set :shortName [file tail ${:groupFolder}]
                
                ## Look for group.duckdoc in folder
                foreach groupScript [glob -nocomplain ${:groupFolder}/[file tail ${:groupFolder}].duckdoc*] {
                    source $groupScript
                }
            
            }
            
            ##################
            ## Pages
            #################
            +type Page : Named  {
                +var pageName ""
                +var path ""
                
            
            }
            
            :markdownFile : Page path  {
                +var content "" 
                +var filePath ""
                
                +builder  {
                    next
                    
                    ## Set Some Defaults
                    ##########
                    set site [:findParentInPrimaryLine { $it isClass ::odfi::duckdoc::Site}]
                    #set site [[:getPrimaryParents] @> at 0]
                    
                    #set :path [odfi::common::relativePath [[:parent] folder get] $path]
                    set :filePath   ${:path}
                    set :name       [file tail ${:path}]
                    set :pageName   [file tail ${:path}]
                    set :shortName  [file tail ${:path}]
                    set :path       [string map [list [[:getSite] contentFolder get] "" ] ${:path} ]
                    
                    #puts "Created MD: ${:path} -> [llength [split ${:path} /]]"
                    
                   
                    :updateContent
                    
                   
                }
                
                +method getSite args {
                    return [:findParentInPrimaryLine { $it isClass ::odfi::duckdoc::Site}]
                }
                
                +method updateContent args {
                
                    ## Read Page Content
                    ##########       
                    set :content [odfi::files::readFileContent ${:filePath}]
                    set :content [odfi::richstream::template::stringToString ${:content}]
                    
                }
                
                +method getPathDepth args {
                    return [expr [llength [split ${:path} /]] - 1 ]
                }
                
                +method isIndex args {
                    if {[string match "*index*" ${:path}]} {
                        return true
                    } else {
                        return false
                    }
                }
                
                +method getHtmlName args {
                    
                    return [string map {md html} ${:path}]
                    
                }
                
                +method toHTML args {
                    
                    ## Read Page Content
                    ##########       
                    #set :content [odfi::files::readFileContent ${:filePath}]
                    #set :content [odfi::richstream::template::stringToString ${:content}]
                    
                    #::puts "COnverting:  ${:content}"
                    return [::Markdown::convert ${:content}]
                }
                
                ###
                ### Content Helpers
                ###
                +method mdLink {link {text ""}} {
                    if {$text==""} {
                        return "\[$link\]($link)"
                    } else {
                        return "\[$text\](\"$link\")"
                    }
                }
                
                +method code text {
                    ::puts "Write code: $text"
                    return "<pre><code>$text</code></pre>"
                }
            
            }
            
        
        }
        
        
        
        
        #############################
        ## template set and get
        ##############################
        
        :template name {
            #+mixin ::odfi::ewww::html::HtmlBuilder
            
            +var templateReady false
            
            +var outputBase ""
            
            +builder {
                package require odfi::ewww::html    2.0.0
                :object mixins add ::odfi::ewww::html::HTMLNode
                :object mixins add ::odfi::ewww::html::HtmlBuilder
                set :outputBase ${:name}
                :registerEventPoint addServePrefix prefix
            }
            
            
            ## Serving
            #####################
            
            +method applyTargetPathPrefix prefix {
                
                set urlRegexp {^\/[\w].*}

                puts "Applying prefix $prefix"
                
                ## Global Scripts and Stylesheets
                :shade ::odfi::ewww::html::Script walkDepthFirstPreorder {
                    
                    #puts "Updating Script Prefix from [$node getAttribute src]"
                    if {[$node getAttribute src]!="" && [regexp $urlRegexp [$node getAttribute src]]} {
                        $node @ src $prefix/[$node getAttribute src]
                    }
                    
                    
                    return true
                }   
                
                :shade ::odfi::ewww::html::Img walkDepthFirstPreorder {
                                    
                    #puts "Updating Script Prefix"
                    if {[$node getAttribute src]!="" && [regexp $urlRegexp [$node getAttribute src]]} {
                        $node @ src $prefix/[$node getAttribute src]
                    }
                    
                    
                    return true
                }   
                
                :shade ::odfi::ewww::html::Stylesheet walkDepthFirstPreorder {
                                    
                    #puts "Updating Script Prefix"
                    if {[regexp $urlRegexp [$node location get]]} {
                        $node location set $prefix/[$node location get]
                    }
                    
                    
                    return true
                }   
                
                
                ## Links 
                :shade ::odfi::ewww::html::A walkDepthFirstPreorder {
                                                    
                    #puts "Updating Script Prefix"
                    if {[regexp $urlRegexp [$node getAttribute href]]} {
                        $node @ href $prefix/[$node getAttribute href]
                    }
                    
                    
                    return true
                } 
                
            }
            
            ## This method is called when serving the Site for testing for example, if the serving path needs an additional prefix
            +method addResourceServingPrefix prefix {
            
                ## Local 
                :callAddServePrefix $prefix
                
                ## Global
                :applyTargetPathPrefix $prefix
                
            }
         
            # Output
            ###############
            +method reInit site {
                if {${:templateReady}} {
                    [:shade ::odfi::ewww::html::HTMLNode children] foreach {
                        $it detach
                    }
                    set :templateReady false
                }
                :initTemplate $site
            
            }
            +method initTemplate site {
            
                if {!${:templateReady}} {
                
                    ## Change output base is this is the only template
                    if {[$site shade ::odfi::duckdoc::Template childCount] == 1} {
                        set :outputBase ""
                    }
                
                    ## Local Init
                    :doInitTemplate $site
                    set :templateReady true
                    
                    ## Set Site target path mapping
                    if {[$site sitePath get]!="/"} {
                        #:applyTargetPathPrefix [$site sitePath get]
                    }
                }
                next
            }
            +method doInitTemplate site {
                error "doInitTemplate should be overriden"
            }
            +method copyResources targetPath {
            
            }
            
            +method toHTML {targetFolder {extraPath ""}} {
                
                puts "HTML Output"
                
                ## Create Output Dir
                #####################
                #set containingFolder [file dirname  $targetFile]
                set targetFolder [file normalize $targetFolder/${:outputBase}/$extraPath]
                file mkdir $targetFolder
                
                ## Output HTML
                #####################
               
                
               
                ## Render Pages
                set site [:parent]
                set template [current object]
                
                ## init if necessary
                $template initTemplate $site 
                
                ## Copy Resources
                $template copyResources $targetFolder
                
                ## Generate
                $site shade ::odfi::duckdoc::Page walkDepthFirstPreorder {
                    
                    ## Render
                    set targetPath $targetFolder/[$node getHtmlName]
                    
                    puts "Found page: [$node info class] -> $targetPath"
                    set rendered [$template renderPage $site $node]
                    
                    ## Target Path
                    file mkdir [file dirname  $targetPath]
                    $rendered toString $targetPath
                    
                    return true
                    
                }
               
                #set htmlNode [:shade ::odfi::ewww::html::HTMLNode firstChild]
                #set htmlNode [:render]     
                #if {$htmlNode==""} {
                #    error "Cannot Produce HTML on template if no HTML node child is present"
                #}
                #$htmlNode toString $targetFile
                
                ## Copy resources
                #################
                return
                $template shade ::odfi::ewww::html::Script walkDepthFirstPreorder {
                    
                    puts "Found Script node: [$node path get]"
                    set sourceFolder [$site outputFolder get]/[file dirname [$node path get]]
                    puts "Source Folder: $sourceFolder"
                    if {[file exists $sourceFolder]} {
                        catch {file delete -force $containingFolder/[file tail $sourceFolder]}
                        file copy -force $sourceFolder $containingFolder
                    }
                }
                
            }
        
        }
        
       +method getTemplate name {
            :shade ::odfi::duckdoc::Template findChildByProperty name $name
       }
        
        
    }

}

odfi::language::nx::new ::odfi::duckdoc::html {

    :markdown : ::odfi::ewww::html::HTMLNode content {
        +exportTo ::odfi::ewww::html::HTMLNode
        
        +builder {
        
            ## Parse Content
            set :content [odfi::richstream::template::stringToString ${:content}]
        }
        
        +method reduceProduce results {
        
            return "<div>[::Markdown::convert ${:content}]</div>"
        
        }
    
    }

}
