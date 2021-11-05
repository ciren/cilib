import sbt._
import Keys._
import sbtbuildinfo.BuildInfoKeys._
import sbtbuildinfo._
import scalafix.sbt.ScalafixPlugin.autoImport._

object BuildHelper {

  def isScala3(version: String): Boolean = version.startsWith("3.")

  private val stdOptions = Seq(
    "-deprecation",  // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8",         // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature",      // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked"     // Enable additional warnings where generated code depends on assumptions.
  ) ++ {
    if (sys.env.contains("CI")) {
      Seq("-Xfatal-warnings")
    } else {
      Nil // to enable Scalafix locally
    }
  }

  private val std2xOptions = Seq(
    "-language:higherKinds",  // Allow higher-kinded types
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-explaintypes",
    "-Yrangepos",
    "-Xlint:_,-missing-interpolator,-type-parameter-shadow",
    "-Ywarn-numeric-widen",   // Warn when numerics are widened.
    "-Ywarn-value-discard"    // Warn when non-Unit expression results are unused
  )

  private def optimizerOptions(optimize: Boolean) =
    if (optimize) Seq("-opt:l:inline")
    else Seq.empty

  def buildInfoSettings(packageName: String) =
    Seq(
      buildInfoKeys    := Seq[BuildInfoKey](organization, moduleName, name, version, scalaVersion, sbtVersion, isSnapshot),
      buildInfoPackage := packageName
    )

  def extraOptions(scalaVersion: String, optimize: Boolean) =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((0, _))  =>
        Seq(
          "-language:implicitConversions",
          "-Xignore-scala2-macros"
        )
      case Some((2, 13)) =>
        Seq(
          "-Ywarn-unused:params,-implicits",
          "-Xlint:-byname-implicit" // https://github.com/scala/bug/issues/12072
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
      case _             => Seq.empty
    }

  def platformSpecificSources( /*platform: String,*/ conf: String, baseDirectory: File)(versions: String*) =
    for {
      platform <- List("shared") //, platform)
      version  <- "scala" :: versions.toList.map("scala-" + _)
      result    = baseDirectory.getParentFile / platform.toLowerCase / "src" / conf / version
      if result.exists
    } yield result

  def crossPlatformSources(scalaVer: String, /*platform: String,*/ conf: String, baseDir: File) = {
    val versions = CrossVersion.partialVersion(scalaVer) match {
      case Some((2, 11)) =>
        List("2.11", "2.11+", "2.11-2.12", "2.x")
      case Some((2, 12)) =>
        List("2.12", "2.11+", "2.12+", "2.11-2.12", "2.12-2.13", "2.x")
      case Some((2, 13)) =>
        List("2.13", "2.11+", "2.12+", "2.13+", "2.12-2.13", "2.x")
      case Some((3, 0))  =>
        List("dotty", "2.11+", "2.12+", "2.13+", "3.x")
      case _             =>
        List()
    }
    platformSpecificSources( /*platform,*/ conf, baseDir)(versions: _*)
  }

  lazy val crossProjectSettings = Seq(
    Compile / unmanagedSourceDirectories ++= {
      crossPlatformSources(
        scalaVersion.value,
        //crossProjectPlatform.value.identifier,
        "main",
        baseDirectory.value
      )
    },
    Test / unmanagedSourceDirectories ++= {
      crossPlatformSources(
        scalaVersion.value,
        //crossProjectPlatform.value.identifier,
        "test",
        baseDirectory.value
      )
    }
  )

  def stdSettings(prjName: String) = Seq(
    name                                   := prjName,
    crossScalaVersions                     := Seq("2.13.6", "2.12.15"),      // "3.0.0"),
    scalaVersion                           := crossScalaVersions.value.head,
    scalacOptions                          := stdOptions ++ extraOptions(scalaVersion.value, optimize = !isSnapshot.value),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) =>
          Seq(
            //"com.github.ghik" % s"silencer-lib_$Scala213" % Version.SilencerVersion % Provided
          )
        case _            =>
          Seq(
            "com.github.ghik" % "silencer-lib" % Version.SilencerVersion % Provided cross CrossVersion.full,
            compilerPlugin("com.github.ghik" % "silencer-plugin" % Version.SilencerVersion cross CrossVersion.full),
            compilerPlugin("org.typelevel"  %% "kind-projector"  % "0.13.2" cross CrossVersion.full)
          )
      }
    },
    testFrameworks                         := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    Test / parallelExecution               := true,
    semanticdbEnabled                      := !isScala3(scalaVersion.value), // enable SemanticDB
    semanticdbOptions += "-P:semanticdb:synthetics:on",
    semanticdbVersion                      := scalafixSemanticdb.revision,   // use Scalafix compatible version
    ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value),
    ThisBuild / scalafixDependencies ++= List(
      "com.github.liancheng" %% "organize-imports" % "0.5.0",
      "com.github.vovapolu"  %% "scaluzzi"         % "0.1.20"
    ),
    incOptions ~= (_.withLogRecompileOnMacro(false))
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
        |${item("build")} - Prepare and fix sources, compile and run tests.
        |${item("fix")} - Fixes files using scalafix and scalafmt
        |${item("~compile")} - Compiles all modules (file-watch enabled)
        |${item("test")} - Runs all tests
        |${item("docs/docusaurusCreateSite")} - Generates the website
      """.stripMargin
  }
}
