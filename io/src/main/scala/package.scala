package cilib

import scalaz._
import Scalaz._
import scalaz.stream._
import scalaz.concurrent.Task

import org.apache.parquet.hadoop.metadata.CompressionCodecName

package object io extends io.Avro4sInstances {
  import cilib.exec._
  import EncodeCsv._

  def writeCsvWithHeader[F[_], A](file: java.io.File, data: F[A])(implicit F: Foldable[F],
                                                                  N: ColumnNameEncoder[A],
                                                                  A: EncodeCsv[A]): Unit = {
    val list = F.toList(data)

    list.headOption match {
      case None =>
        println("No data to persist")

      case Some(o) =>
        val pw = new java.io.PrintWriter(file, "UTF-8")

        pw.println(N.encode(o).mkString(","))

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

  def csvSink[A: EncodeCsv](file: java.io.File)(implicit A: EncodeCsv[Measurement[A]]): Sink[Task, Measurement[A]] = {
    val fileWriter = new java.io.PrintWriter(file)

    sink
      .lift { (input: Measurement[A]) =>
        val encoded = A.encode(input)
        Task.delay(fileWriter.println(encoded.mkString(",")))
      }
      .onComplete(Process.eval_(Task.delay(fileWriter.close())))
  }

  def csvHeaderSink[A: EncodeCsv](file: java.io.File)(implicit A: EncodeCsv[Measurement[A]],
                                                      N: ColumnNameEncoder[Measurement[A]]): Sink[Task, Measurement[A]] = {
    val fileWriter = new java.io.PrintWriter(file)
    var headerWritten = false

    sink
      .lift { (input: Measurement[A]) =>
        if (!headerWritten) {
          fileWriter.println(N.encode(input).mkString(","))
          headerWritten = true
        }

        val encoded = A.encode(input)
        Task.delay(fileWriter.println(encoded.mkString(",")))
      }
      .onComplete(Process.eval_(Task.delay(fileWriter.close())))
  }


  import org.apache.hadoop.conf.Configuration
  import org.apache.hadoop.fs.Path
  import com.sksamuel.avro4s._
  import org.apache.parquet.avro._
  import org.apache.avro.generic.GenericRecord

  def writeParquet[F[_]: Foldable, A: SchemaFor:Encoder](file: java.io.File, data: F[A]): Unit = {
    val testConf = new Configuration
    val schema = AvroSchema[A]

    val encoder = Encoder[A]
    val path = new Path(file.getAbsolutePath)

    val writer = AvroParquetWriter
      // Why does the Encoder instance in avro4s lose type information????? AnyRef is a HORRIBLE type
      .builder[AnyRef](path)
      .withSchema(schema)
      .withCompressionCodec(CompressionCodecName.SNAPPY)
      .withConf(testConf)
      .withPageSize(4 * 1024 * 1024) // For compression
      .withRowGroupSize(16 * 1024 * 1024) // For write buffering (Page size)
      .build()

    Foldable[F].toList(data).foreach(item => writer.write(encoder.encode(item, schema, DefaultFieldMapper)))

    writer.close()
  }

  def parquetSink[A:Encoder:SchemaFor](file: java.io.File): Sink[Task, Measurement[A]] = {
    val testConf = new Configuration
    val schema = AvroSchema[Measurement[A]]
    val path = new Path(file.getAbsolutePath)
    val writer = AvroParquetWriter
      // Why does the Encoder instance in avro4s lose type information????? AnyRef is a HORRIBLE type
      .builder[AnyRef](path)
      .withSchema(schema)
      .withCompressionCodec(CompressionCodecName.SNAPPY)
      .withConf(testConf)
      .withPageSize(4 * 1024 * 1024) // For compression
      .withRowGroupSize(16 * 1024 * 1024) // For write buffering (Page size)
      .build()

    val encoder = Encoder[Measurement[A]]
    val complete = Process.eval_(Task.delay(writer.close))

    sink
      .lift { (input: Measurement[A]) =>
        val record = encoder.encode(input, schema, DefaultFieldMapper)
        Task.delay(writer.write(record))
      }
      .onComplete(complete)
  }

}
