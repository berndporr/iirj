#!/bin/sh
mvn javadoc:javadoc
cd docs
git add .
