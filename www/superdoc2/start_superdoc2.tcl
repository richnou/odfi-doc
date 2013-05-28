#!/usr/bin/env tclsh


package require odfi::ewww::webdata


::new odfi::ewww::webdata::WebdataServer server 8567

## Configuration
#######################


## Start Server
########################
server application /superdoc {

    puts "The variable i has the value $i "

    set applicationFolder [file dirname [file normalize [info script]]]/webapp/

    view "" {


        tview [file dirname [file normalize [info script]]]/webapp/base.html

        tview {

            html {
                body {

                    ul {
                        li { "Hi" }

                        li {
                            return "hi"
                        }
                    }

                }



            }


        }

    }

    data "" {



    }



}

server start

vwait forever

scenario("A module can get an input") {

    case ("We add two inputs") {

        var m= new Module()
        m <= input "clock"

        require (m.inputs.size == 1)
    }


}

MOdule => Unit
module "Hello" {

    module =>

        module input "clock"

        module <= input "clock"




    input "clock"

    module (name:"Hello2") {



    }


}
