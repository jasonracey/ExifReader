package ExifReader

import ExifReader.Orientation.{Landscape, Orientation, Portrait}
import org.scalatest.FunSuite

class OrientationTests  extends FunSuite {
  test("can parse landscape orientation"){
    val result: Option[Orientation] = Orientation.get(Some("Horizontal (normal)"))
    assert(result.isDefined)
    assert(result.get == Landscape)
  }

  test("can parse portrait orientation"){
    val result: Option[Orientation] = Orientation.get(Some("Rotate 270 CW"))
    assert(result.isDefined)
    assert(result.get == Portrait)
  }

  test("input is trimmed before parsing"){
    val result: Option[Orientation] = Orientation.get(Some("    Rotate 270 CW  "))
    assert(result.isDefined)
    assert(result.get == Portrait)
  }

  test("unexpected input returns None"){
    val result: Option[Orientation] = Orientation.get(Some("foo"))
    assert(result.isEmpty)
  }
}
