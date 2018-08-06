package ExifReader

object ShutterSpeed {
  def get(value: Option[String]): Option[ShutterSpeed] = {
    val isFraction: Boolean = value.getOrElse("").contains("/")

    val seconds: Option[Float] = {
      if (isFraction) buildFromFraction(value)
      else ParseUtil.floatOption(value)
    }

    seconds.map{ ShutterSpeed(_) }
  }

  private def buildFromFraction(value: Option[String]): Option[Float] = {
    val fractionalParts: Seq[String] = value.getOrElse("").split("/")

    if (fractionalParts.size != 2) None
    else {
      for {
        dividend <- ParseUtil.floatOption(Some(fractionalParts(0).trim))
        divisor  <- ParseUtil.floatOption(Some(fractionalParts(1).trim))
        if divisor != 0.0f
      } yield {
        dividend / divisor
      }
    }
  }
}

final case class ShutterSpeed(seconds: Float)
