name := "ExifReader"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.rogach" %% "scallop" % "3.1.3",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.xerial" % "sqlite-jdbc" % "3.23.1"
)
