@echo off

REM
REM File created by Gareth David, 2005
REM
REM Created for the CIlib Simulator in Windows Batch Script
REM

title CIlib Simulator - Copyright (C) 2003,  2004,  2005, 2006 - CIRG@UP

if "%1" == "-server" goto useServer
set XML_FILE=%1
set JAVA_OPT=
goto noServer

:useServer
set JAVA_OPT=%1
set XML_FILE=%2

:noServer
set BASEDIR=%~p0
set CILIB_JAR=%BASEDIR%${project.build.finalName}.jar;target\${project.build.finalName}.jar

if not "%CLASSPATH%" == "" goto classPathExist
set CLASSPATH=%CILIB_JAR%;%CLASSPATH%
goto runSimulator

:classPathExist
set CLASSPATH=%CLASSPATH%;%CILIB_JAR%

:runSimulator
java %JAVA_OPT% net.sourceforge.cilib.simulator.Main %XML_FILE% -textprogress

@echo on
