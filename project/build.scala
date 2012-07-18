import sbt._

object CIlibBuild extends Build {
  lazy val root = Project(".", file(".")) aggregate(library, simulator)

  lazy val library = Project("cilib-library", file("library"))

  lazy val simulator = Project(id = "cilib-simulator",
                               base = file("simulator")) dependsOn(library)
}
