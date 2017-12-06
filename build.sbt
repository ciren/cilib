import sbt._
import sbt.Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._

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
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-explaintypes",                     // Explain type errors in more detail.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
    "-language:higherKinds",             // Allow higher-kinded types
    "-language:implicitConversions",     // Allow definition of implicit functions called views
    "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
    //"-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
    "-Xfuture",                          // Turn on future language features.
    "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
    "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
    "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
    "-Xlint:option-implicit",            // Option.apply used implicit view.
    "-Xlint:package-object-classes",     // Class or object defined in package object.
    "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match",              // Pattern match may not be typesafe.
    "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification",             // Enable partial unification in type constructor inference
    "-Ywarn-dead-code",                  // Warn when dead code is identified.
    "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen",              // Warn when numerics are widened.
    "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals",              // Warn if a local definition is unused.
    "-Ywarn-unused:params",              // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates",            // Warn if a private member is unused.
    "-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
  ),
  scalacOptions in (Compile, console) ~= (_.filterNot(Set(
    "-Ywarn-unused:imports",
    "-Xfatal-warnings"
  ))),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "bintray/non" at "http://dl.bintray.com/non/maven"
  ),
  libraryDependencies ++= Seq(
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3" cross CrossVersion.binary)
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/cirg-up/cilib"),
    "scm:git:git@github.com:cirg-up/cilib.git")),
  initialCommands in console := """
    |import scalaz._
    |import Scalaz._
    |import cilib._
    |""".stripMargin
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

lazy val cilibSettings = buildSettings ++ commonSettings ++ publishSettings

lazy val cilib = project.in(file("."))
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .aggregate(core, de, docs, example, exec, ga, moo, pso, tests)
  .dependsOn(core, de, docs, example, exec, ga, moo, pso, tests)

lazy val core = project
  .settings(cilibSettings ++ Seq(
    moduleName := "cilib-core",
    libraryDependencies ++= Seq(
      "org.scalaz"                 %% "scalaz-core"       % scalazVersion,
      "org.scalaz"                 %% "scalaz-concurrent" % scalazVersion,
      "org.spire-math"             %% "spire"             % spireVersion,
      "com.github.julien-truffaut" %% "monocle-core"      % monocleVersion,
      "com.chuusai"                %% "shapeless"         % "2.3.2"
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
    ),
    initialCommands in console := """
    |import scalaz._
    |import Scalaz._
    |import cilib._    |""".stripMargin
  ))

val siteStageDirectory    = SettingKey[File]("site-stage-directory")
val copySiteToStage       = TaskKey[Unit]("copy-site-to-stage")

lazy val docs = project.in(file("docs"))
  .enablePlugins(GhpagesPlugin, TutPlugin, ParadoxSitePlugin, ParadoxMaterialThemePlugin, ScalaUnidocPlugin)
  .settings((scalacOptions in Tut) ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .settings(ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox))
  .settings(moduleName := "cilib-docs")
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .settings(docSettings)
  .dependsOn(core, example, exec, pso, moo, ga)

lazy val docSettings = Seq(
  fork in tut := true,
  tutSourceDirectory := sourceDirectory.value / "main" / "tut",
  ghpagesNoJekyll := true,
  excludeFilter in ghpagesCleanSite :=
    new FileFilter {
      def accept(f: File) = (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath
    },
  siteSubdirName in SiteScaladoc := "api",
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(example),
  addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in SiteScaladoc),
  git.remoteRepo := scmInfo.value.map(_.browseUrl.toString).getOrElse(sys.error("Unable to lookup the scm url")),
  siteStageDirectory := target.value / "site-stage",
  sourceDirectory in paradox in Paradox := siteStageDirectory.value,
  sourceDirectory in paradox  := siteStageDirectory.value,
  paradoxMaterialTheme in Paradox ~= {
    _.withFavicon("img/favicon.png")
      .withLogo("img/sbt-logo.svg")
      .withRepository(uri("https://github.com/cirg-up/cilib"))
  },
 // version in Paradox := {
 //   val git = GitKeys.gitRunner.value
 //   val s = streams.value

//    if (isSnapshot.value) git("tag" :: "-l" :: Nil)(, s.log) //"git tag -l".!!.split("\r?\n").last.substring(1) // TODO: replace this with jgit / sbt git
//    else version.value

//version.value
//  },
  copySiteToStage := {
    IO.copyDirectory(
      source = sourceDirectory.value / "main" / "paradox",
      target = siteStageDirectory.value,
      overwrite = false,
      preserveLastModified = true)
    IO.copyDirectory(
      source = tutTargetDirectory.value,
      target = siteStageDirectory.value,
      overwrite = false,
      preserveLastModified = true)
  },
  copySiteToStage := copySiteToStage.dependsOn(tutQuick).value,
  makeSite := makeSite.dependsOn(copySiteToStage).value
)

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv("SONATYPE_USERNAME"))
    password <- Option(System.getenv("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val example = project.dependsOn(core, exec, ga, moo, pso)
  .settings(cilibSettings ++ noPublishSettings ++ Seq(
    fork in run := true,
    moduleName := "cilib-example",
    libraryDependencies ++= Seq(
      "net.cilib"  %% "benchmarks"        % "0.1.1",
      "org.scalaz" %% "scalaz-core"       % scalazVersion,
      "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
      "org.scalaz" %% "scalaz-effect"     % scalazVersion,
      "org.scalaz.stream" %% "scalaz-stream"     % "0.8.6a"
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

lazy val de = project.dependsOn(core)
  .settings(Seq(moduleName := "cilib-de") ++ cilibSettings)

lazy val tests = project
  .dependsOn(core, pso, ga, moo)
  .settings(cilibSettings ++ noPublishSettings ++ Seq(
    moduleName := "cilib-tests",
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck"                % scalacheckVersion % "test",
      "org.scalaz"     %% "scalaz-scalacheck-binding" % scalazVersion     % "test"
    )
  ))
