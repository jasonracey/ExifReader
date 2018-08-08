package ExifReader

import java.io.File

import scala.sys.process._
import scala.util.Try

object ExifReaderApp extends App {
  if (args.length == 0) throw new IllegalArgumentException("Please specify a year.")

  val year: Int = Try(args(0).toInt).toOption.getOrElse(throw new IllegalArgumentException("Year must be an int."))

  // todo: make the entire path the first arg so this can be used by anyone
  val rawFilesParentDir: File = new File(s"/Volumes/photos-a/Photographs/$year")

  if (!rawFilesParentDir.exists) throw new IllegalArgumentException(s"Directory not found: $rawFilesParentDir")

  // todo: make configurable? hard-code every popular raw file extension?
  val rawFiles: Seq[File] = FileUtil.getFilesOfType(rawFilesParentDir, Seq(".CR2", ".ARW"))

  // todo: add exiftool as prereq to readme
  // todo: handle tool not found? other errors?
  // todo: remove take(10)
  // todo: insert as multiple batches?
  val photographs: Seq[Photograph] = rawFiles.take(10).map{ src: File =>
    val result: String = s"exiftool ${src.getAbsolutePath}".!!
    Photograph(result)
  }

  Database.insertPhotographs(photographs)

  println("Done.")
}
