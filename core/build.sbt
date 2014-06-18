name := "core"

libraryDependencies := Seq(
  "org.scalaz"     %% "scalaz-core" % "7.1.0-M7",
  "org.spire-math" %% "spire"       % "0.7.5",
  //"com.chuusai"    %% "shapeless"   % "2.0.0-M1" cross CrossVersion.full,
  "org.scalacheck" %% "scalacheck"  % "1.11.3" % "test"
)
