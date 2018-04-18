import sbt._
import sbt.util.CacheStoreFactory
import java.io.File

object BlogPostProcessing {

  def generate(cache: File, inR: File, outR: File) = {

    def handleUpdate(inReport: ChangeReport[File], outReport: ChangeReport[File]): Set[File] = {
      val in = (inReport.modified -- inReport.removed).toList
      println(in)
      val out = in.map { in =>
        val inDir = if (in.isDirectory) in else in.getParentFile  // input dir
        new File(outR, inR.toURI.relativize(inDir.toURI).getPath) // output dir
      }

      in.zip(out).flatMap { case (in, out) => generateFile(out, in) }.toSet ++
        Set(generateIndex(in, outR))
    }

    val files = safeListFiles(inR, recurse = true).toSet

    FileFunction.cached(CacheStoreFactory(cache), FilesInfo.hash, FilesInfo.exists)(handleUpdate)(files)
  }

  def safeListFiles(dir: File, recurse: Boolean): List[File] =
    Option(dir.listFiles).fold(List.empty[File]){ files =>
      val l = files.toList
      if (recurse) l.flatMap(flatten) else l
    }

  def flatten(f: File): List[File] =
    f :: (if (f.isDirectory) f.listFiles.toList.flatMap(flatten) else Nil)

  def generateFile(target: File, input: File): Option[File] = {
    println("Generating blog post for file " + input.getName)

    val split = """^(\d{4})-(\d{2})-(\d{2})-(.+)$""".r

    input.getName match {
      case split(year, month, day, topic) =>
        val outputFile = new File(target, s"$year/$month/$day/$topic")
        outputFile.getParentFile.mkdirs()
        IO.copyFile(input, outputFile)
        Some(outputFile)
      case _ =>
        println("Post generation failed for file: " + input.getAbsolutePath)
        None
    }
  }

  def generateIndex(articles: List[File], output: File): File = {
    val index = new File(output, "index.md")

    index
  }
}
