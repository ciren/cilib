package cilib

import scalaz._
import Scalaz._
import scalaz.stream._
import scalaz.concurrent.Task

import org.apache.parquet.hadoop.metadata.CompressionCodecName
import com.github.mjakubowski84.parquet4s._

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


  def writeParquet[F[_]:Foldable, A: ParquetRecordEncoder : ParquetSchemaResolver](file: java.io.File, data: F[A])(implicit encoder: ParquetRecordEncoder[Measurement[A]], schema: ParquetSchemaResolver[Measurement[A]]): Unit = {
    val options = ParquetWriter.Options(compressionCodecName = CompressionCodecName.SNAPPY,
      pageSize = 4 * 1024 * 1024,
      rowGroupSize = 16 * 1024 * 1024
    )

    ParquetWriter.writeAndClose(file.getAbsolutePath, data.toList, options)
  }


  def parquetSink[A: ParquetRecordEncoder : ParquetSchemaResolver](file: java.io.File)(implicit encoder: ParquetRecordEncoder[Measurement[A]], schema: ParquetSchemaResolver[Measurement[A]]): Sink[Task, Measurement[A]] = {
    val options = ParquetWriter.Options(compressionCodecName = CompressionCodecName.SNAPPY,
      pageSize = 4 * 1024 * 1024,
      rowGroupSize = 16 * 1024 * 1024
    )
    val writer = ParquetWriter.writer[Measurement[A]](file.getAbsolutePath, options)
    val complete = Process.eval_(Task.delay(writer.close))

    sink.lift { (input: Measurement[A]) => Task.delay(writer.write(input)) }
      .onComplete(complete)
  }


}
