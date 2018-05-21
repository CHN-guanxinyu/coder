package com.speful.mllib.tasks

import com.speful.sql.utils.SimpleSpark

object PersonData extends SimpleSpark{


  val url =
    "jdbc:mysql://172.16.0.36:3306/" +
    "bs_xd?" +
    "useUnicode=true&amp;" +
    "characterEncoding=utf-8"

  val table = "t_rytz"
  val user = "root"
  val passwd = "root01"

  def data() = jdbc( url , table , user , passwd ).checkpoint



  def main(args: Array[String]): Unit = {
    data show
  }


}
