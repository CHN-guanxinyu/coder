# coder

## spark目录
``这个项目主要对一些常用的spark操作进行封装,在此基础上用户可以方便的使用sc、spark、ssc等对象并且可以十分方便的覆盖一些默认配置。另外,还包括一些常用类型的隐式转换,方便用户使用链式函数调用风格编程``
### 目录结构
com/speful<br>
|_implicits ```一些常用的隐式转换``` <br>
|_spark.utils ```spark工具包```
### 栗子

```scala
import com.speful.implicits._
import com.speful.spark.utils._

object Foo extends App with SimpleSpark{
  //you can override spark config as following if needed
  override def appName = "FooBar" //default current class name `Foo`
  override def master = "yarn" //default `local`
  override def sparkConfOpts = Map("spark.some.config" -> "value") //default `Map.empty`
  override def second = 10 //for streaming, default 1

  //you can directly use such as `sc` `spark` `ssc` and so on
  val rdd = sc makeRDD List(1,2,3)
  
  val df = spark.read load "path/to/file"
  
  //`gzipDF(path , partitionNum)` is from a implicit class
  val gzipDF = spark gzipDF "path/to/some.tar.gz or path/to/*"
  
  val graph = //create a graph first
  //then you can save it as xxx.gexf and show your graph by gephi
  GraphXUtils.saveGraphTo("path/to/xxx.gexf" , graph)
  
  
  
  //implicits part
  //1.class name to a instance
  "package.to.classA".as[classA].foo.bar
  
  //2.sql to df
  "select something from table".go where "cond" show false
  
}
```
持续更新中,未完待续......

## deepscala目录
```没啥用,scala日常练习```


