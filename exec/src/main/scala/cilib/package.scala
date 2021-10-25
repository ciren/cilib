package cilib

import com.github.mjakubowski84.parquet4s.ParquetSchemaResolver.TypedSchemaDef
import com.github.mjakubowski84.parquet4s.{ Value, ValueCodec, _ }
import org.apache.parquet.schema.{ LogicalTypeAnnotation, PrimitiveType }
import zio.prelude.ZValidation
import zio.prelude.fx._
import zio.test.Assertion

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

  object Name extends zio.prelude.NewtypeSmart[String](Assertion.hasSizeString(Assertion.isGreaterThanEqualTo(1))) {
    // def assertion = assert {
    //   Assertion.hasLength(Assertion.greaterThanOrEqualTo(1))
    // }
    implicit val nameCodec: RequiredValueCodec[Name] =
      new RequiredValueCodec[Name] {
        val stringCodec = implicitly[ValueCodec[String]]

        override protected def decodeNonNull(value: Value, configuration: ValueCodecConfiguration): Name =
          Name.make(stringCodec.decode(value, configuration)) match {
            case ZValidation.Failure(_, error) => sys.error(error.toString)
            case ZValidation.Success(_, value) => value
          }

        override protected def encodeNonNull(data: Name, configuration: ValueCodecConfiguration): Value =
          stringCodec.encode(Name.unwrap(data), configuration)
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
