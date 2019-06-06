// lazy val root = (project in file(".")).
  // settings(
name := "fractalizer"
version := "0.0.2-SNAPSHOT"
scalaVersion := "2.12.3"
exportJars := true
organization := "skac"
scalacOptions += "-Ypartial-unification"
// libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
// libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
//libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.8"
//libraryDependencies += "org.typelevel" %% "cats-core" % "1.2.0"
libraryDependencies += "skac" %% "miro" % "1.0.4-SNAPSHOT"
libraryDependencies += "skac" %% "funnodes" % "0.0.2-SNAPSHOT"
libraryDependencies += "skac" %% "vgutils" % "0.1.0-SNAPSHOT"
// ).
// enablePlugins(ScalaJSPlugin)
