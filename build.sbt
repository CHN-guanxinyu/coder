val spark_version   = "2.3.0"
val scala_version   = "2.11.7"
val min_jdk_version = "1.8"



val lombok          = "org.projectlombok"           %"lombok"         %"1.16.20"
val scalaCheck      = "org.scalacheck"              %%"scalacheck"    %"1.13.0"

lazy val baseOpts = Defaults.coreDefaultSettings ++ Seq(
  scalaVersion := scala_version,
  organization := "com.speful",
  version := "1.0",

  libraryDependencies ++= Seq( lombok , scalaCheck )

)

lazy val root = (project in file("."))
  .settings(
    baseOpts ,
    onLoadMessage ~= ( _ + ( if( (sys props "java.specification.version") < min_jdk_version ) {
      s"""
         |You seem to not be running Java ${min_jdk_version}.
         |While the provided code may still work, we recommend that you
         |upgrade your version of Java.
    """.stripMargin
    }else "" ) ),

    name := "coder"
  )
  .aggregate(
    spark, deepScala
  )




lazy val spark = project in file("spark") settings(
  baseOpts,
  libraryDependencies ++=
    Seq(
      "sql"     ,
      "core"    ,
      "mllib"   ,
      "graphx"  ,
      "streaming"
    ).map( md =>
      "org.apache.spark"          %%s"spark-$md"      %spark_version
    )

)
lazy val deepScala = project in file("deepScala") settings( baseOpts )