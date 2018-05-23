package com.speful.sparkall.tasks.suspects

import com.speful.sparkall.tasks.suspects.utils._
import com.speful.sql.utils.SimpleSpark
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import it.nerdammer.spark.hbase._
import org.apache.spark.rdd.RDD

object PersonDataFromLocalFile extends SimpleSpark {
  override def sparkConfOpts: Map[String, String] = Map("spark.hbase.host" -> "master")
  import spark.implicits._
  def main(args: Array[String]): Unit = {
  }
















  lazy val raw = spark.gzipDF("E:\\test\\car.tar.gz").cache
//  lazy val raw = spark.gzipDF("hdfs://master:9000/datas/gat/").cache

  implicit val encoder = RowEncoder(StructType(
    StructField("folder_", StringType) ::
    StructField("file_name_", StringType) ::
    StructField("content_", StringType) :: Nil
  ))

  //索引数据,所有文件下xml文件
  lazy val indices = raw.where("file_name_ like '%xml'").flatMap { case Row(file : String, content : String) =>
    val folder = file.substring(0 , file.lastIndexOf("/") )
    IndicesParser parse content map { case (bcpName, fields) =>
      Row(folder, bcpName.split("_").last, fields mkString "\n")
    }
  }.cache

  //真实数据,bcp文件
  lazy val bcps = raw.where("file_name_ like '%bcp'").flatMap { case Row(file : String, content : String ) =>
    val lasti = file.lastIndexOf("/")
    content split "\n" map{ line => Row(file.substring(0 , lasti), file.split("_").last, line) }
  }.cache

  spark.udf.register("len",(x:String)=>x.length)

  //folder engKey chnKey value
  lazy val parsedData = indices.join(bcps, Seq("folder_", "file_name_")).
    flatMap { case Row(folder : String, fileName : String , fields : String , line : String) =>
      fields.split("\n").
        zip(line split "\t").
        map { case (field, value) =>
          val Array(eng : String , chn : String ) = field split "\t"
          Row(folder , fileName + "_" + eng, chn, value )
        }
    }(
      RowEncoder(StructType(
        StructField("folder_", StringType) ::
          StructField("k_eng_", StringType) ::
          StructField("k_chn_", StringType) ::
          StructField("value_", StringType) :: Nil
      ))
    ).cache


  //person base information
  val personBaseDF = parsedData.where(s"k_eng_ in ('NAME','CERTIFICATE_CODE','MSISDN','COLLECT_PLACE')").rdd.
    map{case Row(f , e, c , v) => (f , e+ "->"+ v)}.
    reduceByKey( _ + "@" + _ ).
    mapPartitions(_ map{ case ( f : String , s : String ) =>
      val data = s split "@" map{e =>
        val Array(k,v) = e split "->"
        k->v
      } toMap

      def d =  data.getOrElse (_ : String , "")

      ( (d("NAME") , d("CERTIFICATE_CODE")) , d("MSISDN" ) )
    }).
//    reduceByKey( (a , b) => a + "," + b ).
    mapPartitions(
      _ map{ case (( name , cc ) , msisdn)=> ( name , cc , msisdn)}
    ).toDF("name_" , "certificate_code_" , "msisdn_" ).
    where("certificate_code_ != ''").
    cache
}

