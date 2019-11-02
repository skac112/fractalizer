// lazy val root = (project in file(".")).
  // settings(
name := "fractalizer"
version := "0.4.0-SNAPSHOT"
scalaVersion := "2.12.8"
exportJars := true
organization := "skac112"
scalacOptions += "-Ypartial-unification"
// libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
// libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
//libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.8"
//libraryDependencies += "org.typelevel" %% "cats-core" % "1.2.0"
libraryDependencies += "skac112" %% "miro" % "1.0.6-SNAPSHOT"
libraryDependencies += "skac112" %% "funnodes" % "0.0.3-SNAPSHOT"
libraryDependencies += "skac112" %% "vgutils" % "0.1.0-SNAPSHOT"
// ).
// enablePlugins(ScalaJSPlugin)
