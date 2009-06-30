@REM
@REM Copyright (C) 2003 - 2009
@REM Computational Intelligence Research Group (CIRG@UP)
@REM Department of Computer Science
@REM University of Pretoria
@REM South Africa
@REM
@REM This program is free software; you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as published by
@REM the Free Software Foundation; either version 2 of the License, or
@REM (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with this program; if not, write to the Free Software
@REM Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
@REM
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
java -cp %CLASSPATH% net.sourceforge.cilib.simulator.Main %XML_FILE% -textprogress

@echo on
