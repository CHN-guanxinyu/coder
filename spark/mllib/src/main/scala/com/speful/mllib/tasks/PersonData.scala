package com.speful.mllib.tasks

import com.speful.sql.utils.SimpleSQL

object PersonData extends App {
  implicit lazy val spark = SimpleSQL context "Person Data"

  val url =
    "jdbc:mysql://172.16.0.36:3306/" +
    "bs_xd?" +
    "useUnicode=true&amp;" +
    "characterEncoding=utf-8"

  val table = "t_rytz"
  val user = "root"
  val passwd = "root01"

  val data = SimpleSQL jdbc( url , table , user , passwd )


}
