addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.2.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")
//addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")

//addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.11")

resolvers += Resolver.sonatypeRepo("releases")
