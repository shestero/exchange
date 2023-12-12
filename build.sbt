ThisBuild / version := "0.2.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "exchange"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test