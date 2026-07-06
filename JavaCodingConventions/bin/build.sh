#!/bin/bash

PROJECT_NAME="JavaCodingConventions"
BIBER="/usr/bin/biber --quiet --sortcase=false"
LATEX="/usr/bin/pdflatex -halt-on-error -interaction=batchmode"
MKIDX="/usr/bin/makeindex -q"
THUMB="/usr/bin/thumbpdf --quiet --noverbose"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOC_HOME=$(realpath "$SCRIPT_DIR/../src")
TEXMFOUTPUT="$DOC_HOME"
MAIN_DOC="$DOC_HOME/$PROJECT_NAME"

cd "$DOC_HOME" || exit 1

echo 1. Cycle
$LATEX -draftmode $MAIN_DOC || exit 1 

echo 2. Cycle
$LATEX -draftmode $MAIN_DOC || exit 1 

echo Running Biber
$BIBER $MAIN_DOC || exit 1

# echo Building the index
# $MKIDX -s JavaCodingConventions.ist -o ${PROJECT_NAME}.ind ${PROJECT_NAME}.idx || exit 1

echo 3. Cycle
$LATEX $MAIN_DOC || exit 1

echo Creating thumbnails
$THUMB $MAIN_DOC || exit 1 

echo Final Cycle
$LATEX $MAIN_DOC || exit 1

echo Done!
