import scala.sys.process.Process

val gitHeadCommitSha = taskKey[String]("determine the current git commit SHA")
val makeVersionProperties = taskKey[Seq[File]]("make a version.properties file.")

inThisBuild( gitHeadCommitSha := Process("git rev-parse HEAD").lineStream.head )
lazy val root = preownedKittenProject("root" , ".").
  settings(
    makeVersionProperties := {
      val propFile = (resourceManaged in Compile).value / "version.properties"
      val content = s"version=${gitHeadCommitSha.value}"
      IO.write( propFile , content )
      Seq(propFile)
    },
    onLoadMessage ~= ( _ + ( if( (sys props "java.specification.version") < Version.min.jdk ) {
      s"""
         |You seem to not be running Java ${Version.min.jdk}.
         |While the provided code may still work, we recommend that you
         |upgrade your version of Java.
    """.stripMargin
    }else "" )),
    libraryDependencies ++= Dependencies.core
  ).settings(CommonSetting.projectSettings)


/**
  * Spark
  */

lazy val spark =
  preownedKittenProject("spark" , "spark").
  dependsOn(root).
  settings(
    libraryDependencies ++= Lib.spark.all,
    libraryDependencies ++= Seq(
      Lib.hadoop_client,
      Lib.hbase.common,
      Lib.hbase.client,
      Lib.hbase.server,
      Lib.hbase.spark_connector,
      Lib.scalaTest
//    ,Lib.kafka,
//    Lib.jackson.core,
//    Lib.jackson.databind,
//    Lib.jackson.mudule_scala

    )
  )

/**
  * scala learning
  */

lazy val deepScala =
  preownedKittenProject("deepScala" , "deepScala").
  dependsOn(root)





/**
  * 创建通用模板
  */
def preownedKittenProject(name : String , path : String ) : Project ={
  Project( name , file(path) ).
    settings(
      version := "0.1-SNAPSHOT",
      organization := "com.speful",
      scalaVersion := Version.scala,
      test in assembly := {}
    ).
    enablePlugins(AssemblyPlugin)
}