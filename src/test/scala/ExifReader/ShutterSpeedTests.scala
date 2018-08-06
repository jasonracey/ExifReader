package ExifReader

import org.scalatest.FunSuite

class ShutterSpeedTests extends FunSuite {
  test("can parse fractional input") {
    val result: Option[ShutterSpeed] = ShutterSpeed.get(Some("1/100"))
    assert(result.isDefined)
    assert(result.get.seconds == 0.01f)
  }

  test("values below 1/1000 are represented in scientific notation") {
    val result: Option[ShutterSpeed] = ShutterSpeed.get(Some("1/2000"))
    assert(result.isDefined)
    assert(result.get.seconds == 5.0E-4f)
  }

  test("can parse decimal input") {
    val result: Option[ShutterSpeed] = ShutterSpeed.get(Some("0.4"))
    assert(result.isDefined)
    assert(result.get.seconds == 0.4f)
  }

  test("fractional input is trimmed before parsing"){
    val result: Option[ShutterSpeed] = ShutterSpeed.get(Some("   1/100   "))
    assert(result.isDefined)
    assert(result.get.seconds == 0.01f)
  }

  test("decimal input is trimmed before parsing"){
    val result: Option[ShutterSpeed] = ShutterSpeed.get(Some("   0.4   "))
    assert(result.isDefined)
    assert(result.get.seconds == 0.4f)
  }
}
