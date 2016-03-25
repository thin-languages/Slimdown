package org.uqbar.thin.ide

import java.nio.file.Paths
import java.io.File
import org.scalatest._
import scala.collection.immutable.Seq
import java.util.Arrays

class SourceFinderTests extends FlatSpec with Matchers with BeforeAndAfter {

  val resourcesPath = "src/test/resources"
  val file1 = new File(resourcesPath + "/file1.jar");
  val file2 = new File(resourcesPath + "/file2.jar");
  val file3 = new File(resourcesPath + "/file3.jar");

  before {
    file1.createNewFile()
    file2.createNewFile()
    file3.createNewFile()
  }

  after {
    file1.delete()
    file2.delete()
    file3.delete()
  }

  "Sourfinder with localDirectory which contains only jars" should "return a list of the jars" in {
    val files = SourceFinder(resourcesPath)
    files.size shouldBe 3
    assert(files.contains(file1))
    assert(files.contains(file2))
    assert(files.contains(file3))
  }

  "Sourfinder with localDirectory which contains jar and others files" should "return a list of the jars" in {
    val fileSarlompa1 = new File(resourcesPath + "/fileSarlompa1.srlmp");
    val fileSarlompa2 = new File(resourcesPath + "/fileSarlompa2.srlmp");
    fileSarlompa1.createNewFile()
    fileSarlompa2.createNewFile()
    val files = SourceFinder(resourcesPath)
    files.size shouldBe 3
    assert(files.contains(file1))
    assert(files.contains(file2))
    assert(files.contains(file3))
    fileSarlompa1.delete()
    fileSarlompa2.delete()
  }
  
   "Sourfinder with localDirectory which no files" should "return an empty seq" in {
    file1.delete()
    file2.delete()
    file3.delete()
    val files = SourceFinder(resourcesPath)
    assert(files.isEmpty)
    file1.createNewFile()
    file2.createNewFile()
    file3.createNewFile()
   
  }

}