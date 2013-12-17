import AssemblyKeys._

assemblySettings

name := "cilib-simulator"

description := "Simulator environment fo running experiments using the CIlib library"

parallelExecution in Test := false

test in assembly := {}

mainClass := Some("net.sourceforge.cilib.simulator.Main")

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.10" % "test",
    "com.novocode" % "junit-interface" % "0.10-M4" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test"
)

autoScalaLibrary := false

resourceDirectory in Test <<= baseDirectory { _ / "simulator" }

