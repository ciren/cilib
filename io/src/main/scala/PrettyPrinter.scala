package cilib
package io

import org.typelevel.paiges._
import scalaz._
import Scalaz._

object PrettyPrinter {

    implicit class positionPrinterOps(p: Position[Double]){
        def prettyPrint: Doc = {
            val pos = Doc.text("Position: ") +
                Doc.intercalate(Doc.text(", "), p.pos.toList.map(x => Doc.text(x.toString))) +
                Doc.line

            val bounds = if(p.boundary.toList.forall(_ == p.boundary.head)) {
                Doc.text("All points in the position share the same bounds of " + p.boundary.head.toString())
            } else {
                Doc.text("Bounds: ") +
                    Doc.intercalate(Doc.text(", "), p.boundary.toList.map(x => Doc.text(x.toString()))) +
                    Doc.line
            }

            val solution = Lenses._singleFitness[Double].composePrism(Lenses._feasible).headOption(p) match {
                    case None        => Doc.empty
                    case Some(value) => Doc.text("Solution: " + value.toString)
                }

        }
    }

}
