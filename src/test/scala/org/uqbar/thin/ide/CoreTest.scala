package org.uqbar.thin.ide

import org.scalatest.FreeSpec
import org.scalatest.Matchers
import better.files._
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import scala.sys.process._

class CoreTest extends FreeSpec with Matchers with BeforeAndAfterAll {

	override def beforeAll = compilePlugins

	"Core" - {
		"loading plugins" - {
			val pluginsPath = "src/test/resources/plugins"

			"should load all the classes from all the jars on the given directories" in {

				val plugins = Core.loadPlugins(pluginsPath)

				plugins should have.size(1)
				plugins.head.getSimpleName should be("Dummy")
			}
		}

		"installing plugins" in pending
	}

	protected def compilePlugins {
		val pluginsRoot = file"./src/test/resources/plugins"

		val scalaVersion = Seq(
			"sbt",
			"-Dsbt.log.noformat=true",
			";show scalaVersion"
		).lineStream.last.drop("[info] ".size)

		println(s"Compiling plugins with Scala version: $scalaVersion")

		for (plugin <- pluginsRoot.list) {
			println(s"Compiling plugin at: $plugin")

			Process(Seq(
				"sbt",
				"-Dsbt.log.noformat=true",
				"--error",
				s""";set scalaVersion := "$scalaVersion"""",
				";set scalaSource in Compile := baseDirectory.value",
				s""";set artifactPath in Compile in packageBin := baseDirectory.value / "${plugin.name}.jar" """,
				";package"
			), plugin.toJava).!

			Process(Seq("rm", "-r", "project", "target"), plugin.toJava).!
		}
	}

}
