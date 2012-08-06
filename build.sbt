seq(scalariformSettings: _*)

name := "cilib"

version := "0.8-SNAPSHOT"

scalacOptions += "-deprecation"

parallelExecution in Test := false

scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2", "2.9.1")

resolvers ++= Seq(
    "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)


seq(netbeans.NetbeansTasks.netbeansSettings:_*)
