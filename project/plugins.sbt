addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.4")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.1")

resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
