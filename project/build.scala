import sbt._
import Keys._
import sbtrelease._
import ReleaseStateTransformations._
import ReleasePlugin._
import ReleaseKeys._

object CIlibBuild extends Build {

  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.11.1",
    organization := "net.cilib",
    organizationName := "CIRG @ UP",
    organizationHomepage := Some(url("http://cirg.cs.up.ac.za")),
    homepage := Some(url("http://cilib.net")),
    licenses := Seq("BSD-style" -> url("http://opensource.org/licenses/BSD-2-Clause")),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      //"-Yno-predef",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard"),
    publishMavenStyle := true,
    publishSetting,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>http://cilib.net</url>
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
            ("avanwyk", "Andrich van Wyk")
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
  )

  lazy val publishSetting = publishTo <<= (version).apply {
    v =>
      if (v.trim.endsWith("SNAPSHOT"))
        Some(Resolvers.sonatypeSnapshots)
      else Some(Resolvers.sonatypeReleases)
  }

  lazy val noPublish = Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false
  )

  lazy val cilib = Project("cilib", file(".")).
    aggregate(core, example, tests).
    settings(cilibSettings: _*)

  lazy val cilibSettings = Seq(
    name := "cilib-aggregate"
  ) ++ noPublish ++ releaseSettings ++ headerCheckSetting

  // Core

  lazy val core = Project("core", file("core")).
    settings(coreSettings: _*)

  lazy val coreSettings = Seq(
    name := "cilib",
    libraryDependencies ++= Seq(
      "org.scalaz"     %% "scalaz-core"   % "7.1.0-M7",
      "org.spire-math" %% "spire"         % "0.7.5"
    )
  ) ++ buildSettings

  // Examples

  lazy val example = Project("example", file("example")).
    settings(exampleSettings: _*).
    dependsOn(core)

  lazy val exampleSettings = Seq(
    name := "cilib-example"
  ) ++ noPublish

  // Tests

  lazy val tests = Project("tests", file("tests")).
    settings(testsSettings: _*).
    dependsOn(core)

  lazy val testsSettings = Seq(
    name := "cilib-tests",
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
    )
  ) ++ noPublish


  // Header task definition
  private val header = """/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
"""

  val headerCheck = TaskKey[Unit]("update-source-headers")

  val headerCheckSetting = headerCheck <<= (
    sources in (core, Compile), sources in (core, Test), //sources in (simulator, Compile), sources in (simulator, Test),
    streams) map { (librarySources, libraryTestSources, /*simulatorSources, simulatorTestSources,*/ s) =>
      val logger = s.log
      updateHeaders(librarySources, logger)
      //updateHeaders(simulatorSources, logger)
      updateHeaders(libraryTestSources, logger)
      //updateHeaders(simulatorTestSources, logger)
    }

  private final def updateHeaders(xs: Seq[File], logger: Logger) = {
    xs.filterNot(x => alreadyHasHeader(IO.readLines(x))).foreach { file =>
      val withHeader = new File(file.getParent, "withHeader")
      IO.append(withHeader, header)
      IO.append(withHeader, removeCurrentHeader(IO.readLines(file)).mkString("", "\n", "\n"))

      logger.info("Replacing header in: " + file.toString)
      IO.copyFile(withHeader, file)
      IO.delete(withHeader)
    }
  }

  private def alreadyHasHeader(contents: List[String]) =
    contents.zip(header.split("\n")) forall {
      case (a, b) => a == b
    }

  private def removeCurrentHeader(contents: List[String]) =
    contents.dropWhile { line =>
      line.startsWith("/*") || line.startsWith(" *")
    }
}

object Resolvers {
  val sonatypeSnapshots = "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
  val sonatypeReleases = "sonatype releases" at "http://oss.sonatype.org/service/local/staging/deploy/maven2"
}
