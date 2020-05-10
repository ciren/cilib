import sbt._
import sbt.Keys._
import sbtrelease.ReleaseStateTransformations._

val scalazVersion       = "7.2.25"
val scalazStreamVersion = "0.8.6a"
val spireVersion        = "0.13.0"
val monocleVersion      = "1.5.0"
val scalacheckVersion   = "1.14.0"
val avro4sVersion       = "3.1.0"

// Library requreid for the SBT build itself
libraryDependencies += "org.scalameta" %% "mdoc" % "2.1.0"

//val previousArtifactVersion = SettingKey[String]("previous-tagged-version")

lazy val mdoc = taskKey[Unit]("Process the mdoc files")
lazy val websiteWatch = taskKey[Unit]("Watch website files and reload")
lazy val buildWebsite = taskKey[Unit]("Build website")

lazy val commonSettings = Seq(
  organization := "net.cilib",
  autoAPIMappings := true,
  scalaVersion := "2.12.8",
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
    //"-Xfatal-warnings",                // Fail the compilation if there are any warnings.
    "-Xfuture",                          // Turn on future language features.
    "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
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
    "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen",              // Warn when numerics are widened.
    "-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
  ),
  scalacOptions in (Compile, console) ~= (_.filterNot(Set(
    "-Ywarn-unused:imports",
    "-Xfatal-warnings"
  ))),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "bintray/non" at "https://dl.bintray.com/non/maven"
  ),
  libraryDependencies ++= Seq(
    compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3" cross CrossVersion.binary),
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/cirg-up/cilib"),
    "scm:git:git@github.com:cirg-up/cilib.git")),
  initialCommands in console := """
    |import scalaz._
    |import Scalaz._
    |import cilib._
    |import spire.implicits._
    |""".stripMargin
  // MiMa related
  /*previousArtifactVersion := { // Can this be done nicer/safer?
    import org.eclipse.jgit._
    import org.eclipse.jgit.api._
    import org.eclipse.jgit.lib.Constants
    import scala.collection.JavaConverters._

    val git = Git.open(new java.io.File("."))
    val tags = git.tagList.call().asScala
    val current = git.getRepository.resolve(Constants.HEAD)

    val lastTag = tags.lift(tags.size - 1)
    val name =
      lastTag.flatMap(last => {
        if (last.getObjectId.getName == current.getName) tags.lift(tags.size - 2).map(_.getName)
        else Some(last.getName)
      })

    name.getOrElse("NO_TAG").replace("refs/tags/v", "")
  },
  mimaPreviousArtifacts := Set(organization.value %% moduleName.value % previousArtifactVersion.value)*/
)

lazy val noPublishSettings = Seq(
  skip in publish := true,
  mimaPreviousArtifacts := Set()
)

lazy val publishSettings = Seq(
  organizationHomepage := Some(url("https://github.com/cirg-up")),
  homepage := Some(url("https://cilib.net")),
  licenses := Seq("Apache-2.0" -> url("https://opensource.org/licenses/Apache-2.0")),
  autoAPIMappings := true,
  apiURL := Some(url("https://cilib.net/api/")),
  publishMavenStyle := true,
  //publishArtifact in packageDoc := false,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
  //   {
  //   val nexus = "https://oss.sonatype.org/"
  //   if (isSnapshot.value)
  //     Some("snapshots" at nexus + "content/repositories/snapshots")
  //   else
  //     Some("releases" at nexus + "service/local/staging/deploy/maven2")
  // },
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
              <url>https://github.com/{id}</url>
            </developer>
        }
      }
    </developers>
  )
)

lazy val cilibSettings =
  commonSettings ++
    publishSettings ++
    credentialSettings

lazy val cilib = project
  .in(file("."))
  .enablePlugins(
    //GitVersioning,
    ReleasePlugin,
    ScalaUnidocPlugin)
  .settings(commonSettings ++ credentialSettings ++ noPublishSettings ++ Seq(
    //git.useGitDescribe := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
//      runClean,
//      runTest,
      releaseStepCommand("publishSigned"),
      releaseStepCommand("sonatypeReleaseAll")
    )
  ))
  .aggregate(core, de, docs, eda, example, exec, ga, moo, pso, tests)
  .dependsOn(core, de, docs, eda, example, exec, ga, moo, pso, tests)


lazy val core = project
  .settings(
    cilibSettings ++ Seq(
      moduleName := "cilib-core",
      libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-core" % scalazVersion,
        "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
        "org.spire-math" %% "spire" % spireVersion,
        "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
        "com.chuusai" %% "shapeless" % "2.3.3",
        "eu.timepit" %% "refined" % "0.9.2"
      ),
      wartremoverErrors in (Compile, compile) ++= Seq(
        //Wart.Any,
        Wart.AnyVal,
        Wart.ArrayEquals,
        Wart.AsInstanceOf,
        Wart.DefaultArguments,
        Wart.ExplicitImplicitTypes,
        Wart.Enumeration,
        //Wart.Equals,
        Wart.FinalCaseClass,
        Wart.FinalVal,
        Wart.ImplicitConversion,
        Wart.ImplicitParameter,
        Wart.IsInstanceOf,
        Wart.JavaConversions,
        Wart.JavaSerializable,
        Wart.LeakingSealed,
        Wart.MutableDataStructures,
        Wart.NonUnitStatements,
        //Wart.Nothing,
        Wart.Null,
        Wart.Option2Iterable,
        Wart.OptionPartial,
        Wart.Overloading,
        Wart.Product,
        Wart.PublicInference,
        Wart.Return,
        //Wart.Recursion,
        Wart.Serializable,
        Wart.StringPlusAny,
        Wart.Throw,
        Wart.ToString,
        Wart.TraversableOps,
        Wart.TryPartial,
        Wart.Var,
        Wart.While
      )
    ))

val mdocVariableMap =
  Map(
    "CILIB_VERSION" -> "2.0.1"
  )
val mdocInFile = new java.io.File("docs")
val mdocOutFile = new java.io.File("website/docs")
val mdocArgs = List("--no-livereload", "--exclude", "target")

lazy val docs = project
  .in(file("docs"))
  .settings(
    moduleName := "cilib-docs",
    connectInput in run := true,
    mdoc := {
      val classpath = (Compile / dependencyClasspath).value

      // build arguments for mdoc
      val settings = _root_.mdoc.MainSettings()
        .withSiteVariables(mdocVariableMap)
        .withArgs(mdocArgs)
        .withOut(new java.io.File("target/mdoc").asPath)
        .withClasspath(classpath.map(_.data).mkString(":"))

      // process the mdoc files to the correct location
      val exitCode = _root_.mdoc.Main.process(settings)

      if (exitCode != 0) sys.exit(exitCode)
    },
    buildWebsite := {
      import scala.sys.process._

      // Generate the mdoc sources
      val classpath = (Compile / dependencyClasspath).value

      // build arguments for mdoc
      val settings = _root_.mdoc.MainSettings()
        .withSiteVariables(mdocVariableMap)
        .withArgs(mdocArgs)
        .withOut(mdocOutFile.asPath)
        .withClasspath(classpath.map(_.data).mkString(":"))

      // process the mdoc files to the correct location
      _root_.mdoc.Main.process(settings)

      Process(Seq("yarn", "install"), new java.io.File("website")).!
      Process(Seq("yarn", "build"), new java.io.File("website")).!
    },
    websiteWatch := {
      import scala.sys.process._

      val yarnProcess = Process(Seq("yarn", "start"), new java.io.File("website")).run
      val classpath = (Compile / dependencyClasspath).value

      // build arguments for mdoc
      val settings = _root_.mdoc.MainSettings()
        .withSiteVariables(mdocVariableMap)
        .withArgs(mdocArgs :+ "--watch")
        .withOut(mdocOutFile.asPath)
        .withClasspath(classpath.map(_.data).mkString(":"))

      // process the mdoc files to the correct location
      _root_.mdoc.Main.process(settings)

      yarnProcess.destroy()
    }
  )
  .settings(cilibSettings)
  .settings(noPublishSettings)
  .dependsOn(core, example, exec, pso, moo, ga)


lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv("SONATYPE_USERNAME"))
    password <- Option(System.getenv("SONATYPE_PASSWORD"))
  } yield
    Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq,
  sonatypeProfileName := "net.cilib",
  pgpPublicRing := file("./project/local.pubring.asc"),
  pgpSecretRing := file("./project/local.secring.asc"),
  pgpPassphrase := Option(System.getenv("PGP_PASS")).map(_.toArray)
)

lazy val eda = project
  .dependsOn(core)
  .settings(Seq(moduleName := "cilib-eda") ++ cilibSettings)

lazy val example = project
  .dependsOn(core, de, exec, ga, io, moo, pso)
  .settings(
    cilibSettings ++ noPublishSettings ++ Seq(
      fork in run := true,
      connectInput in run := true,
      moduleName := "cilib-example",
      libraryDependencies ++= Seq(
        "net.cilib" %% "benchmarks" % "0.1.1",
        "org.scalaz" %% "scalaz-effect" % scalazVersion
      )
    ))

lazy val exec = project
  .dependsOn(core)
  .settings(cilibSettings ++ Seq(
    moduleName := "cilib-exec",
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
      "org.scalaz.stream" %% "scalaz-stream" % scalazStreamVersion,
      "com.sksamuel.avro4s" %% "avro4s-core" % avro4sVersion
    )
  ))

lazy val moo = project
  .dependsOn(core)
  .settings(Seq(moduleName := "cilib-moo") ++ cilibSettings)

lazy val pso = project
  .dependsOn(core)
  .settings(Seq(moduleName := "cilib-pso") ++ cilibSettings)

lazy val ga = project
  .dependsOn(core)
  .settings(Seq(moduleName := "cilib-ga") ++ cilibSettings)

lazy val de = project
  .dependsOn(core)
  .settings(Seq(moduleName := "cilib-de") ++ cilibSettings)

lazy val tests = project
  .dependsOn(core, pso, ga, moo)
  .settings(
    cilibSettings ++ noPublishSettings ++ Seq(
      moduleName := "cilib-tests",
      fork in test := true,
      javaOptions in test += "-Xmx1G",
      libraryDependencies ++= Seq(
        "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test",
        "org.scalaz" %% "scalaz-scalacheck-binding" % (scalazVersion + "-scalacheck-1.14") % "test"
      )
    ))

lazy val io = project
  .dependsOn(core, exec)
  .settings(
    cilibSettings ++ noPublishSettings ++ Seq(
      moduleName := "cilib-io",
      libraryDependencies ++= Seq(
        "com.chuusai" %% "shapeless" % "2.3.2",
        "com.sksamuel.avro4s" %% "avro4s-core" % avro4sVersion,
        "org.apache.parquet" % "parquet-avro" % "1.9.0",
        "org.apache.hadoop" % "hadoop-client" % "2.7.3",
        "org.scalaz.stream" %% "scalaz-stream" % scalazStreamVersion
      )
    ))
