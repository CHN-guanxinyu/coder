import sbt._
import sbt.Keys._

object Dependencies {
  val core = Seq(
    Lib.lombok,
    Lib.scalaCheck
  )
}

object Version{
  val scala = "2.11.7"
  val spark = "2.3.0"
  val min_jdk = "1.8"

  val lombok = "1.16.20"
  val scalaCheck = "1.13.0"
  val hbase = "2.0.0"
}

object Lib{
  val lombok          = "org.projectlombok"           %"lombok"         %Version.lombok
  val scalaCheck      = "org.scalacheck"              %%"scalacheck"    %Version.scalaCheck
  object spark{
    private def foo( md : String ) = "org.apache.spark"          %%s"spark-$md"      %Version.spark

    val core = foo("core")
    val sql = foo("sql")
    val mllib = foo("mllib")
    val graphx = foo("graphx")
    val streaming = foo("streaming")

    val all = Seq( core , sql , mllib , graphx , streaming )
  }

  object hbase{
    private def foo( md : String )="org.apache.hbase"   %s"hbase-$md"  %Version.hbase
    val client = foo("client")
    val server = foo("server")
    val protocol = foo("protocol")
    val hadoop2_compat = foo("hadoop2-compat")
    val hadoop_compat = foo("hadoop-compat")

    val all = Seq( client , server , protocol , hadoop2_compat , hadoop_compat )
  }

}