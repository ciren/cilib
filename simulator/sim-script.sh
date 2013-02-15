#!/bin/bash
 
# This script expects a few environment variables:
# $JAVA_OPTS: This variable represents the options passed directly to the JVM
# Consult the JVM man page for these options.
# $CILIB_ASSEMBLY: Path to the assembled simulator jar file, which includes
# the cilib library and other required libraries
 
java ${JAVA_OPTS} -jar ${CILIB_ASSEMBLY} $@
