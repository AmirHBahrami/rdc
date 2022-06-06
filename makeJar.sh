#!/bin/bash
./clean.sh
./compile.sh
mkdir deployed
cd bin
jar cfe ../deployed/rdc.jar Main *.class
