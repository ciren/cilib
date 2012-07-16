seq(scalariformSettings: _*)

name := "cilib"

version := "0.8-SNAPSHOT"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

scalacOptions += "-deprecation"

parallelExecution in Test := false

scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2", "2.9.1")//, "2.10.0-M5")

resolvers ++= Seq(
	"snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
	"releases"  at "http://oss.sonatype.org/content/repositories/releases"
)
