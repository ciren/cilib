resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
      Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n"      % "sbt-unidoc"  % "0.3.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"     % "1.0.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"    % "1.2.0-RC1")
addSbtPlugin("com.typesafe.sbt"  % "sbt-ghpages" % "0.5.4")
addSbtPlugin("org.tpolecat"      % "tut-plugin"  % "0.4.7")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")

//addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
