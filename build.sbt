seq(scalariformSettings: _*)

version := "0.7.5"

scalacOptions += "-deprecation"

parallelExecution in Test := false

scalaVersion := "2.9.2"

// crossScalaVersions := Seq("2.9.2", "2.10")

resolvers ++= Seq(
    "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)


seq(netbeans.NetbeansTasks.netbeansSettings:_*)
