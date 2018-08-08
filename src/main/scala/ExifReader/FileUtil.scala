package ExifReader

import java.io.File

object FileUtil {
  def getFilesOfType(dir: File, extensions: Seq[String]): Seq[File] = {
    getFilesRecursive(dir).filter{ file: File =>
      extensions.exists{ extension: String =>
        file.getName.toUpperCase.endsWith(extension.toUpperCase)
      }
    }
  }

  private def getFilesRecursive(dir: File): Seq[File] = {
    val these: Seq[File] = dir.listFiles
    these ++ these.filter{ _.isDirectory }.flatMap{ getFilesRecursive }
  }
}
