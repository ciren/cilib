#!/bin/bash

if [ "$1" == "-server" ] ; then 
	JAVA_OPTS=$1
	shift
fi

BASEDIR="`dirname "$0"`"
declare -a CILIBJAR
if [ -e ${BASEDIR}/target/cilib-1.0-SNAPSHOT.jar ]; then
	CILIBJAR=${BASEDIR}/target/cilib-1.0-SNAPSHOT.jar
elif [ -e ${BASEDIR}/cilib-1.0-SNAPSHOT.jar ]; then
	CILIBJAR=${BASEDIR}/cilib-*.jar
fi

#CILIBJAR="${BASEDIR}/target/cilib-1.0-SNAPSHOT.jar"

# Handle all the files in the lib directory and add to the classpath
for jar in lib/*; do
	CLASSPATH="${jar}:${CLASSPATH}"
done

[ "$CLASSPATH" != "" ] && CLASSPATH="${CILIBJAR}:${CLASSPATH}" || CLASSPATH="${CILIBJAR}"

echo $CILIBJAR

nice java $JAVA_OPTS -Xms1000M -Xmx2000M net.sourceforge.cilib.simulator.Main $@ -textprogress
