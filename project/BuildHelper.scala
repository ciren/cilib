import sbt._
import Keys._
import sbtbuildinfo._
import BuildInfoKeys._
import scalafix.sbt.ScalafixPlugin.autoImport.scalafixSemanticdb

object BuildHelper {

  private val stdOptions = Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8",         // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature",      // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked"     // Enable additional warnings where generated code depends on assumptions.
  )

  private val std2xOptions = Seq(
    "-language:higherKinds",  // Allow higher-kinded types
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-explaintypes",
    "-Yrangepos",
    "-Xlint:_,-missing-interpolator,-type-parameter-shadow",
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-value-discard"  // Warn when non-Unit expression results are unused
  ) ++ customOptions

  private def optimizerOptions(optimize: Boolean) =
    if (optimize)
      Seq(
        "-opt:l:inline"
      )
    else Seq.empty

  private def propertyFlag(property: String, default: Boolean) =
    sys.props.get(property).map(_.toBoolean).getOrElse(default)

  private def customOptions =
    if (propertyFlag("fatal.warnings", true)) {
      Seq("-Xfatal-warnings")
    } else {
      Seq.empty
    }

  def buildInfoSettings(packageName: String) =
    Seq(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, isSnapshot),
      buildInfoPackage := packageName,
      buildInfoObject := "BuildInfo"
    )

  def extraOptions(scalaVersion: String, optimize: Boolean) =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((0, _)) =>
        Seq(
          "-language:implicitConversions",
          "-Xignore-scala2-macros"
        )
      case Some((2, 13)) =>
        Seq(
          "-Ywarn-unused:params,-implicits"
        ) ++ std2xOptions ++ optimizerOptions(optimize)
      case Some((2, 12)) =>
        Seq(
          "-opt-warnings",
          "-Ywarn-extra-implicit",
          "-Ywarn-unused:_,imports",
          "-Ywarn-unused:imports",
          "-Ypartial-unification",
          "-Yno-adapted-args",
          "-Ywarn-inaccessible",     // Warn about inaccessible types in method signatures.
          "-Ywarn-infer-any",        // Warn when a type argument is inferred to be `Any`.
          "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
          "-Ywarn-nullary-unit",     // Warn when nullary methods return Unit.
          "-Ywarn-unused:params,-implicits",
          "-Xfuture",
          "-Xsource:2.13",
          "-Xmax-classfile-name",
          "242"
        ) ++ std2xOptions ++ optimizerOptions(optimize)
      case Some((2, 11)) =>
        Seq(
          "-Ypartial-unification",
          "-Yno-adapted-args",
          "-Ywarn-inaccessible",     // Warn about inaccessible types in method signatures.
          "-Ywarn-infer-any",        // Warn when a type argument is inferred to be `Any`.
          "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
          "-Ywarn-nullary-unit",     // Warn when nullary methods return Unit.
          "-Xexperimental",
          "-Ywarn-unused-import",
          "-Xfuture",
          "-Xsource:2.13",
          "-Xmax-classfile-name",
          "242"
        ) ++ std2xOptions
      case _ => Seq.empty
    }

  // lazy val crossProjectSettings = Seq(
  //   Compile / unmanagedSourceDirectories ++= {
  //     val platform = crossProjectPlatform.value.identifier
  //     val baseDir  = baseDirectory.value
  //     val scalaVer = scalaVersion.value
  //     val isDot    = isDotty.value

  //     crossPlatformSources(scalaVer, platform, "main", baseDir, isDot)
  //   },
  //   Test / unmanagedSourceDirectories ++= {
  //     val platform = crossProjectPlatform.value.identifier
  //     val baseDir  = baseDirectory.value
  //     val scalaVer = scalaVersion.value
  //     val isDot    = isDotty.value

  //     crossPlatformSources(scalaVer, platform, "test", baseDir, isDot)
  //   }
  // )

  def stdSettings(prjName: String) = Seq(
    name := prjName,
    crossScalaVersions := Seq("2.12.10", "2.13.1"),
    scalaVersion in ThisBuild := crossScalaVersions.value.head,
    scalacOptions := stdOptions ++ extraOptions(scalaVersion.value, optimize = !isSnapshot.value),
    libraryDependencies ++= {
      // if (isDotty.value)
      //   Seq(("com.github.ghik" % "silencer-lib_2.13.1" % "1.6.0" % Provided).withDottyCompat(scalaVersion.value))
      // else
      Seq(
        compilerPlugin(scalafixSemanticdb),
        "com.github.ghik" % "silencer-lib" % "1.4.4" % Provided cross CrossVersion.full,
        compilerPlugin("com.github.ghik" % "silencer-plugin"     % "1.4.4" cross CrossVersion.full),
        compilerPlugin("org.typelevel"   % "kind-projector"      % "0.11.0" cross CrossVersion.full),
        compilerPlugin("com.olegpy"      %% "better-monadic-for" % "0.3.1")
      )
    }
  )

  def welcomeMessage = onLoadMessage := {
    import scala.Console

    def header(text: String): String = s"${Console.RED}$text${Console.RESET}"

    def item(text: String): String    = s"${Console.GREEN}> ${Console.CYAN}$text${Console.RESET}"
    def subItem(text: String): String = s"  ${Console.YELLOW}> ${Console.CYAN}$text${Console.RESET}"

    s"""|${header("  ____ ___ ____      _   _")}
        |${header(" / ___|_ _|  _ \\ ___| \\ | |")}
        |${header("| |    | || |_) / _ \\  \\| |")}
        |${header("| |___ | ||  _ <  __/ |\\  |")}
        |${header(" \\____|___|_| \\_\\___|_| \\_|")}    ${version.value}
        |
        |Useful sbt tasks:
        |${item("build")} - Prepares sources, compiles and runs tests.
        |${item("prepare")} - Prepares sources by applying both scalafix and scalafmt
        |${item("fix")} - Fixes sources files using scalafix
        |${item("fmt")} - Formats source files using scalafmt
        |${item("~compile")} - Compiles all modules (file-watch enabled)
        |${item("test")} - Runs all tests
        |${item("docs/docusaurusCreateSite")} - Generates the website
      """.stripMargin
  }
}
