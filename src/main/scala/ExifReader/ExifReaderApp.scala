package ExifReader

import java.io.File

import org.rogach.scallop._

import scala.sys.process._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val path: ScallopOption[String] = opt[String](required = true)
  val extensions: ScallopOption[List[String]] = trailArg[List[String]]()
  verify()
}

object ExifReaderApp extends App {
  val conf = new Conf(args)

  val dir: File = new File(conf.path.getOrElse(""))

  if (!dir.exists) throw new IllegalArgumentException(s"Directory not found: $dir")

  // empty seq means all files
  val extensions: Seq[String] = conf.extensions.getOrElse(List.empty[String])

  println("Finding files...")

  val files: Seq[File] = FileUtil.getFilesOfType(dir, extensions)

  val totalFileCount: Int = files.size
  var currentFileCount: Int = 1

  // todo: handle tool not found? other errors?
  // todo: remove take(1000)
  val photographs: Seq[Photograph] = files.take(1000).map{ src: File =>
    println(s"Reading exif data for file $currentFileCount of $totalFileCount")
    currentFileCount += 1
    val result: String = s"exiftool ${src.getAbsolutePath}".!!
    Photograph(result)
  }

  println("Inserting exif data...")

  Database.insertPhotographs(photographs)

  println("Done.")
}
