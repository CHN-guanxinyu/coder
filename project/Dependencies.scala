import sbt._
import sbt.Keys._

object Dependencies {
  val core = Seq(
    Lib.lombok,
    Lib.scalaCheck,
    Lib.slf4j_impl
  )
}

object Version{
  val scala = "2.11.7"
  val spark = "2.3.0"

  val lombok = "1.16.20"
  val scalaCheck = "1.13.0"
  val hbase = "1.0.2"

  val hbase_spark_connr = "1.0.3"

  val hadoop = "2.7.0"

  val slf4j = "2.11.0"

  object min{
    val jdk = "1.8"
  }
}

object Lib{
  val lombok          = "org.projectlombok"           %"lombok"         %Version.lombok
  val scalaCheck      = "org.scalacheck"              %%"scalacheck"    %Version.scalaCheck
  object spark{
    private def foo( md : String ) = "org.apache.spark"          %%s"spark-$md"      %Version.spark excludeAll(ExclusionRule(organization="joda-time"), ExclusionRule(organization="org.slf4j"), ExclusionRule(organization="com.sun.jersey.jersey-test-framework"), ExclusionRule(organization="org.apache.hadoop"))

    val core            = foo("core")
    val sql             = foo("sql")
    val mllib           = foo("mllib")
    val graphx          = foo("graphx")
    val streaming       = foo("streaming")

    val all = Seq( core , sql , mllib , graphx , streaming )
  }

  object hbase{
    private def foo( md : String )="org.apache.hbase"   %s"hbase-$md"  %Version.hbase
    val client          = foo("client")
    val common          = foo("common")
    val server          = foo("server")
    val protocol        = foo("protocol")
    val hadoop2_compat  = foo("hadoop2-compat")
    val hadoop_compat   = foo("hadoop-compat")
    val spark_connector = "it.nerdammer.bigdata" % "spark-hbase-connector_2.10" %Version.hbase_spark_connr

    val all = Seq( common , client , server , protocol , hadoop2_compat , hadoop_compat , spark_connector )
  }

  val hadoop_client = "org.apache.hadoop" % "hadoop-client" % Version.hadoop excludeAll(ExclusionRule(organization="joda-time"), ExclusionRule(organization="org.slf4j"))

  //val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"
  val slf4j_impl = "org.apache.logging.log4j" % "log4j-slf4j-impl" %Version.slf4j

}