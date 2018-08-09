package ExifReader

import java.time.{LocalDateTime, Month}
import org.scalatest.FunSuite

class PhotographTests extends FunSuite {
  private val input: String = """Aperture: 11.0
                                |CreateDate: 2018:05:27 18:38:56
                                |Make: SONY
                                |Model: ILCE-7M3
                                |ColorSpace: sRGB
                                |Directory: /Volumes/photos-a/Photographs/2018/20180519_Washington_IngallsCreek/raw
                                |FileName: A7300136.ARW
                                |FocalLength: 185.0 mm
                                |ImageWidth: 6048
                                |ImageHeight: 4024
                                |ISO: 100
                                |Orientation: Horizontal (normal)
                                |LensID: Sony FE 70-200mm F4 G OSS
                                |Megapixels: 24.3
                                |ShutterSpeed: 1/8""".stripMargin


  test("can parse expected input") {
    val localDateTime: LocalDateTime = LocalDateTime.of(
      2018,
      Month.MAY,
      27,
      18,
      38,
      56
    )

    val photo: Photograph = Photograph(input)

    assert(photo.aperture === Some(11.0f))
    assert(photo.cameraMake === Some("SONY"))
    assert(photo.cameraModel === Some("ILCE-7M3"))
    assert(photo.colorSpace === Some("sRGB"))
    assert(photo.createDate === Some(localDateTime))
    assert(photo.directory === Some("/Volumes/photos-a/Photographs/2018/20180519_Washington_IngallsCreek/raw"))
    assert(photo.fileName === Some("A7300136.ARW"))
    assert(photo.focalLength === Some(FocalLength(mm = 185.0f)))
    assert(photo.pixelHeight === Some(4024))
    assert(photo.pixelWidth === Some(6048))
    assert(photo.iso === Some(100))
    assert(photo.lens === Some("Sony FE 70-200mm F4 G OSS"))
    assert(photo.megaPixels === Some(24.3f))
    assert(photo.orientation === Some(Orientation.Landscape))
    assert(photo.shutterSpeed === Some(ShutterSpeed(seconds = 0.125f)))
  }
}
