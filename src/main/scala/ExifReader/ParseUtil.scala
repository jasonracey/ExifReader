package ExifReader

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ParseUtil {
  def dateOption(value: Option[String], format: String): Option[LocalDateTime] = {
    try {
      val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
      value.map{ LocalDateTime.parse(_, formatter) }
    } catch {
      case _: Exception => None
    }
  }

  def floatOption(value: Option[String]): Option[Float] = {
    try {
      value.map{ _.toFloat }
    } catch {
      case _: Exception => None
    }
  }

  def intOption(value: Option[String]): Option[Int] = {
    try {
      value.map{ _.toInt }
    } catch {
      case _: Exception => None
    }
  }
}
