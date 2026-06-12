#!/bin/sh

pdflatex -draftmode JavaCodingConventions > /dev/null
pdflatex -draftmode JavaCodingConventions > /dev/null
biber JavaCodingConventions
makeindex -s idxhdr.ist JavaCodingConventions
pdflatex JavaCodingConventions 
thumbpdf JavaCodingConventions > /dev/null
pdflatex JavaCodingConventions 

