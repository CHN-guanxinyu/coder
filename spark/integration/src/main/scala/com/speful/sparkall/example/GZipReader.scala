package com.speful.sparkall.example

import java.nio.charset.{Charset, StandardCharsets}

import com.speful.sql.utils.SimpleSpark
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.spark.input.PortableDataStream
import org.apache.spark.sql.Row

import scala.util.Try

object GZipReader extends SimpleSpark{

  def extractFiles(ps: PortableDataStream, n: Int = 1024) = Try {
    val tar = new TarArchiveInputStream(new GzipCompressorInputStream(ps.open))
    Stream.continually(Option(tar.getNextTarEntry))
      // Read until next exntry is null
      .takeWhile(_.isDefined)
      // flatten
      .flatMap(x => x)
      // Drop directories
      .filter(!_.isDirectory)
      .map(e => {
        (e.getName,
        Stream.continually {
          // Read n bytes
          val buffer = Array.fill[Byte](n)(-1)
          val i = tar.read(buffer, 0, n)
          (i, buffer.take(i))}
          // Take as long as we've read something
          .takeWhile(_._1 > 0)
          .map(_._2)
          .flatten
          .toArray)})
      .toArray
  }

  def decode( bytes: Array[Byte] , charset: Charset = StandardCharsets.UTF_8) =
    new String(bytes, StandardCharsets.UTF_8)

  override def sparkConfOpts: Map[String, String] = Map.empty
}
