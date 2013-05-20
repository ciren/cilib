seq(scalariformSettings: _*)

name := "cilib-library"

description := "A library of composable components enabling simpler Computational Intelligence"

organization := "net.cilib"

organizationName := "CIRG @ UP"

organizationHomepage := Some(url("http://cirg.cs.up.ac.za"))

publishArtifact in Test := false

parallelExecution in Test := false

libraryDependencies ++= Seq(
    "com.google.guava" % "guava" % "13.0.1",
    "org.parboiled" % "parboiled-core" % "1.1.4",
    "org.parboiled" % "parboiled-java" % "1.1.4",
    "org.functionaljava" % "functionaljava" % "3.1",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
    "com.novocode" % "junit-interface" % "0.10-M4" % "test"
)

javacOptions ++= Seq("-encoding", "UTF8", "-source", "1.7", "-target", "1.7")

javacOptions in doc := Seq("-encoding", "UTF-8", "-source", "1.7")

scalacOptions += "-deprecation"

//testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

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
  {
    Seq(
      ("gpampara", "Gary Pamparà"),
      ("filinep", "Filipe Nepomuceno"),
      ("benniel", "Bennie Leonard"),
      ("avanwyk", "Andrich van Wyk"),
      ("kgeorgieva", "Kristina Georgieva")
    ).map {
      case (id, name) =>
        <developer>
          <id>{id}</id>
          <name>{name}</name>
          <url>http://github.com/{id}</url>
        </developer>
    }
  }
</developers>)

