#!/bin/bash

if [ "$1" == "-server" ] ; then 
	JAVA_OPTS=$1
	shift
fi

BASEDIR="`dirname "$0"`"

# Obtain the CILib jar file. It's either in the 'target' directory or in '.'
echo  -n "Locating the CILib jar file... "
CILIBJAR=$(find . -maxdepth 3 -iname "cilib*.jar")
echo ${CILIBJAR}

[ "$CLASSPATH" != "" ] && CLASSPATH="${CILIBJAR}:${CLASSPATH}" || CLASSPATH="${CILIBJAR}"

echo "Built classpath: ${CLASSPATH}"

nice java $JAVA_OPTS -Xms1000M -Xmx2000M net.sourceforge.cilib.simulator.Main $@ -textprogress
