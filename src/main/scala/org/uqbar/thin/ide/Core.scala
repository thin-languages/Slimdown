package org.uqbar.thin.ide

import java.util.jar.JarFile

import scala.collection.JavaConversions.enumerationAsScalaIterator
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader

import better.files.File

object Core extends App {
	loadPlugins(args: _*).foreach{ installPlugin }

	def loadPlugins(pluginPaths: String*) = for {
		jarPath <- pluginPaths
		jar <- File(jarPath).glob("**/*.jar")
		classLoader = new URLClassLoader(jar.uri.toURL :: Nil, getClass.getClassLoader)
		jarEntry <- new JarFile(jar.toJava).entries if jarEntry.getName endsWith ".class"
		className = jarEntry.getName.replace('/','.') dropRight ".class".size
	} yield classLoader loadClass className

	def installPlugin(plugin: Class[_]) {
		println(s"Loading plugin: $plugin")
	}
}