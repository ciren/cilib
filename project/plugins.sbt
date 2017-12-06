resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
      Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n"      % "sbt-unidoc"  % "0.4.1")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")

//addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.2")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.3.0")
addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.5.6")

addSbtPlugin("com.dwijnand" % "sbt-travisci" % "1.1.0")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC13")
