seq(scalariformSettings: _*)

name := "cilib"

version := "0.8-SNAPSHOT"

publishArtifact in packageDoc := false

parallelExecution in Test := false

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "com.google.guava" % "guava" % "11.0.1",
    "org.parboiled" % "parboiled-core" % "0.11.0",
    "org.parboiled" % "parboiled-java" % "0.11.0",
    "org.functionaljava" % "functionaljava" % "3.1",
    "junit" % "junit" % "4.10" % "test",
    "org.mockito" % "mockito-all" % "1.8.4" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
    "com.novocode" % "junit-interface" % "0.10-M1" % "test"
)

javacOptions ++= Seq("-encoding", "UTF8", "-Xlint:deprecation")

scalacOptions += "-deprecation"

resolvers ++= Seq(
    "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)
