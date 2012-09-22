import sbt._
import Keys._

object CIlibBuild extends Build {
  lazy val root = Project("cilib", file(".")) aggregate(library, simulator) settings (
    headerCheckSetting
  )

  lazy val library = Project("library", file("library"))

  lazy val simulator = Project(id = "simulator",
                               base = file("simulator")) dependsOn(library)


  // Header task definition
  private val header = """/**            __  __                                                    *
 *     _____ _/ /_/ /_    Computational Intelligence Library (CIlib)     *
 *    / ___/ / / / __ \   (c) CIRG @ UP                                  *
 *   / /__/ / / / /_/ /   http://cilib.net                               *
 *   \___/_/_/_/_.___/                                                   */
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
      IO.append(withHeader, removeCurrentHeader(IO.readLines(file)).mkString("\n"))

      logger.info("Replacing header in: " + file.toString)
      IO.copyFile(withHeader, file)
      IO.delete(withHeader)
    }
  }

  private def alreadyHasHeader(contents: List[String]) =
    contents.zip(header.split("\n")) forall {
      case (a, b) => a == b
    }

  private def removeCurrentHeader(file: List[String]) =
    file.dropWhile { line =>
      line.startsWith("/*") || line.startsWith(" *")
    }
}
