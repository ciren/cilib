import sbt._

object CIlibBuild extends Build {
  lazy val root = Project("cilib", file(".")) aggregate(library, simulator)

  lazy val library = Project("library", file("library"))

  lazy val simulator = Project(id = "simulator",
                               base = file("simulator")) dependsOn(library)
}
