package com.speful.mllib.tasks

import com.speful.sql.utils.SimpleSpark


object PersonData extends App with SimpleSpark{
  override val appName: String = "Person data hello"

  val url =
    "jdbc:mysql://172.16.0.36:3306/" +
    "bs_xd?" +
    "useUnicode=true&amp;" +
    "characterEncoding=utf-8"

  val table = "t_rytz"
  val user = "root"
  val passwd = "root01"

  println( spark.sparkContext.appName )



}
