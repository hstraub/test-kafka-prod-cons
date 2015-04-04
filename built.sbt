lazy val commonSettings = Seq(
  organization := "at.linuxhacker",
  version := "0.1",
  scalaVersion := "2.11.4",
  name := "KafkaTest"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "http://oss.sonatype.org/content/repositories/releases",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases" )
  
libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.1"
)


