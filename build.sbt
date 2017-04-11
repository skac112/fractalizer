// lazy val root = (project in file(".")).
  // settings(
name := "fractalizer"
version := "1.0.1"
scalaVersion := "2.11.8"
exportJars := true
libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.8"
libraryDependencies += "skac" %% "miro" % "1.0.1-SNAPSHOT"
  // ).
// enablePlugins(ScalaJSPlugin)
