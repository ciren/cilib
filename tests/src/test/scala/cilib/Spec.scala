package cilib

import org.scalacheck._

abstract class Spec(name: String) extends Properties(name) {

  def checkAll(props: Properties): Unit = {
    for ((name, prop) <- props.properties) yield {
      property(name) = prop
    }
    ()
  }

  def checkAll(name: String, props: Properties): Unit = {
    for ((name2, prop) <- props.properties) yield {
      property(name + ":" + name2) = prop
    }
    ()
  }
}
