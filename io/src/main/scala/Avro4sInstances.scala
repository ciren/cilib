package cilib
package io

import scala.collection.JavaConverters._

import com.sksamuel.avro4s._
import org.apache.avro.{Schema, SchemaBuilder}
import org.apache.avro.generic.GenericRecord

import scalaz._
import Scalaz._

trait Avro4sInstances {

  // NonEmptyList
  implicit def nonEmptyListEncoder[A](implicit encoder: Encoder[A]) =
    new Encoder[NonEmptyList[A]] {
      def encode(ts: NonEmptyList[A], schema: Schema, fieldMapper: FieldMapper): java.util.List[AnyRef] = {
        require(schema != null)
        ts.map(encoder.encode(_, schema.getElementType, fieldMapper)).toList.asJava
      }
    }

  implicit def nonEmptyListDecoder[T](implicit decoder: Decoder[T]) =
    new Decoder[NonEmptyList[T]] {
      override def decode(value: Any, schema: Schema, fieldMapper: FieldMapper): NonEmptyList[T] =
        value match {
          case array: Array[_] => array.toList.map(decoder.decode(_, schema, fieldMapper)).toNel.get
          case list: java.util.Collection[_] =>
            list.asScala.map(decoder.decode(_, schema, fieldMapper)).toList.toNel.get
          case other => sys.error("Unsupported type " + other)
        }
    }

  implicit def nonEmptyListSchemaFor[T](implicit schemaFor: SchemaFor[T]): SchemaFor[NonEmptyList[T]] =
    new SchemaFor[NonEmptyList[T]] {
      def schema(fieldMapper: FieldMapper): Schema =
        Schema.createArray(schemaFor.schema(fieldMapper))
    }

}
