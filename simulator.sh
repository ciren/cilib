#!/bin/bash

if [ "$1" == "-server" ] ; then 
	JAVA_OPTS=$1
	shift
fi

BASEDIR="`dirname "$0"`"
CILIBJAR="${BASEDIR}/build/cilib.jar"

[ "$CLASSPATH" != "" ] && CLASSPATH="${CILIBJAR}:${CLASSPATH}" || CLASSPATH="${CILIBJAR}"

nice java $JAVA_OPTS net.sourceforge.cilib.Simulator.Main $@ -textprogress
