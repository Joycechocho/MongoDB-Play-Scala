name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test

//swagger for rest api documentation
libraryDependencies ++= Seq(
  "io.swagger" %% "swagger-play2" % "1.5.3", "org.webjars" %% "webjars-play" % "2.5.0-4",
  "org.webjars" % "swagger-ui" % "2.2.0"
)

//Json Library from play framework
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.0"

//casbah
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"

