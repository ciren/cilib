seq(scalariformSettings: _*)

// name := "library"

description := "A library of composable components enabling simpler Computational Intelligence"

organization := "net.cilib"

organizationName := "CIRG @ UP"

organizationHomepage := Some(url("http://cirg.cs.up.ac.za"))

version := "0.7.5"

publishArtifact in packageDoc := false

publishArtifact in Test := false

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

// Related settings to allow for publishing of maven style artifacts
publishMavenStyle := true

autoScalaLibrary := false // Prevent the scala-library automatically being added to the pom

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
</developers>)

pomPostProcess := { (node: scala.xml.Node) =>
  val rewriteRule =
    new scala.xml.transform.RewriteRule {
      override def transform(n: scala.xml.Node): scala.xml.NodeSeq = {
        val name = n.nameToString(new StringBuilder).toString
        println("scalaVersion:"+scalaVersion.toString)
        if (name == "artifactId") {
          println("found it!!! " + name + " " + n.text)
          // println(n.xmlType())
          // n match {
          //   case <artifactId>{v}</artifactId>  => println("boom: " + v.replace("_2.9.2", ""))
          //   case other => {println("other???"); other}
          // }
          n
          // \ "artifactId" match {
          //   case <artifactId>{v}</artifactId> => { println("Found it!"); <artifactId>v</artifactId> }
          //   case other => other
          // }
          // println("child is artifactId?" + (n \ "artifactId").getClass.getName)
        } else n
      }
    }
  val transformer = new scala.xml.transform.RuleTransformer(rewriteRule)
  transformer.transform(node)(0)
}
