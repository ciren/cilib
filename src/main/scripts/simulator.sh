#!/bin/bash

if [ "$1" == "-server" ] ; then 
	JAVA_OPTS=$1
	shift
fi

CLASSPATH="${project.build.finalName}.jar:target/${project.build.finalName}.jar:${CLASSPATH}"

nice java $JAVA_OPTS -Xms1000M -Xmx2000M -cp $CLASSPATH net.sourceforge.cilib.simulator.Main $@ -textprogress
