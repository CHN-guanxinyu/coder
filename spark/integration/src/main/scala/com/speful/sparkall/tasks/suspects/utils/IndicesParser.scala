package com.speful.sparkall.tasks.suspects.utils

import scala.xml.XML

object IndicesParser {
  def parse( xmlStr : String ) = {

    XML.loadString(xmlStr) \ "DATASET" \ "DATA" \ "DATASET" \ "DATA" map { data =>
      val dataSets = data \ "DATASET"
      val bcpName = dataSets(0).\("DATA").\("ITEM")(1).attribute("val").get.toString

      val fields = dataSets(1).\("DATA").\("ITEM").
        map(node => node.attribute("eng").get +"\t"+ node.attribute("chn").get  )
      bcpName -> fields
    }
  }
}
