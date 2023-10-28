package ExifReader

import java.io.File
import org.rogach.scallop._
import scala.sys.process._
import util.Properties

object ExifReaderApp {
  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)

    val dir: File = new File(conf.path.getOrElse(""))
    if (!dir.exists) throw new IllegalArgumentException(s"Directory not found: $dir")

    val extensions: List[String] = conf.extensions.getOrElse(List.empty)

    println("Running exiftool...")
    val exifToolCommand: String = buildExifToolCommand(extensions, dir.getAbsolutePath)
    // using lineStream_! so ProcessBuilder doesn't throw an exception on non-zero exit code
    // todo: ProcessLogger not working as expected
    val exifToolResultStream: Stream[String] = exifToolCommand.lineStream_!(ProcessLogger(line => Console.out.println(line)))
    val exifToolResult: String = exifToolResultStream.mkString(Properties.lineSeparator)

    println("Building photographs...")
    val photographs: List[Photograph] = buildPhotographs(exifToolResult).filter(p => p.fileName.nonEmpty)

    if (photographs.isEmpty) {
      println("No valid photographs built.")
    }
    else {
      println("Inserting exif data...")
      DatabaseUtil.createPhotographsTableIfNotExists()
      DatabaseUtil.insertPhotographs(photographs)
      DatabaseUtil.optimizeDatabase()
    }

    println("Done.")
  }

  private def buildExifToolCommand(extensions: List[String], path: String): String = {
    val initialCapacity: Int = 250

    val sb = new StringBuilder(initialCapacity)

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

  private def buildPhotographs(exifToolResult: String) : List[Photograph] = {
    val linesPerPhotograph: Int = 16
    val lines: Seq[String] = exifToolResult.split(Properties.lineSeparator)
    lines.grouped(linesPerPhotograph).map{ Photograph(_) }.toList
  }
}

private class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val path: ScallopOption[String] = opt[String](required = true)
  val extensions: ScallopOption[List[String]] = trailArg[List[String]]()
  verify()
}
