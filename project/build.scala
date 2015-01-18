import sbt._
import Keys._

import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._

import com.typesafe.sbt.pgp._

object CIlibBuild extends Build {

  // Release step

  lazy val publishSignedArtifacts = ReleaseStep(
    action = st => {
      val extracted = st.extract
      val ref = extracted.get(thisProjectRef)
      extracted.runAggregated(PgpKeys.publishSigned in Global in ref, st)
    },
    check = st => {
      // getPublishTo fails if no publish repository is set up.
      val ex = st.extract
      val ref = ex.get(thisProjectRef)
      Classpaths.getPublishTo(ex.get(publishTo in Global in ref))
      st
    },
    enableCrossBuild = true
  )

  lazy val noPublish = Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false
  )

  // Settings

  override lazy val settings = super.settings ++ Seq(
    scalaVersion         := "2.11.2",
    crossScalaVersions   := Seq("2.10.4", "2.11.2"),
    organization         := "net.cilib",
    organizationName     := "CIRG @ UP",
    organizationHomepage := Some(url("http://cirg.cs.up.ac.za")),
    homepage             := Some(url("http://cilib.net")),
    licenses             := Seq("BSD-style" -> url("http://opensource.org/licenses/BSD-2-Clause")),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      //"-Xfatal-warnings",
      "-Xfuture",
      "-Xlint",
      //"-Yno-imports",
      "-Yno-predef",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused-import"),

    publishMavenStyle    := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },

    publishTo <<= (version).apply {
      v =>
        Some(if (v.trim.endsWith("SNAPSHOT"))
          Resolvers.sonatypeSnapshots
        else Resolvers.sonatypeReleases)
    },

    pomExtra := (
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

  // Main

  lazy val cilib = Project("cilib", file(".")).
    aggregate(core, example, tests).
    settings(cilibSettings: _*)

  lazy val cilibSettings = Seq(
    name := "cilib-aggregate"
  ) ++ noPublish ++ headerCheckSetting ++ releaseSettings ++ Seq(
    publishArtifactsAction := PgpKeys.publishSigned.value,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishSignedArtifacts,
      setNextVersion,
      commitNextVersion
//      pushChanges
    )
  )

  // Core

  lazy val core = Project("core", file("core")).
    settings(coreSettings: _*)

  lazy val coreSettings = Seq(
    name := "cilib",
    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= Seq(
      "org.scalaz"                  %% "scalaz-core"   % "7.1.0",
      "org.spire-math"              %% "spire"         % "0.9.0",
      "com.github.julien-truffaut"  %% "monocle-core"  % "1.0.1"
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
      "org.scalacheck" %% "scalacheck" % "1.12.1" % "test"
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
  val sonatypeSnapshots = "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val sonatypeReleases = "sonatype releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
}
