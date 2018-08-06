package ExifReader

object Orientation extends Enumeration {
  type Orientation = Value
  val Landscape, Portrait = Value

  def get(value: Option[String]): Option[Orientation] = {
    value.map{ _.trim } match {
      case Some("Horizontal (normal)") => Some(Landscape)
      case Some("Rotate 270 CW")       => Some(Portrait)
      case _ => None
    }
  }
}
