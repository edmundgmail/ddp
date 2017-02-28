#!/bin/bash

mvn install:install-file -Dfile=./cb2xml.jar -DgroupId=net.sf.cb2xml -DartifactId=cb2xml -Dversion=0.95.8 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=./JRecord.jar -DgroupId=net.sf.JRecord -DartifactId=JRecord -Dversion=0.81 -Dpackaging=jar -DgeneratePom=true
