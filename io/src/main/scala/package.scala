package cilib

import scalaz.Foldable
import com.sksamuel.avro4s._
import org.apache.parquet.avro._
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

package object io {
  import EncodeCsv._

  def writeCsvWithHeader[F[_], A](file: java.io.File, data: F[A])(implicit F: Foldable[F], N: ColumnNames[A], A: EncodeCsv[A]): Unit = {
    val list = F.toList(data)

    list.headOption match {
      case None =>
        println("No data to persist")

      case Some(o) =>
        val pw = new java.io.PrintWriter(file, "UTF-8")

        pw.write(N.names(o).mkString("", ",", "\n"))

        list.foreach(item => {
          val encoded = EncodeCsv.write(item)
          pw.write(s"$encoded\n")
        })

        pw.close
    }
  }


  def writeCsv[F[_]: Foldable, A: EncodeCsv](file: java.io.File, data: F[A]): Unit = {
    val list = Foldable[F].toList(data)

    list.headOption match {
      case None =>
        println("No data to persist")

      case Some(_) =>
        val pw = new java.io.PrintWriter(file, "UTF-8")

        list.foreach(item => {
          val encoded = EncodeCsv.write(item)
          pw.write(s"$encoded\n")
        })

        pw.close
    }
  }

  def writeParquet[F[_]:Foldable,A:SchemaFor:ToRecord](file: java.io.File, data: F[A])(implicit T: ToRecord[A]) = {
    val testConf = new Configuration
    val schema = AvroSchema[A]

    val path = new Path(file.getAbsolutePath)

    val writer = AvroParquetWriter
      .builder[GenericRecord](path)
      .withSchema(schema)
      .withConf(testConf)
      .build()

    Foldable[F].toList(data).foreach(item => writer.write(T.apply(item)))

    writer.close()
  }

}
