import sbt._
import Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._
import sbtunidoc.Plugin.UnidocKeys._
import com.typesafe.sbt.SbtSite.SiteKeys._

val scalazVersion     = "7.1.2"
val spireVersion      = "0.9.0"
val monocleVersion    = "1.1.1"
val scalacheckVersion = "1.11.4"

lazy val buildSettings = Seq(
  organization := "net.cilib",
  scalaVersion := "2.11.7"
  //crossScalaVersions := Seq("2.11.7")
)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    //"-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    //"-Xlog-implicits",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "bintray/non" at "http://dl.bintray.com/non/maven"
  ),
  libraryDependencies ++= Seq(
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.6.0")
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/cirg-up/cilib"),
    "git@github.com:cirg-up/cilib.git"))
)

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

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val publishSettings = Seq(
  organizationHomepage := Some(url("http://cirg.cs.up.ac.za")),
  homepage := Some(url("http://www.cilib.net")),
  licenses := Seq("BSD-style" -> url("http://opensource.org/licenses/BSD-2-Clause")),//Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  autoAPIMappings := true,
  apiURL := Some(url("https://www.cilib.net/api/")),
  publishMavenStyle := true,
  publishArtifact in packageDoc := false,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <scm>
      <url>git@github.com:cirg-up/cilib.git</url>
      <connection>scm:git:git@github.com:cirg-up/cilib.git</connection>
    </scm>
    <developers>
      {
        Seq(
          ("gpampara", "Gary Pamparà"),
          ("filinep", "Filipe Nepomuceno"),
          ("benniel", "Bennie Leonard")
        ).map {
          case (id, name) =>
            <developer>
              <id>{id}</id>
              <name>{name}</name>
              <url>http://github.com/{id}</url>
            </developer>
        }
      }
    </developers>
  ),
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
    //pushChanges
  )
)

lazy val cilibSettings = buildSettings ++ commonSettings ++ publishSettings ++ releaseSettings

lazy val cilib = project.in(file("."))
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .aggregate(benchmarks, core, docs, example, exec, moo, tests)
  .dependsOn(benchmarks, core, docs, example, exec, moo, tests)

//   lazy val cilibSettings = settings ++ Seq(
//     name := "cilib-aggregate"
//   ) ++ noPublish ++ headerCheckSetting ++ releaseSettings ++ Seq(
//     publishArtifactsAction := PgpKeys.publishSigned.value
//   )

lazy val core = project
  .settings(moduleName := "cilib-core")
  .settings(cilibSettings)
  .settings(Seq(
    libraryDependencies ++= Seq(
      "org.scalaz"                 %% "scalaz-core"       % scalazVersion,
      "org.scalaz"                 %% "scalaz-concurrent" % scalazVersion,
      "org.spire-math"             %% "spire"             % spireVersion,
      "com.github.julien-truffaut" %% "monocle-core"      % monocleVersion
    /*),
    wartremoverErrors ++= Seq(
      //Wart.Any,
      Wart.Any2StringAdd,
      //Wart.AsInstanceOf,
      //Wart.IsInstanceOf,
      Wart.DefaultArguments,
      Wart.ListOps,
      Wart.NonUnitStatements,
      Wart.Null,
      Wart.OptionPartial,
      Wart.Product,
      Wart.Return,
      Wart.Serializable,
      Wart.Var*/
    )
  ))

lazy val docSettings = Seq(
  autoAPIMappings := true,
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(core),
  site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
  tutTargetDirectory := baseDirectory.value / "src" / "jekyll" / "tut",
  //site.addMappingsToSiteDir(tut, "_tut"),
  com.typesafe.sbt.site.JekyllSupport.requiredGems := Map(
    "jekyll" -> "3.0.0",
    "jekyll-paginate" -> "1.1.0",
    "liquid" -> "3.0.6"
  ),
//  ghpagesNoJekyll := false,
  siteMappings += file("CONTRIBUTING.md") -> "contributing.md",
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),
  git.remoteRepo := "git@github.com:cirg-up/cilib.git",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md",
  makeSite <<= makeSite.dependsOn(tut, (unidoc in Compile))
)

lazy val docs = project.in(file("docs"))
  .settings(moduleName := "cilib-docs")
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .settings(unidocSettings)
  .settings(site.settings)
  .settings(site.jekyllSupport())
//.settings(ghpages.settings)
  .settings(tutSettings)
  .settings(docSettings)
  .dependsOn(core, benchmarks)

lazy val example = project.dependsOn(core, exec, moo)
  .settings(moduleName := "cilib-example")
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .settings(Seq(
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core"       % scalazVersion,
      "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
      "org.scalaz" %% "scalaz-effect"     % scalazVersion,
      "org.jfree"   % "jfreechart"        % "1.0.19"
    )
  ))

lazy val exec = project.dependsOn(core)
  .settings(moduleName := "cilib-exec")
  .settings(cilibSettings)

lazy val moo = project.dependsOn(core)
  .settings(moduleName := "cilib-moo")
  .settings(cilibSettings)

lazy val tests = project
  .dependsOn(core, benchmarks)
  .settings(moduleName := "cilib-tests")
  .settings(cilibSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck"                % scalacheckVersion % "test",
      "org.scalaz"     %% "scalaz-scalacheck-binding" % scalazVersion     % "test"
    )
  )
  .settings(noPublishSettings)

lazy val benchmarks = project
  .settings(moduleName := "cilib-benchmarks")
  .settings(cilibSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalaz"     %% "scalaz-core" % scalazVersion,
      "org.spire-math" %% "spire"       % spireVersion
    )
  )
