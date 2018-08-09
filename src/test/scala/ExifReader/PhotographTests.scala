package ExifReader

import java.time.{LocalDateTime, Month}
import org.scalatest.FunSuite

class PhotographTests extends FunSuite {
  private val lines: Seq[String] = Seq(
    "======== /Users/jasonracey/Files/Photographs/20180527_Washington_PointOfTheArches/raw/A7300184.ARW",
    "Aperture: 11.0",
    "CreateDate: 2018:05:27 20:27:00",
    "Make: SONY",
    "Model: ILCE-7M3",
    "ColorSpace: sRGB",
    "Directory: /Users/jasonracey/Files/Photographs/20180527_Washington_PointOfTheArches/raw",
    "FileName: A7300184.ARW",
    "FocalLength: 35.0 mm",
    "ImageWidth: 6048",
    "ImageHeight: 4024",
    "ISO: 100",
    "Orientation: Horizontal (normal)",
    "LensID: Sony FE 16-35mm F4 ZA OSS",
    "Megapixels: 24.3",
    "ShutterSpeed: 0.3"
  )


  test("can parse expected input") {
    val localDateTime: LocalDateTime = LocalDateTime.of(
      2018,
      Month.MAY,
      27,
      20,
      27,
      0
    )

    val photo: Photograph = Photograph(lines)

    assert(photo.aperture === Some(11.0f))
    assert(photo.cameraMake === Some("SONY"))
    assert(photo.cameraModel === Some("ILCE-7M3"))
    assert(photo.colorSpace === Some("sRGB"))
    assert(photo.createDate === Some(localDateTime))
    assert(photo.directory === Some("/Users/jasonracey/Files/Photographs/20180527_Washington_PointOfTheArches/raw"))
    assert(photo.fileName === Some("A7300184.ARW"))
    assert(photo.focalLength === Some(FocalLength(mm = 35.0f)))
    assert(photo.pixelHeight === Some(4024))
    assert(photo.pixelWidth === Some(6048))
    assert(photo.iso === Some(100))
    assert(photo.lens === Some("Sony FE 16-35mm F4 ZA OSS"))
    assert(photo.megaPixels === Some(24.3f))
    assert(photo.orientation === Some(Orientation.Landscape))
    assert(photo.shutterSpeed === Some(ShutterSpeed(seconds = 0.3f)))
  }
}
