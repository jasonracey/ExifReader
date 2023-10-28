name := "ExifReader"

version := "0.1"

scalaVersion := "2.12.18"

libraryDependencies ++= Seq(
  "org.rogach" %% "scallop" % "4.1.0",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.xerial" % "sqlite-jdbc" % "3.40.1.0"
)
