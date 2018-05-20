package com.speful.deep

import scala.io.Source


object TestLoadResources extends App{
  val classLoader = Thread.currentThread getContextClassLoader

  val file = classLoader getResource "log4j2.xml"
  val data = Source fromURL file mkString

  println( data )
}
