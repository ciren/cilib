package cilib
package work

import scalaz.effect.IO._
import scalaz.effect.{IO, SafeApp}

object Main extends SafeApp {

  override val runc: IO[Unit] =
    putStrLn("Hello world")

}