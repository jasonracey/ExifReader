package ExifReader

import java.time.{LocalDateTime, Month}

import org.scalatest.FunSuite

class PhotographTests extends FunSuite {
  private val input: String = """File Name                       : A7300001.ARW
                                |Directory                       : /Volumes/photos-a/Photographs/2018/20180519_Washington_IngallsCreek/raw
                                |File Modification Date/Time     : 2018:05:19 12:37:10-07:00
                                |Orientation                     : Rotate 270 CW
                                |Image Width                     : 6048
                                |Image Height                    : 4024
                                |Aperture                        : 4.0
                                |ISO                             : 100
                                |Focal Length In 35mm Format     : 99 mm
                                |Color Space                     : sRGB
                                |Make                            : SONY
                                |Camera Model Name               : ILCE-7M3
                                |Lens ID                         : Sony FE 24-105mm F4 G OSS
                                |Megapixels                      : 24.3
                                |Shutter Speed                   : 1/100""".stripMargin


  test("can parse expected input") {
    val localDateTime: LocalDateTime = LocalDateTime.of(
      2018,
      Month.MAY,
      19,
      12,
      37,
      10
    )

    val photo: Photograph = Photograph(input)

    assert(photo.aperture === Some(4.0f))
    assert(photo.cameraMake === Some("SONY"))
    assert(photo.cameraModel === Some("ILCE-7M3"))
    assert(photo.colorSpace === Some("sRGB"))
    assert(photo.createDate === Some(localDateTime))
    assert(photo.directory === Some("/Volumes/photos-a/Photographs/2018/20180519_Washington_IngallsCreek/raw"))
    assert(photo.fileName === Some("A7300001.ARW"))
    assert(photo.focalLength === Some(FocalLength(mm = 99)))
    assert(photo.pixelHeight === Some(4024))
    assert(photo.pixelWidth === Some(6048))
    assert(photo.iso === Some(100))
    assert(photo.lens === Some("Sony FE 24-105mm F4 G OSS"))
    assert(photo.megaPixels === Some(24.3f))
    assert(photo.orientation === Some(Orientation.Portrait))
    assert(photo.shutterSpeed === Some(ShutterSpeed(seconds = 0.01f)))
  }
}
