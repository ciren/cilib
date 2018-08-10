package cilib
package io

import cilib.exec.{ Env, Change, Unchanged }

@annotation.implicitNotFound("""
EncodeCsv derivation error for type: ${A}
Not all members of the type have instances of EncodeCsv defined, or in
scope. It is recommended to examine the fields of the type and then to
define an instance of EncodeCsv for the any custom parameter type. The
pre-defined, known, instances exist for the following types:
Boolean, Byte, Short, Int, Long, FLoat, Double, String, Env, and
Foldable[_] types such as List""")
trait EncodeCsv[A] {
  def encode(a: A): List[String]
}

object EncodeCsv {
  import scalaz.Foldable
  import shapeless._

  final def createEncoder[A](f: A => List[String]) = new EncodeCsv[A] {
    def encode(a: A) = f(a)
  }

  implicit val booleanCsvEncode = createEncoder[Boolean](x => List(x.toString))
  implicit val byteEncodeCsv = createEncoder[Byte](x => List(x.toString))
  implicit val shortEncodeCsv = createEncoder[Short](x => List(x.toString))
  implicit val intEncodeCsv = createEncoder[Int](x => List(x.toString))
  implicit val longEncodeCsv = createEncoder[Long](x => List(x.toString))
  implicit val floatEncodeCsv = createEncoder[Float](x => List(x.toString))
  implicit val doubleEncodeCsv = createEncoder[Double](x => List(x.toString))
  implicit val stringEncodeCsv = createEncoder[String](List(_))

  implicit def foldableEncodeCsv[F[_], A](implicit F: Foldable[F], A: EncodeCsv[A]) =
    createEncoder[F[A]](l => List(F.toList(l).flatMap(A.encode).mkString("[", ",", "]")))

  implicit val envEncodeCsv =
    createEncoder[Env](_ match {
      case Unchanged => List("Unchanged")
      case Change => List("Changed")
    })

  implicit def genericEncodeCsv[A, H](
      implicit gen: Generic.Aux[A, H],
      enc: Lazy[EncodeCsv[H]]
  ): EncodeCsv[A] =
    createEncoder(a => enc.value.encode(gen.to(a)))

  // HList induction
  implicit val hnilToEncodeCsv: EncodeCsv[HNil] =
    createEncoder(_ => Nil)

  implicit def hconsToEncodeCsv[H, T <: HList](implicit
                                               hEncode: EncodeCsv[H],
                                               tEncode: EncodeCsv[T]): EncodeCsv[H :: T] =
    createEncoder {
      case h :: t => hEncode.encode(h) ++ tEncode.encode(t)
    }

  @inline def apply[A](implicit c: EncodeCsv[A]): EncodeCsv[A] = c

  def write[A](a: A)(implicit enc: EncodeCsv[A]): String =
    enc.encode(a).mkString(",")
}






@annotation.implicitNotFound("Implicit ColumnNameEncoder instance is missing for the provided type ${A}")
trait ColumnNameEncoder[A] {
  def encode(a: A): List[String]
}

object ColumnNameEncoder {

  @inline def apply[A](implicit c: ColumnNameEncoder[A]): ColumnNameEncoder[A] = c

  def createEncoder[A](f: A => List[String]): ColumnNameEncoder[A] =
    new ColumnNameEncoder[A] {
      def encode(a: A) = f(a)
    }

  import shapeless._
  import shapeless.LabelledGeneric
  import shapeless.Witness
  import shapeless.labelled.FieldType

  implicit val booleanColumnNameEncoder = createEncoder((_: Boolean) => List.empty)
  implicit val byteColumnNameEncoder = createEncoder((_: Byte) => List.empty)
  implicit val shortColumnNameEncoder = createEncoder((_: Short) => List.empty)
  implicit val intColumnNameEncoder = createEncoder((_: Int) => List.empty)
  implicit val longColumnNameEncoder = createEncoder((_: Long) => List.empty)
  implicit val floatColumnNameEncoder = createEncoder((_: Float) => List.empty)
  implicit val doubleColumnNameEncoder = createEncoder((_: Double) => List.empty)
  implicit val stringColumnNameEncoder = createEncoder((_: String) => List.empty)
  implicit val envEncodeCsv = createEncoder((e: Env) => List.empty)

  implicit val hnilColumnNameEncoder: ColumnNameEncoder[HNil] =
    createEncoder(_ => List.empty)

  implicit def hconsColumnNameEncoder[K <: Symbol, H, T <: HList](
    implicit
      witness: Witness.Aux[K],
      hEncoder: Lazy[ColumnNameEncoder[H]],
    tEncoder: ColumnNameEncoder[T]
  ): ColumnNameEncoder[shapeless.::[FieldType[K, H], T]] = {
    val fieldName: String = witness.value.name

    createEncoder { hlist =>
      val head: List[String] = hEncoder.value.encode(hlist.head)
      val tail: List[String] = tEncoder.encode(hlist.tail)


      (if (head.isEmpty) List(fieldName) else head) ++ tail
    }
  }

  implicit def genericProductEncoder[A, H](
  implicit
  generic: LabelledGeneric.Aux[A, H],
  hEncoder: Lazy[ColumnNameEncoder[H]]
): ColumnNameEncoder[A] =
  createEncoder { value =>
    hEncoder.value.encode(generic.to(value))
  }
}
