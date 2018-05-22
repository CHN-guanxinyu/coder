package com.speful.sparkall.tasks.suspects

import com.speful.sparkall.tasks.suspects.utils._
import com.speful.sql.utils.SimpleSpark
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{StringType, StructField, StructType}


object PersonDataFromLocalFile extends SimpleSpark {
  import spark.implicits._
  def main(args: Array[String]): Unit = {



    val baseInfo = Map(
      "NAME"                ->      "对象姓名",
      "CERTIFICATE_CODE"    ->      "对象证件号码",
      "MSISDN"              ->      "本机号码"
    )
    val flag = "CASE_NAME" -> "案件名称"

    personBaseDF show 1000
  }




  lazy val raw = spark.gzipDF("E:\\test\\car.tar.gz").cache

  implicit val encoder = RowEncoder(StructType(
    StructField("folder_", StringType) ::
    StructField("file_name_", StringType) ::
    StructField("content_", StringType) :: Nil
  ))

  //索引数据,所有文件下xml文件
  lazy val indices = raw.where("file_name_ like '%xml'").flatMap { case Row(file, content) =>
    val t = file split "/"
    val folder = t(t.length - 2)
    IndicesParser parse content map { case (bcpName, fields) =>
      Row(folder, bcpName, fields mkString "\n")
    }
  }.cache

  //真实数据,bcp文件
  lazy val bcps = raw.where("file_name_ like '%bcp'").map { case Row(file, content) =>
    val bcpName :: folder :: _ = file.split("/").reverse.toList
    Row(folder, bcpName, content)
  }.cache

  spark.udf.register("len",(x:String)=>x.length)

  //folder engKey chnKey value
  lazy val parsedData = indices.join(bcps, Seq("folder_", "file_name_")).
    flatMap { case Row(folder, _, fields, content) =>
      fields.split("\n").
        zip(content split "\t").
        map { case (field, value) =>
          val Array(eng, chn) = field split "\t"
          Row(folder.split("-")(0), eng, chn, value)
        }
    }(
      RowEncoder(StructType(
        StructField("folder_", StringType) ::
          StructField("k_eng_", StringType) ::
          StructField("k_chn_", StringType) ::
          StructField("value_", StringType) :: Nil
      ))
    ).where("len(value_) > 0 and value_ != '\n'").cache


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
    reduceByKey( (a , b) => a + "," + b ).
    mapPartitions(
      _ map{ case (( name , cc ) , msisdn)=> ( name , cc , msisdn)}
    ).toDF("name_" , "certificate_code_" , "msisdn_" ).
    where("certificate_code_ != ''").
    cache
}

