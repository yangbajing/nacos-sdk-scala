#!/bin/sh

#sbt pekko-boot-docs/paradox
if [ ! -d ../docs/ ]; then mkdir ../docs/; fi;
rm -rf ../docs/*
cp -r ../nacos-docs/target/paradox/site/main/* ../docs/
