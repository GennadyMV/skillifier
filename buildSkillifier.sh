#!/bin/bash
#Script to build skillifier application. Requires git and maven

git submodule init
git submodule update
mvn clean install
