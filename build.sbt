seq(scalariformSettings: _*)

scalacOptions += "-deprecation"

parallelExecution in Test := false

resolvers ++= Seq(
    "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases"  at "http://oss.sonatype.org/content/repositories/releases"
)
