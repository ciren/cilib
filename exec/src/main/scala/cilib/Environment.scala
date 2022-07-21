package cilib
package exec

import com.github.mjakubowski84.parquet4s._
import zio.stream._

sealed abstract class Env
final case object Unchanged extends Env
final case object Change    extends Env

object Env {
  def unchanging: zio.stream.UStream[Env] =
    ZStream(Unchanged: Env) ++ unchanging

  def frequency[A](n: Long): UStream[Env] =
    unchanging.take(n - 1) ++ ZStream(Change: Env) ++ frequency(n)

  implicit val envTypeCodec: RequiredValueCodec[Env] =
    new RequiredValueCodec[Env] {
      override protected def decodeNonNull(value: Value, configuration: ValueCodecConfiguration): Env =
        value match {
          case BinaryValue(binary) =>
            binary.toStringUsingUTF8 match {
              case "Unchanged" => Unchanged
              case "Change"    => Change
            }
          case _                   => sys.error("Impossible state: Not possible to decode an Env type from data file")
        }

      override protected def encodeNonNull(data: Env, configuration: ValueCodecConfiguration): Value =
        data match {
          case Unchanged => BinaryValue("Unchanged")
          case Change    => BinaryValue("Change")
        }
    }

  import com.github.mjakubowski84.parquet4s.TypedSchemaDef
  import org.apache.parquet.schema.{ LogicalTypeAnnotation, PrimitiveType }

  implicit val envTypeSchema: TypedSchemaDef[Env] = // Save the data as a String in the schema
    SchemaDef
      .primitive(
        primitiveType = PrimitiveType.PrimitiveTypeName.BINARY,
        required = true,
        logicalTypeAnnotation = Some(LogicalTypeAnnotation.stringType())
      )
      .typed[Env]
}
