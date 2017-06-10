import sbt._
import sbt.Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._
import sbtunidoc.Plugin.UnidocKeys._
import microsites._

val scalazVersion     = "7.2.7"
val spireVersion      = "0.13.0"
val monocleVersion    = "1.3.2"
val scalacheckVersion = "1.12.6"

lazy val buildSettings = Seq(
  organization := "net.cilib"
)

lazy val commonSettings = Seq(
  autoAPIMappings := true,
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    //"-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    //"-Xfatal-warnings",
    "-Xlint",
    //"-Xlog-implicits",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
//    "-Yno-predef",
//    "-Yno-imports",
    "-Xfuture",
    "-Ypartial-unification"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "bintray/non" at "http://dl.bintray.com/non/maven"
  ),
  libraryDependencies ++= Seq(
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3" cross CrossVersion.binary)
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/cirg-up/cilib"),
    "scm:git:git@github.com:cirg-up/cilib.git"))
)

/*lazy val publishSignedArtifacts = ReleaseStep(
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
)*/

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
  //publishArtifact in packageDoc := false,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
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
    //publishSignedArtifacts,
    setNextVersion,
    commitNextVersion
    //pushChanges
  )
) ++ credentialSettings

lazy val cilibSettings = buildSettings ++ commonSettings ++ publishSettings// ++ releaseSettings

lazy val cilib = project.in(file("."))
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .aggregate(core, docs, example, exec, ga, moo, pso, tests)
  .dependsOn(core, docs, example, exec, ga, moo, pso, tests)

lazy val core = project
  .settings(cilibSettings ++ Seq(
    moduleName := "cilib-core",
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

lazy val docs = project.in(file("docs"))
  .enablePlugins(MicrositesPlugin)
  .settings(moduleName := "cilib-docs")
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .settings(unidocSettings)
  .settings(ghpages.settings)
  .settings(docSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .dependsOn(core, example, exec, pso, moo, ga)

lazy val docSettings = Seq(
    micrositeName := "CIlib",
    micrositeDescription := "Verifiable Computational Intelligence",
    micrositeBaseUrl := "/cilib",
    micrositeDocumentationUrl := "/cilib/docs",
    //micrositeAuthor := "",
    micrositeHomepage := "http://cirg-up.github.io",
    micrositeGithubOwner := "cirg-up",
    micrositeGithubRepo := "cilib",
    //micrositeHighlightTheme := "monokai",
    micrositeExtraMdFiles := Map(file("README.md") -> ExtraMdFileConfig("index.html", "home", Map("title" -> "Home", "section" -> "home"))),
    fork in tut := true,

    siteSubdirName in SiteScaladoc := "api",
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(example),
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in SiteScaladoc)
  )

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv("SONATYPE_USERNAME"))
    password <- Option(System.getenv("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val example = project.dependsOn(core, exec, ga, moo, pso)
  .settings(cilibSettings ++ noPublishSettings ++ Seq(
    moduleName := "cilib-example",
    libraryDependencies ++= Seq(
      "net.cilib"  %% "benchmarks"        % "0.1.1",
      "org.scalaz" %% "scalaz-core"       % scalazVersion,
      "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
      "org.scalaz" %% "scalaz-effect"     % scalazVersion,
      "org.jfree"   % "jfreechart"        % "1.0.19"
    )
  ))

lazy val exec = project.dependsOn(core)
  .settings(Seq(moduleName := "cilib-exec") ++ cilibSettings)

lazy val moo = project.dependsOn(core)
  .settings(Seq(moduleName := "cilib-moo") ++ cilibSettings)

lazy val pso = project.dependsOn(core)
  .settings(Seq(moduleName := "cilib-pso") ++ cilibSettings)

lazy val ga = project.dependsOn(core)
  .settings(Seq(moduleName := "cilib-ga") ++ cilibSettings)

lazy val tests = project
  .dependsOn(core)
  .settings(cilibSettings ++ noPublishSettings ++ Seq(
    moduleName := "cilib-tests",
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck"                % scalacheckVersion % "test",
      "org.scalaz"     %% "scalaz-scalacheck-binding" % scalazVersion     % "test"
    )
  ))
