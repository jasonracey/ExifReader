#!/bin/bash

# Uncomment to enable debugging
# export JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

sbt "runMain ExifReader.ExifReaderApp $@"
