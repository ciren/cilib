package cilib

import com.github.mjakubowski84.parquet4s.ParquetSchemaResolver.TypedSchemaDef
import com.github.mjakubowski84.parquet4s.{ Value, _ }
import org.apache.parquet.schema.{ LogicalTypeAnnotation, PrimitiveType }
import zio.prelude.fx._
import zio.prelude.{ Assertion, QuotedAssertion, Subtype, ZValidation }

package object exec {

  implicit val StepMonadStep: MonadStep[ZPure[Nothing, RNG, RNG, (Comparison, Eval[NonEmptyVector]), Exception, +*]] =
    new MonadStep[Step[+*]] {
      def liftR[A](r: RVar[A]): Step[A] =
        Step.liftR(r)
    }

  implicit def StepSMonadStep[S]
    : MonadStep[ZPure[Nothing, (RNG, S), (RNG, S), (Comparison, Eval[NonEmptyVector]), Exception, +*]] =
    new MonadStep[StepS[S, +*]] {
      def liftR[A](r: RVar[A]): StepS[S, A] =
        StepS.liftR(r)
    }

  object Name extends Subtype[String] {
    override def assertion: QuotedAssertion[String] = assert {
      Assertion.hasLength(Assertion.greaterThanOrEqualTo(1))
    }

    implicit val nameCodec: RequiredValueCodec[Name] =
      new RequiredValueCodec[Name] {
        override protected def decodeNonNull(value: Value, configuration: ValueCodecConfiguration): Name =
          value match {
            case BinaryValue(binary) =>
              Name.make(binary.toStringUsingUTF8()) match {
                case ZValidation.Failure(_, error) => sys.error(error.toString)
                case ZValidation.Success(_, value) => value
              }
            case _                   => sys.error("Value found that is not binary")
          }

        override protected def encodeNonNull(data: Name, configuration: ValueCodecConfiguration): Value =
          BinaryValue(Name.unwrap(data))
      }

    implicit val nameTypeSchema: TypedSchemaDef[Name] = // Save the data as a String in the schema
      SchemaDef
        .primitive(
          primitiveType = PrimitiveType.PrimitiveTypeName.BINARY,
          required = true,
          logicalTypeAnnotation = Some(LogicalTypeAnnotation.stringType())
        )
        .typed[Name]
  }

  type Name = Name.Type

}
