package ExifReader

import java.time.LocalDateTime

import ExifReader.Orientation.Orientation

/*
File Name                       : A7300001.ARW
Directory                       : /Volumes/photos-a/Photographs/2018/20180519_Washington_IngallsCreek/raw
File Modification Date/Time     : 2018:05:19 12:37:10-07:00
Orientation                     : Rotate 270 CW
Image Width                     : 6048
Image Height                    : 4024
Aperture                        : 4.0
ISO                             : 100
Focal Length In 35mm Format     : 99 mm
Color Space                     : sRGB
Make                            : SONY
Camera Model Name               : ILCE-7M3
Lens ID                         : Sony FE 24-105mm F4 G OSS
Megapixels                      : 24.3
Shutter Speed                   : 1/100
*/

object Photograph {
  val exifToolDateTimePattern: String = "yyyy:MM:dd HH:mm:ssXXX"

  def apply(value: String): Photograph = {
    val lines: Seq[String] = value.split("\\r?\\n")

    val fields: Map[String, String] = lines.map{ line: String =>
      val items: Seq[String] = line.split(" : ").map{ _.trim }
      if (items.size == 2) (items(0), items(1))
      else ("", "")
    }.toMap

    Photograph(
      aperture     = ParseUtil.floatOption(fields.get("Aperture")),
      cameraMake   = fields.get("Make"),
      cameraModel  = fields.get("Camera Model Name"),
      colorSpace   = fields.get("Color Space"),
      createDate   = ParseUtil.dateOption(fields.get("File Modification Date/Time"), exifToolDateTimePattern),
      directory    = fields.get("Directory"),
      fileName     = fields.get("File Name"),
      focalLength  = FocalLength.get(fields.get("Focal Length In 35mm Format")),
      pixelHeight  = ParseUtil.intOption(fields.get("Image Height")),
      pixelWidth   = ParseUtil.intOption(fields.get("Image Width")),
      iso          = ParseUtil.intOption(fields.get("ISO")),
      lens         = fields.get("Lens ID"),
      megaPixels   = ParseUtil.floatOption(fields.get("Megapixels")),
      orientation  = Orientation.get(fields.get("Orientation")),
      shutterSpeed = ShutterSpeed.get(fields.get("Shutter Speed"))
    )
  }
}

final case class Photograph(
  aperture: Option[Float] = None,
  cameraMake: Option[String] = None,
  cameraModel: Option[String] = None,
  colorSpace: Option[String] = None,
  createDate: Option[LocalDateTime] = None,
  directory: Option[String] = None,
  fileName: Option[String] = None,
  focalLength: Option[FocalLength] = None,
  pixelHeight: Option[Int] = None,
  pixelWidth: Option[Int] = None,
  iso: Option[Int] = None,
  lens: Option[String] = None,
  megaPixels: Option[Float] = None,
  orientation: Option[Orientation] = None,
  shutterSpeed: Option[ShutterSpeed] = None
)
