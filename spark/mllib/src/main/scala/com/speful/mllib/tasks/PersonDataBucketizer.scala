package com.speful.mllib.tasks

import com.speful.sql.utils.SimpleSpark
import org.apache.spark.ml.feature.Bucketizer

object PersonDataPretreatment extends SimpleSpark{


  def main(args: Array[String]): Unit = {
    val raw = PersonData.data
    raw.show
    val fields = Array(
      "iscaseperson",
      "isothercaseperson",
      "caselink",
      "othercaselink",
      "sumcallcishu_case",
      "sumcalltime_case",
      "callcishu_other",
      "calltime_other",
      "iscriminal"
    )
    val splits = Array( 0 , 10 , 100 , 1000 , 10000 , Double.PositiveInfinity )

    val bucketizer = new Bucketizer().
      setInputCol( field ).
      setOutputCol(s"b_${field}").
      setSplits( splits )

    bucketizer.transform( raw select field )
  }
}
