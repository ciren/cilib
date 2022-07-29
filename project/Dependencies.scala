import sbt._

object Version {

  val zio        = "2.0.0" // matches the zio version used in zio-prelude
  val zioPrelude = "1.0.0-RC15"
  val zioOptics  = "2.0.0-RC4"
  val parquet4s  = "2.6.0"

}

object Dependencies {

  val zio          = "dev.zio"                  %% "zio"            % Version.zio
  val zioStreams   = "dev.zio"                  %% "zio-streams"    % Version.zio
  val zioPrelude   = "dev.zio"                  %% "zio-prelude"    % Version.zioPrelude
  val zioOptics    = "dev.zio"                  %% "zio-optics"     % Version.zioOptics
  val zioTest      = "dev.zio"                  %% "zio-test"       % Version.zio % Test
  val zioTestSbt   = "dev.zio"                  %% "zio-test-sbt"   % Version.zio % Test
  val parquet4s    = "com.github.mjakubowski84" %% "parquet4s-core" % Version.parquet4s
  val hadoopClient = "org.apache.hadoop"         % "hadoop-client"  % "2.8.5"

}
