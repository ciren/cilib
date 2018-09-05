package cilib
package io

import cilib.exec.Progress
import org.typelevel.paiges._
import scalaz._
import Scalaz._
import spire.math.Interval

@annotation.implicitNotFound(
    """
EncodeDoc derivation error for type: ${A}
Not all members of the type have instances of EncodeDoc defined, or in
scope. It is recommended to examine the fields of the type and then to
define an instance of EncodeDoc for the any custom parameter type.""")
sealed trait EncodeDoc[A] {
    def toDoc(x: A): Doc
}

object PrettyPrinter {

    def apply[A](a: A)(implicit docEncoder: EncodeDoc[A]) =
        docEncoder.toDoc(a)

    def createEncoder[A](f: A => Doc) =
        new EncodeDoc[A] {
            override def toDoc(x: A): Doc = f(x)
        }

    // Method for creating docs based on non empty lists
    private def nelEncoder[A](x: NonEmptyList[A])(implicit docEncoder: EncodeDoc[A]) =
        Doc.intercalate(Doc.paragraph(", "), x.toList.map(docEncoder.toDoc))

    // Encoders for basic types
    implicit val booleanEncodeDoc = createEncoder[Boolean](x => Doc.paragraph(x.toString))
    implicit val byteEncodeDoc = createEncoder[Byte](x => Doc.paragraph(x.toString))
    implicit val shortEncodeDoc = createEncoder[Short](x => Doc.paragraph(x.toString))
    implicit val intEncodeDoc = createEncoder[Int](x => Doc.paragraph(x.toString))
    implicit val longEncodeDoc = createEncoder[Long](x => Doc.paragraph(x.toString))
    implicit val floatEncodeDoc = createEncoder[Float](x => Doc.paragraph(x.toString))
    implicit val doubleEncodeDoc = createEncoder[Double](x => Doc.paragraph(x.toString))
    implicit val stringEncodeDoc = createEncoder[String](Doc.paragraph)

    // Opinionated implicicts for common types within CIlib
    implicit val intervalEncodeDoc = createEncoder[Interval[Double]](x => Doc.paragraph(x.toString))

    implicit val memEncoder = createEncoder[Mem[Double]](x => {
        val feasibleOptic = Lenses._singleFitness[Double].composePrism(Lenses._feasible)
        Doc.paragraph("Personal Best Position: ") + nelEncoder(x.b.pos) + Doc.line +
            Doc.paragraph("Personal Best Bounds: ") + nelEncoder(x.b.boundary) + Doc.line +
            Doc.paragraph("Personal Best Solution: ") + Doc.paragraph(feasibleOptic.headOption(x.b).getOrElse("Infeasible").toString) +
            Doc.line + Doc.paragraph("Velocity: ") + nelEncoder(x.v.pos)
    })

    implicit val positionEncodeDoc = createEncoder[Position[Double]](x => {
        val feasibleOptic = Lenses._singleFitness[Double].composePrism(Lenses._feasible)
        Doc.paragraph("Position: ") + nelEncoder(x.pos) + Doc.line +
            Doc.paragraph("Bounds: ") + nelEncoder(x.boundary) + Doc.line +
            Doc.paragraph("Solution: ") + Doc.paragraph(feasibleOptic.headOption(x).getOrElse("Infeasible").toString)
    })

    implicit val particleEcodeDoc = createEncoder[Entity[Mem[Double], Double]](x =>
        Doc.paragraph("Entity") + Doc.line +
            positionEncodeDoc.toDoc(x.pos) + Doc.line +
            memEncoder.toDoc(x.state) + Doc.line
    )

    implicit val nelParticleEcodeDoc = createEncoder[NonEmptyList[Entity[Mem[Double], Double]]](x =>
        Doc.intercalate(Doc.line, x.zipWithIndex.toList map {
            case (entity: Entity[Mem[Double], Double], index: Int) =>
                Doc.paragraph(s"Entity ${index + 1}") + Doc.line +
                    positionEncodeDoc.toDoc(entity.pos) + Doc.line +
                    memEncoder.toDoc(entity.state) + Doc.line
        })
    )

    implicit val progressParticleEcodeDoc = createEncoder[Progress[NonEmptyList[Entity[Mem[Double], Double]]]](x =>
        Doc.paragraph(s"Algorithm: ${x.algorithm}") + Doc.line +
            Doc.paragraph(s"Problem: ${x.problem}") + Doc.line +
            Doc.paragraph(s"Seed Value: ${x.seed}") + Doc.line +
            Doc.paragraph(s"Iterations: ${x.iteration}") + Doc.line +
            Doc.paragraph(s"Environment: ${x.env}") + Doc.line + Doc.line +
            nelParticleEcodeDoc.toDoc(x.value)
    )

    implicit val individualEcodeDoc = createEncoder[Entity[Unit, Double]](x =>
        Doc.paragraph("Entity") + Doc.line +
            positionEncodeDoc.toDoc(x.pos) + Doc.line
    )

    implicit val nelIndividualEcodeDoc = createEncoder[NonEmptyList[Entity[Unit, Double]]](x =>
        Doc.intercalate(Doc.line, x.zipWithIndex.toList map {
            case (entity: Entity[Unit, Double], index: Int) =>
                Doc.paragraph(s"Entity ${index + 1}") + Doc.line +
                    positionEncodeDoc.toDoc(entity.pos) + Doc.line
        })
    )

    implicit val progressIndividualEcodeDoc = createEncoder[Progress[NonEmptyList[Entity[Unit, Double]]]](x =>
        Doc.paragraph(s"Algorithm: ${x.algorithm}") + Doc.line +
            Doc.paragraph(s"Problem: ${x.problem}") + Doc.line +
            Doc.paragraph(s"Seed Value: ${x.seed}") + Doc.line +
            Doc.paragraph(s"Iterations: ${x.iteration}") + Doc.line +
            Doc.paragraph(s"Environment: ${x.env}") + Doc.line + Doc.line +
            nelIndividualEcodeDoc.toDoc(x.value)
    )

}