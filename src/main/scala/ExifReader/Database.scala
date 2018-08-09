package ExifReader

import java.sql._

object Database {
  private val batchSize: Int = 100

  private val createTableIfNotExistsSql: String = {
    val initialCapacity: Int = 600

    val sb = new StringBuilder(initialCapacity)

    sb ++= "CREATE TABLE IF NOT EXISTS photograph ("
    sb ++= "id INTEGER PRIMARY KEY, "
    sb ++= "aperture REAL, "
    sb ++= "cameraMake TEXT, "
    sb ++= "cameraModel TEXT, "
    sb ++= "colorSpace TEXT, "
    sb ++= "createDate TEXT, "
    sb ++= "directory TEXT NOT NULL, "
    sb ++= "fileName TEXT NOT NULL, "
    sb ++= "focalLength INTEGER, "
    sb ++= "pixelHeight INTEGER, "
    sb ++= "pixelWidth INTEGER, "
    sb ++= "iso INTEGER, "
    sb ++= "lens TEXT, "
    sb ++= "megaPixels REAL, "
    sb ++= "orientation TEXT, "
    sb ++= "shutterSpeed REAL)"

    sb.result
  }

  private val insertPhotographSnippet: String = {
    val initialCapacity: Int = 350

    val sb = new StringBuilder(initialCapacity)

    sb ++= "INSERT INTO photograph ("
    sb ++= "aperture, "
    sb ++= "cameraMake, "
    sb ++= "cameraModel, "
    sb ++= "colorSpace, "
    sb ++= "createDate, "
    sb ++= "directory, "
    sb ++= "fileName, "
    sb ++= "focalLength, "
    sb ++= "pixelHeight, "
    sb ++= "pixelWidth, "
    sb ++= "iso, "
    sb ++= "lens, "
    sb ++= "megaPixels, "
    sb ++= "orientation, "
    sb ++= "shutterSpeed) VALUES"

    sb.result
  }

  def insertPhotographs(photographs: Seq[Photograph]): Unit = {
    val sortedByPathAndName: Seq[Photograph] = photographs.sortBy{ photograph: Photograph =>
      (photograph.directory, photograph.fileName)
    }

    val batches: Iterator[Seq[Photograph]] = sortedByPathAndName.grouped(batchSize)

    val totalBatchCount: Int = batches.size
    var currentBatchCount: Int = 1

    batches.foreach{ batch: Seq[Photograph] =>
      insert(batch)
      println(s"inserted batch $currentBatchCount of $totalBatchCount")
      currentBatchCount += 1
    }
  }

  private def insert(photographs: Seq[Photograph]): Unit = {
    val valuesStatements: String = photographs.map{ makeValuesStatement }.mkString(",")

    val insertPhotographSql: String = s"$insertPhotographSnippet $valuesStatements"

    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC")

    var connection: Connection = null

    try {
      connection = DriverManager.getConnection("jdbc:sqlite:photographs.db")
      val statement: Statement = connection.createStatement
      statement.setQueryTimeout(30) // seconds
      statement.executeUpdate(createTableIfNotExistsSql)
      // todo: indexes?
      statement.executeUpdate(insertPhotographSql)
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

  private def makeValuesStatement(photograph: Photograph): String = {
    val initialCapacity: Int = 500

    val sb = new StringBuilder(initialCapacity)

    sb ++= s"(${photograph.aperture.orNull}, "
    sb ++= s"'${photograph.cameraMake.orNull}', "
    sb ++= s"'${photograph.cameraModel.orNull}', "
    sb ++= s"'${photograph.colorSpace.orNull}', "
    sb ++= s"'${photograph.createDate.orNull}', "
    sb ++= s"'${photograph.directory}', "
    sb ++= s"'${photograph.fileName}', "
    sb ++= s"${photograph.focalLength.map{ _.mm }.orNull}, "
    sb ++= s"${photograph.pixelHeight.orNull}, "
    sb ++= s"${photograph.pixelWidth.orNull}, "
    sb ++= s"${photograph.iso.orNull}, "
    sb ++= s"'${photograph.lens.orNull}', "
    sb ++= s"${photograph.megaPixels.orNull}, "
    sb ++= s"'${photograph.orientation.orNull}', "
    sb ++= s"${photograph.shutterSpeed.map{ _.seconds }.orNull})"

    sb.result
  }
}