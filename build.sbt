// lazy val root = (project in file(".")).
  // settings(
name := "fractalizer"
version := "1.0.2-SNAPSHOT"
scalaVersion := "2.12.3"
exportJars := true
organization := "skac"
// libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
// libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.8"
libraryDependencies += "skac" %% "miro" % "1.0.2-SNAPSHOT"
  // ).
// enablePlugins(ScalaJSPlugin)
