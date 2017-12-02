resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
      Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n"      % "sbt-unidoc"  % "0.3.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")

//addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

addSbtPlugin("com.fortysevendeg"  % "sbt-microsites"  % "0.4.0")
addSbtPlugin("com.dwijnand" % "sbt-travisci" % "1.1.0")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC13")
