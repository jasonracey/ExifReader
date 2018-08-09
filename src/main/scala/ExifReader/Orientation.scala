package ExifReader

object Orientation extends Enumeration {
  type Orientation = Value
  val Landscape, Portrait = Value

  def get(value: Option[String]): Option[Orientation] = {
    val trimmed: Option[String] = value.map{ _.trim }

    trimmed match {
      case Some("Horizontal (normal)") => Some(Landscape)
      case Some("Rotate 90 CW")        => Some(Portrait)
      case Some("Rotate 270 CW")       => Some(Portrait)
      case _ => throw new Exception(s"Unknown orientation: $trimmed")
    }
  }
}
