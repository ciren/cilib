import sbt._
import sbt.Keys._
import sbtrelease.ReleaseStateTransformations._

val scalazVersion     = "7.3.2"
val spireVersion      = "0.13.0"
val monocleVersion    = "1.5.0"
val scalacheckVersion = "1.14.0"
val parquet4sVersion  = "1.3.1"

val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion

// Library requreid for the SBT build itself
libraryDependencies += "org.scalameta" %% "mdoc" % "2.1.0"

//val previousArtifactVersion = SettingKey[String]("previous-tagged-version")

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  List(
    organization := "ciren",
    homepage := Some(url("https://zio.dev")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "gpampara",
        "Gary Pamparà",
        "",
        url("http://gpampara.github.io")
      )
    ),
    // pgpPassphrase := sys.env.get("PGP_PASSWORD").map(_.toArray),
    // pgpPublicRing := file("/tmp/public.asc"),
    // pgpSecretRing := file("/tmp/secret.asc"),
    scmInfo := Some(
      ScmInfo(url("https://github.com/ciren/cilib/"), "scm:git:git@github.com:ciren/cilib.git")
    )
  )
)

addCommandAlias("build", "prepare; test")
addCommandAlias("prepare", "fix; fmt")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all root/scalafmtSbt root/scalafmtAll")

lazy val mdoc         = taskKey[Unit]("Process the mdoc files")
lazy val websiteWatch = taskKey[Unit]("Watch website files and reload")
lazy val buildWebsite = taskKey[Unit]("Build website")

//   initialCommands in console := """
//     |import scalaz._
//     |import Scalaz._
//     |import cilib._
//     |import spire.implicits._
//     |""".stripMargin
//   // MiMa related
//   /*previousArtifactVersion := { // Can this be done nicer/safer?
//     import org.eclipse.jgit._
//     import org.eclipse.jgit.api._
//     import org.eclipse.jgit.lib.Constants
//     import scala.collection.JavaConverters._

//     val git = Git.open(new java.io.File("."))
//     val tags = git.tagList.call().asScala
//     val current = git.getRepository.resolve(Constants.HEAD)

//     val lastTag = tags.lift(tags.size - 1)
//     val name =
//       lastTag.flatMap(last => {
//         if (last.getObjectId.getName == current.getName) tags.lift(tags.size - 2).map(_.getName)
//         else Some(last.getName)
//       })

//     name.getOrElse("NO_TAG").replace("refs/tags/v", "")
//   },
//   mimaPreviousArtifacts := Set(organization.value %% moduleName.value % previousArtifactVersion.value)*/
// )

// lazy val publishSettings = Seq(
//   organizationHomepage := Some(url("https://github.com/cirg-up")),
//   homepage := Some(url("https://cilib.net")),
//   licenses := Seq("Apache-2.0" -> url("https://opensource.org/licenses/Apache-2.0")),
//   autoAPIMappings := true,
//   apiURL := Some(url("https://cilib.net/api/")),
//   publishMavenStyle := true,
//   //publishArtifact in packageDoc := false,
//   publishArtifact in Test := false,
//   pomIncludeRepository := { _ => false },
//   publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
//   //   {
//   //   val nexus = "https://oss.sonatype.org/"
//   //   if (isSnapshot.value)
//   //     Some("snapshots" at nexus + "content/repositories/snapshots")
//   //   else
//   //     Some("releases" at nexus + "service/local/staging/deploy/maven2")
//   // },
//   pomExtra := (
//     <developers>
//       {
//         Seq(
//           ("gpampara", "Gary Pamparà"),
//           ("filinep", "Filipe Nepomuceno"),
//           ("benniel", "Bennie Leonard")
//         ).map {
//           case (id, name) =>
//             <developer>
//               <id>{id}</id>
//               <name>{name}</name>
//               <url>https://github.com/{id}</url>
//             </developer>
//         }
//       }
//     </developers>
//   )
// )

// lazy val cilibSettings =
//   commonSettings ++
//     publishSettings ++
//     credentialSettings

lazy val root = project
  .in(file("."))
  .settings(
    skip in publish := true,
    console := (console in Compile in core).value,
    BuildHelper.welcomeMessage
  )
  .aggregate(
    core,
    pso,
    eda,
    ga,
    de,
    tests,
    example,
    exec,
    io,
    moo
  )

// lazy val cilib = project
//   .in(file("."))
//   .enablePlugins(
//     //GitVersioning,
//     ReleasePlugin,
//     ScalaUnidocPlugin)
//   .settings(commonSettings ++ credentialSettings ++ noPublishSettings ++ Seq(
//     //git.useGitDescribe := true,
//     releaseProcess := Seq[ReleaseStep](
//       checkSnapshotDependencies,
// //      runClean,
// //      runTest,
//       releaseStepCommand("publishSigned"),
//       releaseStepCommand("sonatypeReleaseAll")
//     )
//   ))

lazy val core = project
  .in(file("core"))
  .settings(BuildHelper.stdSettings("core"))
  //.settings(crossProjectSettings
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      scalaz,
      "org.spire-math"             %% "spire"        % spireVersion,
      "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
      "eu.timepit"                 %% "refined"      % "0.9.15"
    )
  )
  .enablePlugins(BuildInfoPlugin)

//       wartremoverErrors in (Compile, compile) ++= Seq(
//         //Wart.Any,
//         Wart.AnyVal,
//         Wart.ArrayEquals,
//         Wart.AsInstanceOf,
//         Wart.DefaultArguments,
//         Wart.ExplicitImplicitTypes,
//         Wart.Enumeration,
//         //Wart.Equals,
//         Wart.FinalCaseClass,
//         Wart.FinalVal,
//         Wart.ImplicitConversion,
//         Wart.ImplicitParameter,
//         Wart.IsInstanceOf,
//         Wart.JavaConversions,
//         Wart.JavaSerializable,
//         Wart.LeakingSealed,
//         Wart.MutableDataStructures,
//         Wart.NonUnitStatements,
//         //Wart.Nothing,
//         Wart.Null,
//         Wart.Option2Iterable,
//         Wart.OptionPartial,
//         Wart.Overloading,
//         Wart.Product,
//         Wart.PublicInference,
//         Wart.Return,
//         //Wart.Recursion,
//         Wart.Serializable,
//         Wart.StringPlusAny,
//         Wart.Throw,
//         Wart.ToString,
//         Wart.TraversableOps,
//         Wart.TryPartial,
//         Wart.Var,
//         Wart.While
//       )
//     ))

// val mdocVariableMap =
//   Map(
//     "CILIB_VERSION" -> "2.0.1"
//   )
// val mdocInFile = new java.io.File("docs")
// val mdocOutFile = new java.io.File("website/docs")
// val mdocArgs = List("--no-livereload", "--exclude", "target")

// lazy val docs = project
//   .in(file("docs"))
//   .settings(
//     moduleName := "cilib-docs",
//     connectInput in run := true,
//     mdoc := {
//       val classpath = (Compile / dependencyClasspath).value

//       // build arguments for mdoc
//       val settings = _root_.mdoc.MainSettings()
//         .withSiteVariables(mdocVariableMap)
//         .withArgs(mdocArgs)
//         .withOut(new java.io.File("target/mdoc").asPath)
//         .withClasspath(classpath.map(_.data).mkString(":"))

//       // process the mdoc files to the correct location
//       val exitCode = _root_.mdoc.Main.process(settings)

//       if (exitCode != 0) sys.exit(exitCode)
//     },
//     buildWebsite := {
//       import scala.sys.process._

//       // Generate the mdoc sources
//       val classpath = (Compile / dependencyClasspath).value

//       // build arguments for mdoc
//       val settings = _root_.mdoc.MainSettings()
//         .withSiteVariables(mdocVariableMap)
//         .withArgs(mdocArgs)
//         .withOut(mdocOutFile.asPath)
//         .withClasspath(classpath.map(_.data).mkString(":"))

//       // process the mdoc files to the correct location
//       _root_.mdoc.Main.process(settings)

//       Process(Seq("yarn", "install"), new java.io.File("website")).!
//       Process(Seq("yarn", "build"), new java.io.File("website")).!
//     },
//     websiteWatch := {
//       import scala.sys.process._

//       val yarnProcess = Process(Seq("yarn", "start"), new java.io.File("website")).run
//       val classpath = (Compile / dependencyClasspath).value

//       // build arguments for mdoc
//       val settings = _root_.mdoc.MainSettings()
//         .withSiteVariables(mdocVariableMap)
//         .withArgs(mdocArgs :+ "--watch")
//         .withOut(mdocOutFile.asPath)
//         .withClasspath(classpath.map(_.data).mkString(":"))

//       // process the mdoc files to the correct location
//       _root_.mdoc.Main.process(settings)

//       yarnProcess.destroy()
//     }
//   )
//   .settings(cilibSettings)
//   .settings(noPublishSettings)
//   .dependsOn(core, example, exec, pso, moo, ga)
// lazy val credentialSettings = Seq(
//   credentials ++= (for {
//     username <- Option(System.getenv("SONATYPE_USERNAME"))
//     password <- Option(System.getenv("SONATYPE_PASSWORD"))
//   } yield
//     Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq,
//   sonatypeProfileName := "net.cilib",
//   pgpPublicRing := file("./project/local.pubring.asc"),
//   pgpSecretRing := file("./project/local.secring.asc"),
//   pgpPassphrase := Option(System.getenv("PGP_PASS")).map(_.toArray)
// )

lazy val eda = project
  .in(file("eda"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("eda"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .enablePlugins(BuildInfoPlugin)

lazy val example = project
  .in(file("example"))
  .dependsOn(core)
  .dependsOn(pso)
  .dependsOn(ga)
  .dependsOn(exec)
  .dependsOn(io)
  .dependsOn(de)
  .settings(BuildHelper.stdSettings("example"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(fork in run := true)
  .settings(connectInput in run := true)
  .settings(
    libraryDependencies ++= Seq(
      "net.cilib" %% "benchmarks" % "0.1.1"
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val exec = project
  .in(file("exec"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("exec"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                  %% "zio"            % "1.0.0-RC21-2",
      "dev.zio"                  %% "zio-streams"    % "1.0.0-RC21-2",
      "com.github.mjakubowski84" %% "parquet4s-core" % parquet4sVersion
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val moo = project
  .in(file("moo"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("moo"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .enablePlugins(BuildInfoPlugin)

lazy val pso = project
  .in(file("pso"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("pso"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .enablePlugins(BuildInfoPlugin)

lazy val ga = project
  .in(file("ga"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("ga"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .enablePlugins(BuildInfoPlugin)

lazy val de = project
  .in(file("de"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("de"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .enablePlugins(BuildInfoPlugin)

lazy val tests = project
  .in(file("tests"))
  .dependsOn(core)
  .dependsOn(pso)
  .settings(BuildHelper.stdSettings("tests"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(skip in publish := true)
  .settings(javaOptions in test += "-Xmx1G")
  .settings(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck"                % scalacheckVersion % "test",
      "org.scalaz"     %% "scalaz-scalacheck-binding" % scalazVersion     % "test"
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val io = project
  .in(file("io"))
  .dependsOn(core)
  .dependsOn(exec)
  .settings(BuildHelper.stdSettings("io"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      "com.chuusai"              %% "shapeless"      % "2.3.2",
      "com.github.mjakubowski84" %% "parquet4s-core" % parquet4sVersion,
      "org.apache.hadoop"        % "hadoop-client"   % "2.7.3",
      "dev.zio"                  %% "zio"            % "1.0.0-RC21-2",
      "dev.zio"                  %% "zio-streams"    % "1.0.0-RC21-2"
    )
  )
  .enablePlugins(BuildInfoPlugin)

scalafixDependencies in ThisBuild += "com.nequissimus" %% "sort-imports" % "0.5.0"
