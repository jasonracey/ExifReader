package ExifReader

import java.io.File

import scala.sys.process._
import scala.util.Try

object ExifReaderApp extends App {
  if (args.length == 0) throw new IllegalArgumentException("Please specify a year.")

  val year: Int = Try(args(0).toInt).toOption.getOrElse(throw new IllegalArgumentException("Year must be an int."))

  val rawFilesParentDir: File = new File(s"/Volumes/photos-a/Photographs/$year")

  if (!rawFilesParentDir.exists) throw new IllegalArgumentException(s"Directory not found: $rawFilesParentDir")

  val rawFiles: Seq[File] = FileUtil.getFilesOfType(rawFilesParentDir, Seq(".CR2", ".ARW"))

  // sorting so that overall progress can be estimated from console output
  rawFiles.sortBy{ _.getAbsolutePath }.foreach{ src: File =>
    val result: String = s"exiftool ${src.getAbsolutePath}".!!

    val photograph: Photograph = Photograph(result)

    println(photograph)

    // todo: if not exists in database then insert new record
  }

  println("Done.")
}
