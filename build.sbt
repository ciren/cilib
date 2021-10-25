import sbt._
import sbt.Keys._

val zio        = "dev.zio" %% "zio"          % Version.zio
val zioStreams = "dev.zio" %% "zio-streams"  % Version.zio
val zioPrelude = "dev.zio" %% "zio-prelude"  % Version.zioPrelude
val zioTest    = "dev.zio" %% "zio-test"     % Version.zio % Test
val zioTestSbt = "dev.zio" %% "zio-test-sbt" % Version.zio % Test

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  List(
    organization := "ciren",
    homepage     := Some(url("https://cilib,net")),
    licenses     := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers   := List(
      Developer(
        "gpampara",
        "Gary Pamparà",
        "",
        url("http://gpampara.github.io")
      )
    ),
    scmInfo      := Some(
      ScmInfo(url("https://github.com/ciren/cilib/"), "scm:git:git@github.com:ciren/cilib.git")
    )
  )
)

addCommandAlias("build", "prepare; test")
addCommandAlias("fix", "; all compile:scalafix test:scalafix; all scalafmtSbt scalafmtAll")
addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check; test:scalafix --check")

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
    console        := (core / Compile / console).value,
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
  .settings(BuildHelper.crossProjectSettings)
  .settings(BuildHelper.buildInfoSettings("net.cilib"))
  .settings(Compile / console / scalacOptions ~= { _.filterNot(Set("-Xfatal-warnings")) })
  .settings(
    libraryDependencies ++= Seq(
      zio,
      zioPrelude,
      "org.typelevel" %% "spire" % Version.spire
    )
  )
  .enablePlugins(BuildInfoPlugin)

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
  .settings(BuildHelper.crossProjectSettings)
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      zio,
      zioStreams,
      "com.github.mjakubowski84" %% "parquet4s-core" % Version.parquet4s
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
  .settings(run / fork := true)
  .settings(libraryDependencies ++= Seq(zioTest, zioTestSbt))
  .enablePlugins(BuildInfoPlugin)

lazy val io = project
  .in(file("io"))
  .dependsOn(core)
  .dependsOn(exec)
  .settings(BuildHelper.stdSettings("io"))
  .settings(BuildHelper.buildInfoSettings("cilib"))
  .settings(
    libraryDependencies ++= Seq(
      "com.github.mjakubowski84" %% "parquet4s-core" % Version.parquet4s,
      "org.apache.hadoop"         % "hadoop-client"  % "2.8.5",
      zio,
      zioStreams
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val docs = project
  .in(file("cilib-docs"))
  .settings(BuildHelper.stdSettings("docs"))
  .settings(mdocVariables := Map("CILIB_VERSION" -> version.value))
  .settings(publish / skip := true)
  .dependsOn(core, pso, exec, io)
  .enablePlugins(MdocPlugin, DocusaurusPlugin)
