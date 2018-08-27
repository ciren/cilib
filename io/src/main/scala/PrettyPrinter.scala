package cilib
package io

import org.typelevel.paiges._
import scalaz._
import Scalaz._
import spire.math.Interval

sealed trait PrettyPrinter[A] {
    def toDoc(x: A): Doc
}

object PrettyPrinter {

    def apply[A](x: A)(implicit prettyPrinter: PrettyPrinter[A]): Doc = prettyPrinter.toDoc(x)

    implicit val boundsPrettyPrinter: PrettyPrinter[NonEmptyList[Interval[Double]]] =
        new PrettyPrinter[NonEmptyList[Interval[Double]]] {
            def toDoc(bounds: NonEmptyList[Interval[Double]]): Doc =
                Doc.intercalate(Doc.paragraph(", "), bounds.toList.map(x => Doc.paragraph(x.toString()))) +
                    Doc.line
        }

    implicit val nelPrettyPrinter: PrettyPrinter[NonEmptyList[Double]] =
        new PrettyPrinter[NonEmptyList[Double]] {
            def toDoc(bounds: NonEmptyList[Double]): Doc =
                Doc.intercalate(Doc.paragraph(", "), bounds.toList.map(x => Doc.paragraph(x.toString()))) +
                    Doc.line
        }

    implicit val positionPrettyPrinter: PrettyPrinter[Position[Double]] =
        new PrettyPrinter[Position[Double]] {
            def toDoc(x: Position[Double]): Doc =
                Doc.paragraph("Position: ") + nelPrettyPrinter.toDoc(x.pos) + Doc.paragraph("Bounds: ") +
                    boundsPrettyPrinter.toDoc(x.boundary)
        }

}

val rng = RNG.init(12L)
val bounds = Interval(-10.0, 10.0) ^ 3
val point = Position.createPosition(bounds).eval(rng)

PrettyPrinter(point).render(80)