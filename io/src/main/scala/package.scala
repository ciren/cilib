package cilib

import scalaz.Foldable
import scalaz.stream._
import scalaz.concurrent.Task
import com.sksamuel.avro4s._
import org.apache.parquet.avro._
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

package object io {
  import EncodeCsv._

  def writeCsvWithHeader[F[_], A](file: java.io.File, data: F[A])(implicit F: Foldable[F],
                                                                  N: ColumnNames[A],
                                                                  A: EncodeCsv[A]): Unit = {
    val list = F.toList(data)

    list.headOption match {
      case None =>
        println("No data to persist")

      case Some(o) =>
        val pw = new java.io.PrintWriter(file, "UTF-8")

        pw.println(N.names(o).mkString(","))

        list.foreach(item => {
          val encoded = EncodeCsv.write(item)
          pw.println(encoded)
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
          pw.println(encoded)
        })

        pw.close
    }
  }

  def csvSink[A: EncodeCsv](file: java.io.File)(implicit A: EncodeCsv[A]): Sink[Task, A] = {
    val fileWriter = new java.io.PrintWriter(file)

    sink
      .lift { (input: A) =>
        val encoded = A.encode(input)
        Task.delay(fileWriter.println(encoded.mkString(",")))
      }
      .onComplete(Process.eval_(Task.delay(fileWriter.close())))
  }

  def csvHeaderSink[A: EncodeCsv](file: java.io.File)(implicit A: EncodeCsv[A],
                                                      N: ColumnNames[A]): Sink[Task, A] = {
    val fileWriter = new java.io.PrintWriter(file)
    var headerWritten = false

    sink
      .lift { (input: A) =>
        if (!headerWritten) {
          fileWriter.println(N.names(input).mkString(","))
          headerWritten = true
        }

        val encoded = A.encode(input)
        Task.delay(fileWriter.println(encoded.mkString(",")))
      }
      .onComplete(Process.eval_(Task.delay(fileWriter.close())))
  }

  def writeParquet[F[_]: Foldable, A: SchemaFor: ToRecord](file: java.io.File, data: F[A])(
      implicit T: ToRecord[A]) = {
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

  def parquetSink[A: SchemaFor: ToRecord](file: java.io.File)(
      implicit toRecord: ToRecord[A]): Sink[Task, A] = {
    val testConf = new Configuration
    val schema = AvroSchema[A]

    println("schema: " + schema)

    val path = new Path(file.getAbsolutePath)

    val writer = AvroParquetWriter
      .builder[GenericRecord](path)
      .withSchema(schema)
      .withConf(testConf)
      .build()

    val complete = Process.eval_(Task.delay(writer.close))

    sink
      .lift { (input: A) =>
        Task.delay(writer.write(toRecord.apply(input)))
      }
      .onComplete(complete)
  }

}
