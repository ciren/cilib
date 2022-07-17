package cilib

import com.github.mjakubowski84.parquet4s._
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import zio._
import zio.prelude._
import zio.stream._

package object io {
  import cilib.exec._

  def writeCsvWithHeader[F[+_], A: EncodeCsv](
    file: java.io.File,
    data: F[A]
  )(implicit F: ForEach[F], N: ColumnNameEncoder[A]): Unit = {
    val list: List[A] = F.toList(data)

    list.headOption match {
      case None =>
        println("No data to persist")

      case Some(o) =>
        val pw = new java.io.PrintWriter(file, "UTF-8")

        pw.println(N.encode(o).mkString(","))

        list.foreach { item =>
          val encoded = EncodeCsv.write(item)
          pw.println(encoded)
        }

        pw.close
    }
  }

  def writeCsv[F[+_], A: EncodeCsv](file: java.io.File, data: F[A])(implicit F: ForEach[F]): Unit = {
    val list: List[A] = F.toList(data)

    list.headOption match {
      case None =>
        println("No data to persist")

      case Some(_) =>
        val pw = new java.io.PrintWriter(file, "UTF-8")

        list.foreach { item =>
          val encoded = EncodeCsv.write(item)
          pw.println(encoded)
        }

        pw.close
    }
  }

  def csvSink[A: EncodeCsv](
    file: java.io.File
  )(implicit A: EncodeCsv[Measurement[A]]): ZSink[Any, Throwable, Measurement[A], Measurement[A], Unit] =
    ZSink.unwrap {
      ZIO.acquireReleaseWith(ZIO.succeed(new java.io.PrintWriter(file)))(chan =>
        ZIO.attemptBlocking(ZIO.succeed(chan.close())).orDie
      ) { printWriter =>
        ZIO.succeed {
          ZSink.foreach((measurement: Measurement[A]) =>
            ZIO.attemptBlocking {
              ZIO.succeed(printWriter.println(A.encode(measurement).mkString(",")))
            }
          )
        }
      }

      // val managedChannel = ZManaged.a
      //   //blocking.effectBlockingInterrupt {
      //     new java.io.PrintWriter(file)
      //   //}
      // )(chan => chan.close())//blocking.effectBlocking(chan.close()).orDie)

      // val writer: ZSink[Blocking, Throwable, Measurement[A], Measurement[A], Unit] =
      //   ZSink.managed(managedChannel) { chan =>
      //     ZSink.foreach[Blocking, Throwable, Measurement[A]](measurement =>
      //       blocking.effectBlockingInterrupt {
      //         chan.println(A.encode(measurement).mkString(","))
      //       }
      //     )
      //   }

      // writer
    }

  def csvHeaderSink[A: EncodeCsv](
    file: java.io.File
  )(implicit
    A: EncodeCsv[Measurement[A]],
    N: ColumnNameEncoder[Measurement[A]]
  ): ZSink[Any, Throwable, Measurement[A], Measurement[A], Unit] =
    //  ): ZSink[Blocking, Throwable, Measurement[A], Measurement[A], Unit] = {
    // val managedChannel = ZManaged.make(
    //   blocking.effectBlockingInterrupt {
    //     new java.io.PrintWriter(file)
    //   }
    // )(chan => blocking.effectBlocking(chan.close()).orDie)

    // val writer: ZSink[Blocking, Throwable, Measurement[A], Measurement[A], Unit] =
    //   ZSink.managed(managedChannel) { chan =>
    //     var headerWritten = false

    //     ZSink.foreach[Blocking, Throwable, Measurement[A]](measurement =>
    //       blocking.effectBlockingInterrupt {
    //         if (!headerWritten) {
    //           chan.println(N.encode(measurement).mkString(","))
    //           headerWritten = true
    //         }

    //         chan.println(A.encode(measurement).mkString(","))
    //       }
    //     )
    //   }

    // writer
    ZSink.unwrap {
      ZIO.acquireReleaseWith(ZIO.succeed(new java.io.PrintWriter(file)))(writer => ZIO.succeed(writer.close())) {
        printWriter =>
          ZIO.succeed {
            var headerWritten = false

            ZSink.foreach { (measurement: Measurement[A]) =>
              if (!headerWritten) {
                printWriter.println(N.encode(measurement).mkString(","))
                headerWritten = true
              }

              ZIO.succeed(printWriter.println(A.encode(measurement).mkString(",")))
            }
          }
      }
    }

  val parquetOptions: ParquetWriter.Options = ParquetWriter.Options(
    compressionCodecName = CompressionCodecName.SNAPPY,
    pageSize = 4 * 1024 * 1024,
    rowGroupSize = 16 * 1024 * 1024
  )

  def writeParquet[F[+_], A: ParquetRecordEncoder: ParquetSchemaResolver](
    file: java.io.File,
    data: F[A]
  )(implicit
    F: ForEach[F],
    encoder: ParquetRecordEncoder[Measurement[A]],
    schema: ParquetSchemaResolver[Measurement[A]]
  ): Unit = {
    val list: List[A] = F.toList(data)

    ParquetWriter
      .of[A]
      .options(parquetOptions)
      .writeAndClose(Path(file.getAbsolutePath), list)
  }

  def parquetSink[A: ParquetRecordEncoder: ParquetSchemaResolver](file: java.io.File)(implicit
    encoder: ParquetRecordEncoder[Measurement[A]],
    schema: ParquetSchemaResolver[Measurement[A]]
  ): ZSink[Any, Throwable, Measurement[A], Measurement[A], Unit] =
    ZSink.unwrap {
      val parquetWriter = ZIO.succeed {
        ParquetWriter
          .of[Measurement[A]]
          .options(parquetOptions)
          .build(Path(file.getAbsolutePath))
      }

      ZIO.acquireReleaseWith(parquetWriter)(writer => ZIO.succeed(writer.close())) { writer =>
        ZIO.succeed {
          ZSink.foreach((measurement: Measurement[A]) => ZIO.succeed(writer.write(measurement)))
        }
      }
    }

  // ): ZSink[Blocking, Throwable, Measurement[A], Measurement[A], Unit] = {
  //   val managedChannel = ZManaged.make(
  //     blocking.effectBlockingInterrupt {
  //       ParquetWriter
  //         .of[Measurement[A]]
  //         .options(parquetOptions)
  //         .build(Path(file.getAbsolutePath))
  //     }
  //   )(chan => blocking.effectBlocking(chan.close()).orDie)

  //   val writer: ZSink[Blocking, Throwable, Measurement[A], Measurement[A], Unit] =
  //     ZSink.managed(managedChannel) { chan =>
  //       ZSink.foreach[Blocking, Throwable, Measurement[A]](measurement =>
  //         blocking.effectBlockingInterrupt {
  //           chan.write(measurement)
  //         }
  //       )
  //     }

  //   writer
  // }

}
