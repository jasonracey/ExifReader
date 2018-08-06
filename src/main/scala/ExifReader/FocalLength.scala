package ExifReader

object FocalLength {
  def get(value: Option[String]): Option[FocalLength] = {
    println("focal length: " + value)
    val numeric: Option[String] = value.map{ _.replace("mm", "").trim }
    ParseUtil.intOption(numeric).map{ i: Int => FocalLength(i) }
  }
}

final case class FocalLength(mm: Int)
