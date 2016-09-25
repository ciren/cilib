resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
      Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n"      % "sbt-unidoc"  % "0.3.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")
//addSbtPlugin("com.typesafe.sbt"  % "sbt-pgp"     % "1.0.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"    % "1.0.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-ghpages" % "0.5.4")
addSbtPlugin("org.tpolecat"      % "tut-plugin"  % "0.4.2")

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.14")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
