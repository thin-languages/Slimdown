package org.uqbar.thin.ide

import java.util.jar.JarFile
import scala.reflect.internal.util.ScalaClassLoader
import scala.collection.JavaConversions._
import java.util.jar.JarEntry
import java.net.URL
import java.net.URLClassLoader
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader
import java.io.File
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object Core extends App{
  
  implicit def sarlemps:Try[_]=>Option[_] = _.toOption
  
  def executePlugin:Class[_]=>Unit = { x => print(x)
                                  //placeholder
                                   print("\n") 
                                 }
  
  val paths = io.Source.fromFile("config.txt").getLines
    
  val classes = for{path <- paths
      file <- SourceFinder(path)
      loadedClass <- Loader(file).loadedClasses
      }yield{loadedClass}
 
      classes.foreach{ executePlugin }
}

object SourceFinder{
  def apply(path:String) = path match{
    case path if isLocalDirectory(path) => new File(path).listFiles.filter(_.getName.endsWith(".jar")).toSeq
    case _ => List()
  }
  def isLocalDirectory(path:String) = true //placeholder
}

object Loader{
  def apply(file:File) = new Loader(List(file))
}
case class Loader(files:Seq[File]){
  implicit def toJarContent = JarContent
  
  def addFile(newFile:File) = copy(files = files :+ newFile)
  
  def addFiles(newFiles:Seq[File]) = copy(files = files ++ newFiles)
  
  lazy val fileURLs = files map {_.toURI.toURL}
  
  lazy val classLoader = new ScalaClassLoader.URLClassLoader(fileURLs,getClass.getClassLoader)
  
  def loadClass(className:String) = Try(classLoader.loadClass(className))
  
  def loadClassesFrom(file:File) = for{entry <- new JarFile(file).entries
                                       if entry.isClass} yield {loadClass(entry.className)}
                                       
  lazy val loadAll = files flatMap {loadClassesFrom}
  
  lazy val loadedClasses = for{Success(loadedClass)<-loadAll}yield{loadedClass}
  
  lazy val failedClasses = for{Failure(error)<-loadAll
                          }yield{error}
  
}
 
case class JarContent(entry:JarEntry){
    val (className,typeSuffix) = {
      val entryName = entry.getName.replace('/','.')
      entryName.splitAt( entryName.lastIndexOf(".class"))
    }
    def isClass:Boolean = !entry.isDirectory() && (typeSuffix equals ".class")
} 