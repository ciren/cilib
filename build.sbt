import sbt._
import sbt.Keys._

val scalazVersion     = "7.3.2"
val spireVersion      = "0.17.0-RC1"
val parquet4sVersion  = "1.3.1"
val scalacheckVersion = "1.14.3"
val zioVersion        = "1.0.6"

val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion
val zio = "dev.zio" %% "zio" % zioVersion
val zioStreams = "dev.zio" %% "zio-streams" % zioVersion
val zioPrelude = "dev.zio" %% "zio-prelude" % "1.0.0-RC3"

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  List(
    organization := "ciren",
    homepage := Some(url("https://cilib,net")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "gpampara",
        "Gary Pamparà",
        "",
        url("http://gpampara.github.io")
      )
    ),
    scmInfo := Some(
      ScmInfo(url("https://github.com/ciren/cilib/"), "scm:git:git@github.com:ciren/cilib.git")
    )
  )
)

addCommandAlias("build", "prepare; test")
addCommandAlias("prepare", "fix; fmt")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias(
  "fixCheck",
  "; compile:scalafix --check ; test:scalafix --check"
)
addCommandAlias("fmt", "all root/scalafmtSbt root/scalafmtAll")
addCommandAlias("fmtCheck", "all root/scalafmtSbtCheck root/scalafmtCheckAll")

//   initialCommands in console := """
//     |import scalaz._
//     |import Scalaz._
//     |import cilib._
//     |import spire.implicits._
//     |""".stripMargin

// lazy val publishSettings = Seq(
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

lazy val root = project
  .in(file("."))
  .settings(
    publish / skip := true,
    console := (core / Compile / console).value,
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
    moo,
    docs
  )

lazy val core = project
  .in(file("core"))
  .settings(BuildHelper.stdSettings("core"))
  //.settings(crossProjectSettings
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      scalaz,
      zio,
      zioPrelude,
      "org.typelevel"              %% "spire"        % spireVersion,
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
  .settings(publish / skip := true)
  .settings(run / fork := true)
  .settings(run / connectInput := true)
  .enablePlugins(BuildInfoPlugin)

lazy val exec = project
  .in(file("exec"))
  .dependsOn(core)
  .settings(BuildHelper.stdSettings("exec"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      zio,
      zioStreams,
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
  .settings(publish / skip := true)
  .settings(test / javaOptions += "-Xmx1G")
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
      "com.chuusai"              %% "shapeless"      % "2.3.3",
      "com.github.mjakubowski84" %% "parquet4s-core" % parquet4sVersion,
      "org.apache.hadoop"        % "hadoop-client"   % "2.7.3",
      zio,
      zioStreams
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val docs = project
  .in(file("cilib-docs"))
  .settings(mdocVariables := Map("CILIB_VERSION" -> version.value))
  .settings(publish / skip := true)
  .dependsOn(core, pso, exec, io)
  .enablePlugins(MdocPlugin, DocusaurusPlugin)

ThisBuild / scalafixDependencies += "com.nequissimus" %% "sort-imports" % "0.5.0"
