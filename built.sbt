enablePlugins( JavaServerAppPackaging )

lazy val commonSettings = Seq(
  organization := "at.linuxhacker",
  version := "0.1",
  scalaVersion := "2.11.6",
  name := "KafkaTest"
)

mainClass in Compile := Some( "at.linuxhacker.kafka.actorSystem.TestApp" )

lazy val root = (project in file(".")).
  settings(commonSettings: _*)
  
maintainer in Linux := "Herbert Straub <herbert@linuxhacker.at>"

packageSummary in Linux := "Custom application configuration"

packageDescription := "Custom application configuration"

rpmVendor := "Herbert Straub"

rpmLicense := Some( "GNU/GPLv3" )

mappings in Universal <+= (packageBin in Compile, sourceDirectory ) map { (_, src) =>
    // we are using the reference.conf as default application.conf
    // the user can override settings here
    val conf = src / "main" / "resources" / "application.conf"
    conf -> "conf/application.conf"
}

//javaOptions in Universal ++= Seq("-Dconfig.file=${{app_home}}/conf/application.conf")

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "http://oss.sonatype.org/content/repositories/releases",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases" )
  
  
val akkaVersion = "2.3.9"
  
libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.1",
  "org.scala-lang.modules" %% "scala-async" % "0.9.2",
  "com.typesafe.akka" %% "akka-kernel" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" % "akka-slf4j_2.11" % akkaVersion,
  "org.zeromq" % "jeromq" % "0.3.4"  
)


