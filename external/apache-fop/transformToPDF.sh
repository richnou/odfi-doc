#!/bin/bash

echo "Transforming to PDF"

scriptPath="$(dirname "$(readlink -f ${BASH_SOURCE[0]})")"
$scriptPath/fop-trunk/fop -fo $1 -pdf $2/$3
