package cilib
package exec

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

import com.github.mjakubowski84.parquet4s._

sealed abstract class Env
final case object Unchanged extends Env
final case object Change extends Env

object Env {
  def constant: Stream[Env] =
    Stream(Unchanged) #::: constant

  def frequency[A](n: Int Refined Positive): Stream[Env] =
    (constant.take(n - 1) #::: Stream[Env](Change)) #::: frequency(n)


  implicit val envTypeCodec: RequiredValueCodec[Env] =
    new RequiredValueCodec[Env] {
      val stringCodec = implicitly[ValueCodec[String]]

      override protected def decodeNonNull(value: Value, configuration: ValueCodecConfiguration): Env =
        stringCodec.decode(value, configuration) match {
          case "Unchanged" => Unchanged
          case "Change" => Change
        }

      override protected def encodeNonNull(data: Env, configuration: ValueCodecConfiguration): Value =
        data match {
          case Unchanged => stringCodec.encode("Unchanged", configuration)
          case Change => stringCodec.encode("Change", configuration)
        }
    }

  import com.github.mjakubowski84.parquet4s.ParquetSchemaResolver._
  import org.apache.parquet.schema._

  implicit val envTypeSchema: TypedSchemaDef[Env] = { // Save the data as a String in the schema
    typedSchemaDef[Env](
      PrimitiveSchemaDef(
        primitiveType = PrimitiveType.PrimitiveTypeName.BINARY,
        required = true,
        originalType = Some(OriginalType.UTF8)
      )
    )
  }
}
