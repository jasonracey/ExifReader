package ExifReader

import ExifReader.Orientation.{Landscape, Orientation, Portrait}
import org.scalatest.FunSuite

class OrientationTests  extends FunSuite {
  test("can parse landscape orientation"){
    val result: Option[Orientation] = Orientation.get(Some("Horizontal (normal)"))
    assert(result.isDefined)
    assert(result.get == Landscape)
  }

  test("can parse portrait orientation 90"){
    val result: Option[Orientation] = Orientation.get(Some("Rotate 90 CW"))
    assert(result.isDefined)
    assert(result.get == Portrait)
  }

  test("can parse portrait orientation 270"){
    val result: Option[Orientation] = Orientation.get(Some("Rotate 270 CW"))
    assert(result.isDefined)
    assert(result.get == Portrait)
  }

  test("input is trimmed before parsing"){
    val result: Option[Orientation] = Orientation.get(Some("    Rotate 270 CW  "))
    assert(result.isDefined)
    assert(result.get == Portrait)
  }

  test("none returns none"){
    val result: Option[Orientation] = Orientation.get(None)
    assert(result.isEmpty)
  }

  test("unexpected input throws exception"){
    val exception: Exception = intercept[Exception](Orientation.get(Some("foo")))
    assert(exception.getMessage == "Unknown orientation: Some(foo)")
  }
}
