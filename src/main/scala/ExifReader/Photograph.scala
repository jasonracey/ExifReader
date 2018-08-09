package ExifReader

import java.time.LocalDateTime
import ExifReader.Orientation.Orientation
import scala.util.Properties

object Photograph {
  val exifToolDateTimePattern: String = "yyyy:MM:dd HH:mm:ss"

  def apply(value: String): Photograph = {
    val lines: Seq[String] = value.split(Properties.lineSeparator)

    val fields: Map[String, String] = lines.map{ line: String =>
      val items: Seq[String] = line.split(": ").map{ _.trim }
      if (items.size == 2) (items(0), items(1))
      else ("", "")
    }.toMap

    Photograph(
      aperture     = ParseUtil.floatOption(fields.get("Aperture")),
      cameraMake   = fields.get("Make"),
      cameraModel  = fields.get("Model"),
      colorSpace   = fields.get("ColorSpace"),
      createDate   = ParseUtil.dateOption(fields.get("CreateDate"), exifToolDateTimePattern),
      directory    = fields.get("Directory"),
      fileName     = fields.get("FileName"),
      focalLength  = FocalLength.get(fields.get("FocalLength")),
      pixelHeight  = ParseUtil.intOption(fields.get("ImageHeight")),
      pixelWidth   = ParseUtil.intOption(fields.get("ImageWidth")),
      iso          = ParseUtil.intOption(fields.get("ISO")),
      lens         = fields.get("LensID"),
      megaPixels   = ParseUtil.floatOption(fields.get("Megapixels")),
      orientation  = Orientation.get(fields.get("Orientation")),
      shutterSpeed = ShutterSpeed.get(fields.get("ShutterSpeed"))
    )
  }
}

final case class Photograph(
  aperture: Option[Float],
  cameraMake: Option[String],
  cameraModel: Option[String],
  colorSpace: Option[String],
  createDate: Option[LocalDateTime],
  directory: Option[String],
  fileName: Option[String],
  focalLength: Option[FocalLength],
  pixelHeight: Option[Int],
  pixelWidth: Option[Int],
  iso: Option[Int],
  lens: Option[String],
  megaPixels: Option[Float],
  orientation: Option[Orientation],
  shutterSpeed: Option[ShutterSpeed]
)
