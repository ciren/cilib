package cilib

import com.github.mjakubowski84.parquet4s._
import org.apache.parquet.hadoop.ParquetFileWriter
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import zio._
import zio.prelude._
import zio.stream._

package object io {
  import cilib.exec._

  def parquetOptions(writeMode: WriteMode): ParquetWriter.Options = {
    val hadoopWriteMode =
      writeMode match {
        case Create    => ParquetFileWriter.Mode.CREATE
        case Overwrite => ParquetFileWriter.Mode.OVERWRITE
      }

    ParquetWriter.Options(
      writeMode = hadoopWriteMode,
      compressionCodecName = CompressionCodecName.SNAPPY,
      pageSize = 4 * 1024 * 1024,
      rowGroupSize = 16 * 1024 * 1024L
    )
  }

  def writeParquet[F[+_], A: ParquetRecordEncoder: ParquetSchemaResolver](
    file: java.io.File,
    data: F[A],
    writeMode: WriteMode
  )(implicit
    F: ForEach[F],
    encoder: ParquetRecordEncoder[Measurement[A]],
    schema: ParquetSchemaResolver[Measurement[A]]
  ): Unit = {
    val list: List[A] = F.toList(data)

    ParquetWriter
      .of[A]
      .options(parquetOptions(writeMode))
      .writeAndClose(Path(file.getAbsolutePath), list)
  }

  def parquetSink[A](file: java.io.File, writeMode: WriteMode = Create)(implicit
    encoder: ParquetRecordEncoder[Measurement[A]],
    schema: ParquetSchemaResolver[Measurement[A]]
  ): ZSink[Any, Throwable, Measurement[A], Measurement[A], Unit] = {
    def close(resource: java.io.Closeable): UIO[Unit] =
      ZIO.attempt(resource.close()).orDie

    def openParquet =
      ZIO.acquireRelease(
        ZIO.attemptBlockingIO(
          ParquetWriter
            .of[Measurement[A]]
            .options(parquetOptions(writeMode))
            .build(Path(file.getAbsolutePath))
        )
      )(close)

    ZSink.unwrapScoped {
      for {
        writer <- openParquet
      } yield ZSink.foreach((measurement: Measurement[A]) => ZIO.attemptBlockingIO(writer.write(measurement)))
    }
  }
}
