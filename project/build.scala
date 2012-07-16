import sbt._

object CIlibBuild extends Build {
  lazy val root = Project(".", file(".")) aggregate(core, simulator)

  lazy val core = Project("cilib-library", file("library"))

  lazy val simulator = Project(id = "cilib-simulator",
                               base = file("simulator")) dependsOn(core)
}
