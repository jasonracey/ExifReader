package ExifReader

import java.io.File
import org.rogach.scallop._
import scala.collection.mutable.ListBuffer
import scala.sys.process._
import util.Properties

object ExifReaderApp {
  private val exifToolCommandSnippet: String = {
    val sb = new StringBuilder

    sb ++= "exiftool "
    sb ++= "-ext crw "
    sb ++= "-ext cr2 "
    sb ++= "-ext arw "
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

    sb.result
  }

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)

    val dir: File = new File(conf.path.getOrElse(""))
    if (!dir.exists) throw new IllegalArgumentException(s"Directory not found: $dir")

    println("Reading exif data...")
    val exifToolCommand: String = s"$exifToolCommandSnippet${dir.getAbsolutePath}"
    val exifToolResult: String = exifToolCommand.!!

    println("Building photographs...")
    val photographs: ListBuffer[Photograph] = buildPhotographs(exifToolResult)

    println("Inserting exif data...")
    Database.insertPhotographs(photographs)

    println("Done.")
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
  verify()
}
