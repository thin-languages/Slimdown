package org.uqbar.thin.ide

import java.util.jar.JarFile
import scala.reflect.internal.util.ScalaClassLoader
import scala.collection.JavaConversions._
import java.util.jar.JarEntry


object Core extends App{
  
  def ejecutarPlugin:Class[_]=>Unit = { x => print(x)
                                   print("\n") 
                                 }

  val paths = io.Source.fromFile("config.txt").getLines
  
  implicit class JarClass(entry:JarEntry){
    val (className,typeSuffix) = {
      val entryName = entry.getName.replace('/','.')
      entryName.splitAt( entryName.lastIndexOf(".class"))
    }
    def isClass:Boolean = !entry.isDirectory() && (typeSuffix equals ".class")
  }
    
  val classes = for{path <- paths
      file <- new java.io.File(path).listFiles if file.getName endsWith ".jar"
      jarContents = new JarFile(file).entries
      entry<-jarContents
      if entry.isClass
      }yield{getClass.getClassLoader.loadClass(entry.className)}
       
  classes.foreach { ejecutarPlugin }
}

