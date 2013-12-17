import sbt._
import Keys._

object CIlibBuild extends Build {

  val buildSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.0",
    version := "0.9-SNAPSHOT",
    organization := "net.cilib",
    organizationName := "CIRG @ UP",
    organizationHomepage := Some(url("http://cirg.cs.up.ac.za")),
    javacOptions ++= Seq("-encoding", "UTF8", "-source", "1.7", "-target", "1.7"),
    javacOptions in doc := Seq("-encoding", "UTF-8", "-source", "1.7"),
    scalacOptions += "-deprecation",
    publishMavenStyle := true,
    publishSetting,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
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

  lazy val root = Project(id = "cilib",
    base = file("."),
    settings = buildSettings) aggregate(library, simulator) settings (
      headerCheckSetting
    )

  lazy val library = Project(id = "library",
    base = file("library"),
    settings = buildSettings)

  lazy val simulator = Project(id = "simulator",
    base = file("simulator"),
    settings = buildSettings) dependsOn(library)


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
    sources in (library, Compile), sources in (library, Test), sources in (simulator, Compile), sources in (simulator, Test),
    streams) map { (librarySources, libraryTestSources, simulatorSources, simulatorTestSources, s) =>
      val logger = s.log
      updateHeaders(librarySources, logger)
      updateHeaders(simulatorSources, logger)
      updateHeaders(libraryTestSources, logger)
      updateHeaders(simulatorTestSources, logger)
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
