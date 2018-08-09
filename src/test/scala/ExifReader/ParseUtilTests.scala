package ExifReader

import java.time.{LocalDateTime, Month}
import org.scalatest.FunSuite

class ParseUtilTests extends FunSuite {
  test("can parse date time") {
    val result = ParseUtil.dateOption(Some("2018:05:19 12:37:10"), Photograph.exifToolDateTimePattern)
    assert(result.isDefined)

    val dateTime: LocalDateTime = result.get
    assert(dateTime.getYear === 2018)
    assert(dateTime.getMonth === Month.MAY)
    assert(dateTime.getDayOfMonth === 19)
    assert(dateTime.getHour === 12)
    assert(dateTime.getMinute === 37)
    assert(dateTime.getSecond === 10)
  }

  test("invalid date time returns None") {
    val result = ParseUtil.dateOption(Some(""), Photograph.exifToolDateTimePattern)
    assert(result.isEmpty)
  }

  test("empty date time returns None") {
    val result = ParseUtil.dateOption(None, Photograph.exifToolDateTimePattern)
    assert(result.isEmpty)
  }

  test("can parse float") {
    val result = ParseUtil.floatOption(Some("123.4"))
    assert(result.isDefined)
    assert(result.get == 123.4f)
  }

  test("invalid float returns None") {
    val result = ParseUtil.floatOption(Some("one point five"))
    assert(result.isEmpty)
  }

  test("empty float returns None") {
    val result = ParseUtil.floatOption(None)
    assert(result.isEmpty)
  }

  test("can parse int") {
    val result = ParseUtil.intOption(Some("123"))
    assert(result.isDefined)
    assert(result.get == 123)
  }

  test("invalid int returns None") {
    val result = ParseUtil.intOption(Some("two"))
    assert(result.isEmpty)
  }

  test("empty int returns None") {
    val result = ParseUtil.intOption(None)
    assert(result.isEmpty)
  }
}
