
set dir [file dirname [file normalize [info script]]]

package ifneeded odfi::duckdoc     1.0.0 [list source [file join $dir duckdoc-1.x/ duckdoc-1.x.tm]]

package ifneeded odfi::duckdoc::templates::odfi 1.0.0 [list source [file join $dir duckdoc-1.x/ templates common-odfi common-odfi.tcl]]