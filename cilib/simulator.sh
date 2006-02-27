#!/bin/bash

if [ "$1" == "-server" ] ; then 
	JAVA_OPTS=$1
	shift
fi

CLASSPATH=$CLASSPATH:build/cilib.jar nice java $JAVA_OPTS net.sourceforge.cilib.Simulator.Main $@ &
