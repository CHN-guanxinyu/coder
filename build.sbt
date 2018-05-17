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
    onLoadMessage ~= ( _ + ( if( (sys props "java.specification.version") < Version.min_jdk ) {
      s"""
         |You seem to not be running Java ${Version.min_jdk}.
         |While the provided code may still work, we recommend that you
         |upgrade your version of Java.
    """.stripMargin
    }else "" ))
  ).settings(CommonSetting.projectSettings)


/**
  * Spark related
  */

lazy val spark = preownedKittenProject("spark" , "spark").
  dependsOn(root)

lazy val spark_core = preownedKittenProject("spark-core" , "spark/core").
  dependsOn(spark).
  settings( libraryDependencies ++=
    Seq(
      Lib.spark.core,
      Lib.hadoop_client
    ) ++
    Lib.hbase.all
  )

lazy val spark_sql = preownedKittenProject("spark-sql" , "spark/sql").
  dependsOn(spark_core).
  settings( libraryDependencies += Lib.spark.sql )

lazy val spark_mllib = preownedKittenProject("spark-mllib" , "spark/mllib").
  dependsOn(spark_core).
  settings( libraryDependencies += Lib.spark.mllib )

lazy val spark_graphx = preownedKittenProject("spark-graphx" , "spark/graphx").
  dependsOn(spark_core).
  settings( libraryDependencies += Lib.spark.graphx )

lazy val spark_streaming = preownedKittenProject( "spark-streaming" , "spark/streaming").
  dependsOn(spark_core).
  settings( libraryDependencies += Lib.spark.streaming )


//spark 各个模块集成
lazy val spark_integration = preownedKittenProject("spark-integration" , "spark/integration").
  dependsOn(spark_core , spark_sql , spark_mllib , spark_graphx , spark_streaming )



/**
  * scala learning
  */

lazy val deepScala = preownedKittenProject("deepScala" , "deepScala").
  dependsOn(root)




/**
  * 创建通用模板
  */
def preownedKittenProject(name : String , path : String ) : Project ={
  Project( name , file(path) ).
    settings(
      version := "0.1.0",
      organization := "com.speful",
      libraryDependencies ++= Dependencies.core,
      scalaVersion := Version.scala
    )
}