package com.speful.spark.implicits

import com.speful.spark.utils.SimpleSpark
import org.apache.spark.sql.DataFrame

/**
  * "select xxx" go = spark sql "select xxx"
  *
  * @param sqlStr
  */
class SimpleSql(@transient sqlStr: String) extends SimpleSpark {
  def go: DataFrame = spark sql sqlStr
}

trait ToSimpleSql {
  implicit def to(sql: String): SimpleSql = new SimpleSql(sql)
}
