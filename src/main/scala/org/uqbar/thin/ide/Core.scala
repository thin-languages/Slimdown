package org.uqbar.thin.ide

import java.util.jar.JarFile
import scala.reflect.internal.util.ScalaClassLoader
import scala.collection.JavaConversions._
import java.util.jar.JarEntry
import java.net.URL
import java.net.URLClassLoader
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader
import java.io.File



object Core extends App{
  
  implicit class JarClass(entry:JarEntry){
    val (className,typeSuffix) = {
      val entryName = entry.getName.replace('/','.')
      entryName.splitAt( entryName.lastIndexOf(".class"))
    }
    def isClass:Boolean = !entry.isDirectory() && (typeSuffix equals ".class")
  }
  
  def executePlugin:Class[_]=>Unit = { x => print(x)
                                  //placeholder
                                   print("\n") 
                                 }
  
  val paths = io.Source.fromFile("config.txt").getLines
    
  val classes = for{path <- paths
      file <- SourceFinder(path)
      classLoader = new ScalaClassLoader.URLClassLoader(List(file.toURI.toURL),getClass.getClassLoader)
      jarContents = new JarFile(file).entries
      entry<-jarContents
      if entry.isClass
      }yield{classLoader.loadClass(entry.className)}
  classes.foreach { executePlugin }
  
}

object SourceFinder{
  def apply(path:String) = path match{
    case path if isLocalDirectory(path) => new File(path).listFiles.filter(_.getName.endsWith(".jar")).toSeq
    case _ => List()
  }
  def isLocalDirectory(path:String) = path.startsWith("C")
}