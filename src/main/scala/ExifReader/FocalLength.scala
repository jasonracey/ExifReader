package ExifReader

object FocalLength {
  def get(value: Option[String]): Option[FocalLength] = {
    val numeric: Option[String] = value.map{ _.toLowerCase.replace("mm", "").trim }
    ParseUtil.floatOption(numeric).map{ f: Float => FocalLength(f) }
  }
}

final case class FocalLength(mm: Float)
