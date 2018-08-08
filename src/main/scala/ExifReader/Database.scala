package ExifReader

import java.sql._

object Database
{
  // todo: use string builder?
  private val createTableIfNotExists: String = "CREATE TABLE IF NOT EXISTS photograph (" +
    "id INTEGER PRIMARY KEY, " +
    "aperture REAL, " +
    "cameraMake TEXT, " +
    "cameraModel TEXT, " +
    "colorSpace TEXT, " +
    "createDate TEXT, " +
    "directory TEXT NOT NULL, " +
    "fileName TEXT NOT NULL, " +
    "focalLength INTEGER, " +
    "pixelHeight INTEGER, " +
    "pixelWidth INTEGER, " +
    "iso INTEGER, " +
    "lens TEXT, " +
    "megaPixels REAL, " +
    "orientation TEXT, " +
    "shutterSpeed REAL, " +
    "UNIQUE (directory, fileName) ON CONFLICT ROLLBACK)"

  // todo: use string builder?
  private val insertPhotograph: String = s"INSERT INTO photograph (" +
    "aperture, " +
    "cameraMake, " +
    "cameraModel, " +
    "colorSpace, " +
    "createDate, " +
    "directory, " +
    "fileName, " +
    "focalLength, " +
    "pixelHeight, " +
    "pixelWidth, " +
    "iso, " +
    "lens, " +
    "megaPixels, " +
    "orientation, " +
    s"shutterSpeed) VALUES"

  def insertPhotographs(photographs: Seq[Photograph]): Unit = {

    val sortedByPathAndName: Seq[Photograph] = photographs.sortBy{ photograph: Photograph =>
      (photograph.directory, photograph.fileName)
    }

    // todo: what happens when orNull?
    // todo: use string builder?
    val valuesStatements: String = sortedByPathAndName.map{ photograph: Photograph =>
      s"(${photograph.aperture.orNull}, " +
      s"'${photograph.cameraMake.orNull}', " +
      s"'${photograph.cameraModel.orNull}', " +
      s"'${photograph.colorSpace.orNull}', " +
      s"'${photograph.createDate.orNull}', " +
      s"'${photograph.directory.getOrElse{ throw new Exception("directory must not be null") }}', " +
      s"'${photograph.fileName.getOrElse{ throw new Exception("fileName must not be null") }}', " +
      s"${photograph.focalLength.map{ _.mm }.orNull}, " +
      s"${photograph.pixelHeight.orNull}, " +
      s"${photograph.pixelWidth.orNull}, " +
      s"${photograph.iso.orNull}, " +
      s"'${photograph.lens.orNull}', " +
      s"${photograph.megaPixels.orNull}, " +
      s"'${photograph.orientation.orNull}', " +
      s"${photograph.shutterSpeed.map{ _.seconds }.orNull})"
    }.mkString(",")

    val batchInsertPhotograph: String = s"$insertPhotograph $valuesStatements"

    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC")

    var connection: Connection = null

    try {
      connection = DriverManager.getConnection("jdbc:sqlite:photographs.db")
      val statement: Statement = connection.createStatement
      // todo: can this timeout accommodate a 10K item batch?
      statement.setQueryTimeout(30) // seconds
      statement.executeUpdate(createTableIfNotExists)
      // todo: indexes?
      statement.executeUpdate(batchInsertPhotograph)
    } catch {
      // if the error message is "out of memory" it probably means no database file is found
      case e: SQLException => System.err.println(e.getMessage)
    } finally try {
      if (connection != null) connection.close()
    } catch {
      // connection close failed
      case e: SQLException => System.err.println(e)
    }
  }
}