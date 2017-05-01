package duckdoc.quickrun

import org.odfi.wsb.fwapp.Site
import org.odfi.wsb.fwapp.DefaultSiteApp
import org.odfi.wsb.fwapp.framework.FWAppValueBindingView
import org.odfi.wsb.fwapp.views.InlineView
import org.odfi.wsb.fwapp.module.semantic.SemanticView
import org.odfi.indesign.core.harvest.Harvest
import org.odfi.indesign.core.main.IndesignPlatorm
import org.odfi.eda.h2dl.tool.sphinx.SphinxProjectHarvester
import org.odfi.wsb.fwapp.lib.indesign.FWappResourceValueBindingView
import org.odfi.eda.h2dl.tool.sphinx.SphinxProject
import java.io.File
import org.odfi.indesign.core.harvest.Harvester
import org.odfi.eda.h2dl.tool.sphinx.SphinxLiveCompiler
import org.odfi.wsb.fwapp.HarvesterSite
import org.odfi.eda.h2dl.tool.msys.MsysHarvester
import org.odfi.wsb.fwapp.assets.ResourcesIntermediary
import org.odfi.wsb.fwapp.assets.AssetsResolver
import org.odfi.wsb.fwapp.assets.ResourcesAssetSource
import org.odfi.eda.h2dl.tool.msys.MsysInstall
import org.odfi.indesign.ide.core.ui.tasks.TasksView

object DuckDocQuickRun extends DefaultSiteApp("/duckdoc/quickrun") with HarvesterSite {

  this.listen(8554)

  // Use MSYS to be able to call Sphinx on Windows
  requireHarvester(MsysHarvester)
  //IndesignPlatorm use SphinxProjectHarvester

  // Harvest
  // Create Sphinx Projects from config
  //----------------
  override def doHarvest = {

    //-- Gather Projects
    DuckDocQuickRun.config.get.getKeyValues("sphinx", "files").foreach {
      projectPath =>
        var sp = new SphinxProject(new File(projectPath))
        gather(sp)
    }

  }

  Harvest.onHarvestDone {
    println("Updating Python Path")
    this.onResources[SphinxProject] {
      case p =>
        p.getLiveCompiler[SphinxLiveCompiler] match {
          case Some(lc) => lc.putEnvironment("PYTHONPATH", DuckDocQuickRun.config.get.getString("PYTHONPATH", ""))
          case None     =>
        }
    }
  }

  this.onGathered[SphinxProject] {
    p =>

      // Make sure LC is present
      p.buildLiveCompilers

      // Map output
      val index = getResourcesOfType[SphinxProject].indexOf(p)
      //staticServe.addAssetsSource(s"/$index", new ResourcesAssetSource)
      (s"/build/$index/" uses (new ResourcesIntermediary("/"))).addFilesSource(p.getBuildHTMLOutput.getCanonicalPath)
    //.addFilesSource(p.getBuildHTMLOutput.getCanonicalPath)
    //.addFilesSource(p.getBuildHTMLOutput)
  }

  //val staticServe = "doc" uses new AssetsResolver

  // Gui
  //---------
  "/" view new InlineView with SemanticView with FWAppValueBindingView with FWappResourceValueBindingView with TasksView {

    html {
      head {
        placeLibraries
      }

      "ui page container" :: body {

        h1("Welcome to Duckdoc Quick Run") {

        }

        semanticDivider
        h2("Sphinx Projects") {

        }

        // Make sure projects can be build
        //------------
        MsysHarvester.getResource[MsysInstall] match {
          case None =>
            "ui warning message" :: "Cannot find MSYS2, you won't be able to build projects"
            "ui button" :: buttonClick("Install MSYS") {

              // http://repo.msys2.org/distrib/msys2-x86_64-latest.exe
            }
          case Some(msys) =>

            //-- Update Button
            "ui primary button" :: taskButton("odfi.templates.update")("Update ODFI Templates", "Updating ODFI Templates...") {
              task =>

                msys.runBashCommand("pip3 install --upgrade odfi_templates", true)

            }

            //-- Python Path
            form {
              +@("style" -> "margin:10px")
              "ui field" :: div {

                label("PYTHONPATH") {
                  val b = DuckDocQuickRun.config.get.getStringAsBuffer("PYTHONPATH", "")
                  b.onDataUpdate(Harvest.run)
                  inputToBufferAfter500MS(b)
                }

              }
            }

        }

        // Table of Projects
        //------------------------

        text("Projects: " + DuckDocQuickRun.getResourcesOfType[SphinxProject].size)

        "ui  celled  table" :: table {

          thead("", "Name", "Path", "Live Builder")

          DuckDocQuickRun.config.get.getKey("sphinx", "files") match {
            case None =>
              tbody {
                tr {
                  td("No Projects") {
                    colspan(4)
                  }
                }
              }
            case Some(sphinxKey) =>

              tbodyTrLoop(sphinxKey.values) {

                project =>

                  // Delete
                  // Name is hint
                  // Path
                  // Live Compiler stash
                  rtd {
                    "ui delete icon" :: iconClickReload {

                      sphinxKey.values -= project
                      DuckDocQuickRun.saveConfig
                      Harvest.run

                    }
                  }
                  rtd {
                    inputBindAfter500MSInit(project.hint) {
                      str =>
                        project.hint = str
                        DuckDocQuickRun.saveConfig
                    }
                  }
                  td(project) {

                  }
                  rtd {
                    DuckDocQuickRun.getResourcesOfType[SphinxProject].find(_.path.toString == project.toString()) match {
                      case Some(project) =>

                        project.getLiveCompiler[SphinxLiveCompiler] match {
                          case None =>

                            classes("negative")
                            text("Live Compiler Not Online")

                          case Some(lc) =>
                            classes("positive")
                            val index = getResourcesOfType[SphinxProject].indexOf(project)

                            a(s"/build/$index/index.html") {
                              +@("target" -> "_blank")
                              text("Live Compiler Online")
                            }

                        }

                        a(s"#") {
                          text("Rebuild Live Compiler")
                          onClickReload {
                            project.buildInvalidateLiveCompilers
                            project.buildLiveCompilers
                          }
                        }

                      case None =>
                        classes("negative")
                        text("No Project Found")
                    }

                  }

              }

          }

          tfootTrTh {

            form {
              "inline" :: input {
                fieldName("path")
                semanticFieldRequire
              }

              "inline" :: semanticOnSubmitButton("Add") {

                println("Adding")
                var value = DuckDocQuickRun.config.get.getKeyCreate("sphinx", "files").values.add
                value.set(request.get.getURLParameter("path").get)
                DuckDocQuickRun.saveConfig
                Harvest.run

              }
            }

          }

        }

      }
    }

  }

  start

}