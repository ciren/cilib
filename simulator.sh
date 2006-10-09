#!/bin/bash

if [ "$1" == "-server" ] ; then 
	JAVA_OPTS=$1
	shift
fi

BASEDIR="`dirname "$0"`"
CILIBJAR="${BASEDIR}/build/cilib.jar"

# Handle all the files in the lib directory and add to the classpath
for jar in lib/*; do
	CLASSPATH="${jar}:${CLASSPATH}"
done

[ "$CLASSPATH" != "" ] && CLASSPATH="${CILIBJAR}:${CLASSPATH}" || CLASSPATH="${CILIBJAR}"

nice java $JAVA_OPTS net.sourceforge.cilib.simulator.Main $@ -textprogress
