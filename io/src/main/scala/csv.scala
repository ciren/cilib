package cilib
package io

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

  final def apply[A](f: A => List[String]) = new EncodeCsv[A] {
    def encode(a: A) = f(a)
  }

  implicit val booleanCsvEncode = EncodeCsv[Boolean](x => List(x.toString))
  implicit val byteEncodeCsv = EncodeCsv[Byte](x => List(x.toString))
  implicit val shortEncodeCsv = EncodeCsv[Short](x => List(x.toString))
  implicit val intEncodeCsv = EncodeCsv[Int](x => List(x.toString))
  implicit val longEncodeCsv = EncodeCsv[Long](x => List(x.toString))
  implicit val floatEncodeCsv = EncodeCsv[Float](x => List(x.toString))
  implicit val doubleEncodeCsv = EncodeCsv[Double](x => List(x.toString))
  implicit val stringEncodeCsv = EncodeCsv[String](List(_))

  implicit def foldableEncodeCsv[F[_], A](implicit F: Foldable[F], A: EncodeCsv[A]) =
    EncodeCsv[F[A]](l => List(F.toList(l).flatMap(A.encode).mkString("[", ",", "]")))

  implicit val envEncodeCsv =
    EncodeCsv[cilib.exec.Env](_ match {
      case cilib.exec.Unchanged => List("Unchanged")
      case cilib.exec.Change => List("Changed")
    })

  implicit def genericEncodeCsv[A, R](
      implicit gen: Generic.Aux[A, R],
      enc: Lazy[EncodeCsv[R]]
  ): EncodeCsv[A] =
    EncodeCsv(a => enc.value.encode(gen.to(a)))

  // HList induction
  implicit val hnilToEncodeCsv: EncodeCsv[HNil] =
    EncodeCsv(_ => Nil)

  implicit def hconsToEncodeCsv[H, T <: HList](implicit
                                               hEncode: EncodeCsv[H],
                                               tEncode: EncodeCsv[T]): EncodeCsv[H :: T] =
    EncodeCsv {
      case h :: t => hEncode.encode(h) ++ tEncode.encode(t)
    }

  def write[A](a: A)(implicit enc: EncodeCsv[A]): String =
    enc.encode(a).mkString(",")
}

@annotation.implicitNotFound("Implicit instance is missing for the provided type ${A}")
trait ColumnNames[A] {
  def names(a: A): List[String]
}

object ColumnNames {
  import scalaz.Foldable
  import shapeless._
  import shapeless.labelled._

  @inline def apply[A](implicit c: ColumnNames[A]): ColumnNames[A] = c

  def columnName[A](f: A => List[String]): ColumnNames[A] =
    new ColumnNames[A] {
      def names(a: A) = f(a)
    }

  implicit val booleanColumnNames = columnName[Boolean](_ => List())
  implicit val byteColumnNames = columnName[Byte](_ => List())
  implicit val shortColumnNames = columnName[Short](_ => List())
  implicit val intColumnNames = columnName[Int](_ => List())
  implicit val longColumnNames = columnName[Long](_ => List())
  implicit val floatColumnNames = columnName[Float](_ => List())
  implicit val doubleColumnNames = columnName[Double](_ => List())
  implicit val stringColumnNames = columnName[String](_ => List())

  implicit def foldableCoilumnNames[F[_], A](implicit F: Foldable[F], A: ColumnNames[A]) =
    columnName[F[A]](l => List(F.toList(l).flatMap(A.names).mkString("[", ",", "]")))

  implicit val hnilColumnsNames: ColumnNames[HNil] =
    new ColumnNames[HNil] {
      def names(a: HNil) = List()
    }

  implicit def hconsColumnNames[K <: Symbol, H, T <: HList](
      implicit
      witness: Witness.Aux[K],
//      hNamer: ColumnNames[H],
      tNamer: ColumnNames[T]
  ): ColumnNames[FieldType[K, H] :: T] = {
    val fieldName: String = witness.value.name
    new ColumnNames[FieldType[K, H] :: T] {
      def names(a: FieldType[K, H] :: T) =
        List(fieldName) ++ tNamer.names(a.tail)
    }
  }

  implicit def genericColumnNames[A, H <: HList](
      implicit generic: LabelledGeneric.Aux[A, H],
      hNamer: Lazy[ColumnNames[H]]
  ): ColumnNames[A] =
    new ColumnNames[A] {
      def names(a: A) =
        hNamer.value.names(generic.to(a))
    }
}
