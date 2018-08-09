package ExifReader

import java.io.File
import org.rogach.scallop._
import scala.sys.process._

object ExifReaderApp {
  private val exifToolCommandSnippet: String = {
    val sb = new StringBuilder

    sb ++= "exiftool "
    sb ++= "-S "
    sb ++= "-Aperture "
    sb ++= "-ColorSpace "
    sb ++= "-CreateDate "
    sb ++= "-Directory "
    sb ++= "-FileName "
    sb ++= "-FocalLength "
    sb ++= "-ImageHeight "
    sb ++= "-ImageWidth "
    sb ++= "-LensID "
    sb ++= "-ISO "
    sb ++= "-Make "
    sb ++= "-Megapixels "
    sb ++= "-Model "
    sb ++= "-Orientation "
    sb ++= "-ShutterSpeed "

    sb.result
  }

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)

    val dir: File = new File(conf.path.getOrElse(""))

    if (!dir.exists) throw new IllegalArgumentException(s"Directory not found: $dir")

    // empty seq means all files
    val extensions: Seq[String] = conf.extensions.getOrElse(List.empty[String])

    println("Finding files...")

    val files: Seq[File] = FileUtil.getFilesOfType(dir, extensions)

    println(s"${files.size} files found")

    // todo: read and insert in batches? what about restart on fail?
    if (files.nonEmpty) {
      val photographs = makePhotographs(files)
      println("Inserting exif data...")
      Database.insertPhotographs(photographs)
    }

    println("Done.")
  }

  private def makePhotographs(files: Seq[File]) : Seq[Photograph] = {
    var currentFileCount: Int = 1

    val photographOptions: Seq[Option[Photograph]] = files.map{ file: File =>
      if (currentFileCount == 1 || currentFileCount % 10 == 0) println(s"Reading exif data for file $currentFileCount of ${files.size}")

      // todo: this is painfully slow - try using exiftool's batch read capability
      val exifToolCommand: String = s"$exifToolCommandSnippet${file.getAbsolutePath}"

      try {
        val result: String = exifToolCommand.!!
        Some(Photograph(result))
      } catch {
        case e: Exception =>
          System.err.println(e)
          None
      } finally {
        currentFileCount += 1
      }
    }

    photographOptions.filter{ _.isDefined }.map{ _.get }
  }
}

private class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val path: ScallopOption[String] = opt[String](required = true)
  val extensions: ScallopOption[List[String]] = trailArg[List[String]]()
  verify()
}
