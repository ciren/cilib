seq(scalariformSettings: _*)

name := "cilib"

version := "0.8-SNAPSHOT"

publishArtifact in packageDoc := false

parallelExecution in Test := false

libraryDependencies ++= Seq(
	"org.scalaz" %% "scalaz-core" % "6.0.4",
	"com.google.guava" % "guava" % "11.0.1",
	"org.parboiled" % "parboiled-core" % "0.11.0",
	"org.parboiled" % "parboiled-java" % "0.11.0",
	"org.functionaljava" % "functionaljava" % "3.1",
	"junit" % "junit" % "4.10" % "test",
	"org.mockito" % "mockito-all" % "1.8.4" % "test",
	"org.hamcrest" % "hamcrest-all" % "1.1" % "test",
	"org.specs2" %% "specs2" % "1.9" % "test",
	"com.novocode" % "junit-interface" % "0.5" % "test",
	"org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

scalacOptions += "-deprecation"
