package cilib.io

sealed abstract class WriteMode
object Overwrite extends WriteMode
object Create    extends WriteMode
