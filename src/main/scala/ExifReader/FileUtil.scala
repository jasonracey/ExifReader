package ExifReader

import java.io.File

object FileUtil {
  def getFilesOfType(dir: File, extensions: Seq[String]): Seq[File] = {
    getFilesRecursive(dir).filter{ file: File =>
      extensions.exists{ ext: String =>
        file.getName.toUpperCase.endsWith(ext.toUpperCase)
      }
    }
  }

  private def getFilesRecursive(dir: File): Seq[File] = {
    val these: Seq[File] = dir.listFiles
    these ++ these.filter{ _.isDirectory }.flatMap{ getFilesRecursive }
  }
}
