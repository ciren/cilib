package cilib
package io

@annotation.implicitNotFound(
  """Not all members of type ${A} have instances of EncodeCsv defined or in scope.
It is recommended to examine the fields of ${A} and then to define
an instance of EncodeCsv for the custom parameter type. The predefined
known instances are for the following types: Boolean, Byte, Short,
Int, Long, FLoat, Double, String, and Foldable[_] types such as List""")
trait EncodeCsv[A] {
  def encode(a: A): List[String]
}

object EncodeCsv {
  import scalaz.Foldable
  import shapeless._
  import shapeless.labelled._

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
    EncodeCsv[F[A]](l =>
      List(F.toList(l).flatMap(A.encode).mkString("[", ",", "]")))

  implicit def genericEncodeCsv[A,R](
    implicit gen: Generic.Aux[A,R],
    enc: EncodeCsv[R]
  ): EncodeCsv[A] =
    EncodeCsv(a => enc.encode(gen.to(a)))

  // HList induction
  implicit val hnilToEncodeCsv: EncodeCsv[HNil] =
    EncodeCsv(_ => Nil)

  implicit def hconsToEncodeCsv[H, T <: HList](implicit
    hEncode: EncodeCsv[H],
    tEncode: EncodeCsv[T]
  ): EncodeCsv[H :: T] =
    EncodeCsv {
      case h :: t => hEncode.encode(h) ++ tEncode.encode(t)
    }

  def write[A](a: A)(implicit enc: EncodeCsv[A]): String =
    enc.encode(a).mkString(",")


  /* Header column name derivations */
  trait ColumnName[L <: HList] { def apply(l: L): List[String] }

  trait LowPriorityColumnName {
    implicit def hconsColumnName1[K <: Symbol, V, T <: HList](
      implicit wit: Witness.Aux[K],
      columnName: ColumnName[T]
    ): ColumnName[FieldType[K, V] :: T] = new ColumnName[FieldType[K, V] :: T] {
      def apply(l: FieldType[K, V] :: T): List[String] =
        List(wit.value.name) ++ columnName(l.tail)
    }
  }

  object ColumnName extends LowPriorityColumnName {
    implicit val hnilColumnName: ColumnName[HNil] = new ColumnName[HNil] {
      def apply(l: HNil): List[String] =
        List.empty[String]
    }

    implicit def hconsColumnName[K <: Symbol, V, R <: HList, T <: HList](
      implicit lgen: LabelledGeneric.Aux[V,R],
      headColumnName: ColumnName[R],
      tailColumnName: ColumnName[T]
    ): ColumnName[FieldType[K,V] :: T] = new ColumnName[FieldType[K,V] :: T] {
      def apply(l: FieldType[K,V] :: T): List[String] =
        headColumnName(lgen.to(l.head)) ++ tailColumnName(l.tail)
    }
  }

  def writeHeaders[A, L <: HList](a: A)(
    implicit lgen: LabelledGeneric.Aux[A, L],
    colName: Lazy[ColumnName[L]]
  ) = colName.value.apply(lgen.to(a)).mkString(",")
}
