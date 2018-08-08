package ExifReader

object FocalLength {
  def get(value: Option[String]): Option[FocalLength] = {
    // todo: this needs a case-insensitive unit test
    val numeric: Option[String] = value.map{ _.toLowerCase.replace("mm", "").trim }
    ParseUtil.intOption(numeric).map{ i: Int => FocalLength(i) }
  }
}

final case class FocalLength(mm: Int)
