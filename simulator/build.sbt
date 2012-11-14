import AssemblyKeys._

assemblySettings

// name := "simulator"

version := "0.7.5"

description := "Simulator environment fo running experiments using the CIlib library"

scalacOptions += "-deprecation"

parallelExecution in Test := false

scalaVersion := "2.9.2"

mainClass := Some("net.sourceforge.cilib.simulator.Main")

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.10" % "test",
    "com.novocode" % "junit-interface" % "0.5" % "test",
    "org.mockito" % "mockito-all" % "1.8.4" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test"
)

// Handle the scala compiler dependency
libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    deps :+ ("org.scala-lang" % "scala-compiler" % sv)
}

resourceDirectory in Test <<= baseDirectory { _ / "simulator" }


seq(netbeans.NetbeansTasks.netbeansSettings:_*)
