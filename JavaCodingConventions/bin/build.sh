#!/bin/bash

BIBER="/usr/bin/biber"
LATEX="/usr/bin/pdflatex"
MKIDX="/usr/bin/makeindex"
THUMB="/usr/bin/thumbpdf"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOC_HOME=$(realpath "$SCRIPT_DIR/../src")
MAIN_DOC="$DOC_HOME/JavaCodingConventions"

cd "$DOC_HOME" || exit 1

#pdflatex -draftmode JavaCodingConventions > /dev/null
echo 1. Cycle
$LATEX -draftmode $MAIN_DOC 
#> /dev/null

#pdflatex -draftmode JavaCodingConventions > /dev/null
echo 2. Cycle
$LATEX -draftmode $MAIN_DOC 
#> /dev/null

#biber JavaCodingConventions
echo Running Biber
$BIBER $MAIN_DOC

#makeindex -s idxhdr.ist JavaCodingConventions
echo Building the index
#$MKIDX -s idxhdr.ist $MAIN_DOC

#pdflatex JavaCodingConventions
echo 3. Cycle
#$LATEX $MAIN_DOC

#thumbpdf JavaCodingConventions > /dev/null
echo Creating thumbnails
#$THUMB $MAIN_DOC > /dev/null 

#pdflatex JavaCodingConventions 
echo Final Cycle
#$LATEX $MAIN_DOC
echo Done!
