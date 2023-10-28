package ExifReader

import org.scalatest.funsuite.AnyFunSuite

class FocalLengthTests  extends AnyFunSuite {
  test("can parse expected format"){
    val result: Option[FocalLength] = FocalLength.get(Some("50.0 mm"))
    assert(result.isDefined)
    assert(result.get.mm == 50.0f)
  }

  test("unit is case-insensitive"){
    val result: Option[FocalLength] = FocalLength.get(Some("50.0 MM"))
    assert(result.isDefined)
    assert(result.get.mm == 50.0f)
  }

  test("can parse format without space before unit"){
    val result: Option[FocalLength] = FocalLength.get(Some("50.0mm"))
    assert(result.isDefined)
    assert(result.get.mm == 50.0f)
  }

  test("can parse format without unit"){
    val result: Option[FocalLength] = FocalLength.get(Some("50.0"))
    assert(result.isDefined)
    assert(result.get.mm == 50.0f)
  }

  test("input is trimmed before parsing"){
    val result: Option[FocalLength] = FocalLength.get(Some("   50.0     mm    "))
    assert(result.isDefined)
    assert(result.get.mm == 50.0f)
  }

  test("unexpected format returns None"){
    val result: Option[FocalLength] = FocalLength.get(Some("50.0 millimeters"))
    assert(result.isEmpty)
  }
}
