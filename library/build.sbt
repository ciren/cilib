seq(scalariformSettings: _*)

name := "cilib-library"

description := "A library of composable components enabling simpler Computational Intelligence"

organization := "net.cilib"

organizationName := "CIRG @ UP"

organizationHomepage := Some(url("http://cirg.cs.up.ac.za"))

publishArtifact in Test := false

parallelExecution in Test := false

libraryDependencies ++= Seq(
    "com.google.guava" % "guava" % "11.0.1",
    "org.parboiled" % "parboiled-core" % "0.11.0",
    "org.parboiled" % "parboiled-java" % "0.11.0",
    "org.functionaljava" % "functionaljava" % "3.1",
    "junit" % "junit" % "4.10" % "test",
    "org.mockito" % "mockito-all" % "1.8.4" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
    "com.novocode" % "junit-interface" % "0.10-M4" % "test"
)

javacOptions ++= Seq("-encoding", "UTF8", "-source", "1.7", "-target", "1.7")

javacOptions in doc := Seq("-source", "1.7")

scalacOptions += "-deprecation"

crossPaths := false

// Related settings to allow for publishing of maven style artifacts
publishMavenStyle := true

//autoScalaLibrary := false // Prevent the scala-library automatically being added to the pom

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
<url>http://cilib.net</url>
<licenses>
  <license>
    <name>LGPL</name>
    <url>http://www.gnu.org/licenses/lgpl.html</url>
    <distribution>repo</distribution>
  </license>
</licenses>
<scm>
  <url>git@github.com:cilib/cilib.git</url>
  <connection>scm:git:git@github.com:cilib/cilib.git</connection>
</scm>
<developers>
  <developer>
    <id>gpampara</id>
    <name>Gary Pamparà</name>
    <url>http://gpampara.github.com</url>
  </developer>
  <developer>
    <id>filinep</id>
    <name>Filipe Nepomuceno</name>
    <url>http://github.com/filinep</url>
  </developer>
  <developer>
    <id>benniel</id>
    <name>Bennie Leonard</name>
    <url>http://github.com/benniel</url>
  </developer>
  <developer>
    <id>avanwyk</id>
    <name>Andrich van Wyk</name>
    <url>http://github.com/avanwyk</url>
  </developer>
  <developer>
    <id>kgeorgieva</id>
    <name>Kristina Georgieva</name>
    <url>http://github.com/kgeorgieva</url>
  </developer>
</developers>)

