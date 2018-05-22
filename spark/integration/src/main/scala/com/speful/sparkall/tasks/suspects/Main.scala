package com.speful.sparkall.tasks.suspects

import com.speful.sparkall.tasks.suspects.utils.IndicesParser
import com.speful.sql.utils.SimpleSpark
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import utils._

object PersonData extends SimpleSpark{

  lazy val raw = spark.gzipDF("E:\\test\\car.tar.gz" ).cache

  implicit val encoder = RowEncoder(StructType(
    StructField("folder_" , StringType) ::
      StructField("file_name_" , StringType)  ::
      StructField("content_" , StringType) :: Nil
  ))

  //索引数据,所有文件下xml文件
  lazy val indices = raw.where("file_name_ like '%xml'").flatMap{ case Row(file , content)=>
    val folder = file.split("/")(1)
    IndicesParser parse content map{ case(bcpName , fields )=>
      Row( folder , bcpName , fields mkString "\n" )
    }
  }.cache

  //真实数据,bcp文件
  lazy val bcps = raw.where("file_name_ like '%bcp'").
    map{ case Row(file , content) =>
      val t = file.split("/").drop(1)
      Row(t(0) , t(1) , content)
    }.cache


  lazy val parsedData = indices.join(bcps , Seq("folder_" , "file_name_")).
    flatMap{ case Row(folder , _ , fields , content) =>
      fields.split("\n").
        zip( content split "\t" ).
        map{case( field , value ) =>
          val Array(eng , chn) = field split "\t"
          Row(folder , eng , chn , value )
        }
    }(
      RowEncoder( StructType(
        StructField("folder_" , StringType) ::
          StructField("k_eng_" , StringType)  ::
          StructField("k_chn_" , StringType)  ::
          StructField("value_" , StringType) :: Nil
      ))
    ).cache


  def main(args: Array[String]): Unit = {


  }
}

