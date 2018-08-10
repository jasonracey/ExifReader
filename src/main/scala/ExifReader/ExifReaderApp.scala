package ExifReader

import java.io.File
import org.rogach.scallop._
import scala.collection.mutable.ListBuffer
import scala.sys.process._
import util.Properties

object ExifReaderApp {
  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)

    val dir: File = new File(conf.path.getOrElse(""))
    if (!dir.exists) throw new IllegalArgumentException(s"Directory not found: $dir")

    println("Running exiftool...")
    val extensions: List[String] = conf.extensions.getOrElse(List.empty)
    val exifToolCommand: String = buildExifToolCommand(extensions, dir.getAbsolutePath)
    // todo: need some way of reporting progress
    // using lineStream_! so nonzero exit codes don't break this process by throwing an exception
    val exifToolResult: String = exifToolCommand.lineStream_!.mkString(Properties.lineSeparator)

    if (exifToolResult.contains("0 image files read")) {
      println("No image files found.")
    } else {
      println("Building photographs...")
      val photographs: ListBuffer[Photograph] = buildPhotographs(exifToolResult)

      println("Inserting exif data...")
      DatabaseUtil.createPhotographsTableIfNotExists()
      DatabaseUtil.insertPhotographs(photographs)
      DatabaseUtil.optimizeDatabase()
    }

    println("Done.")
  }

  private def buildExifToolCommand(extensions: List[String], path: String): String = {
    val sb = new StringBuilder

    sb ++= "exiftool "
    extensions.foreach{ ext: String => sb ++= s"-ext $ext " }
    sb ++= "-r "
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
    sb ++= path

    sb.result
  }

  private def buildPhotographs(exifToolResult: String) : ListBuffer[Photograph] = {
    val lines: Seq[String] = exifToolResult.split(Properties.lineSeparator)

    val linesPerPhotograph: Int = 16
    var start: Int = 0

    val photographs: ListBuffer[Photograph] = new ListBuffer[Photograph]()

    while (start <= lines.size) {
      val linesOfCurrentPhotograph: Seq[String] = lines.slice(start, start + linesPerPhotograph)

      if (linesOfCurrentPhotograph.size == linesPerPhotograph) {
        photographs += Photograph(linesOfCurrentPhotograph)
      }

      start += linesPerPhotograph
    }

    photographs
  }
}

private class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val path: ScallopOption[String] = opt[String](required = true)
  val extensions: ScallopOption[List[String]] = trailArg[List[String]]()
  verify()
}
