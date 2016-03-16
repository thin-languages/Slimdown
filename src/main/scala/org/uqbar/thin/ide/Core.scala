package org.uqbar.thin.ide

import scala.util.Try
import java.util.jar.JarFile
import java.net.URL
import scala.reflect.internal.util.ScalaClassLoader
import scala.collection.JavaConversions._
import java.util.jar.JarEntry


object Core extends App{
  
  val paths = io.Source.fromFile("config.txt").getLines
  
  def ejecutarPlugin:Any=>Unit = { x => print(x)
                                   print("\n") 
                                 }
  
  val classes = for{path <- paths
      file <- new java.io.File(path).listFiles if file.getName endsWith ".jar"
      jarContents = new JarFile(file).entries
      e<-jarContents
      eName = e.getName
      if !e.isDirectory() && eName.endsWith(".class")
      className = eName.dropRight(6).replace('/','.')
      }yield{getClass.getClassLoader.loadClass(className)}
       
  classes.foreach { ejecutarPlugin }
}