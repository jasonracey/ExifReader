package ExifReader

import org.scalatest.FunSuite

class FocalLengthTests  extends FunSuite {
  test("can parse expected format"){
    val result: Option[FocalLength] = FocalLength.get(Some("50 mm"))
    assert(result.isDefined)
    assert(result.get.mm == 50)
  }

  test("unit is case-insensitive"){
    val result: Option[FocalLength] = FocalLength.get(Some("50 MM"))
    assert(result.isDefined)
    assert(result.get.mm == 50)
  }

  test("can parse format without space before unit"){
    val result: Option[FocalLength] = FocalLength.get(Some("50mm"))
    assert(result.isDefined)
    assert(result.get.mm == 50)
  }

  test("can parse format without unit"){
    val result: Option[FocalLength] = FocalLength.get(Some("50"))
    assert(result.isDefined)
    assert(result.get.mm == 50)
  }

  test("input is trimmed before parsing"){
    val result: Option[FocalLength] = FocalLength.get(Some("   50     mm    "))
    assert(result.isDefined)
    assert(result.get.mm == 50)
  }

  test("unexpected format returns None"){
    val result: Option[FocalLength] = FocalLength.get(Some("50 millimeters"))
    assert(result.isEmpty)
  }
}
