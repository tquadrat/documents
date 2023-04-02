#!/bin/sh

pdflatex -draftmode JavaCodingConventions > /dev/nul
pdflatex -draftmode JavaCodingConventions > /dev/nul
biber JavaCodingConventions
makeindex -s idxhdr.ist JavaCodingConventions
pdflatex JavaCodingConventions 
thumbpdf JavaCodingConventions
pdflatex JavaCodingConventions 

