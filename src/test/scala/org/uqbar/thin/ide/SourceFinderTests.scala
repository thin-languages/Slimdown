package org.uqbar.thin.ide


import java.io.File
import org.scalatest._
import scala.collection.immutable.Seq
import java.util.Arrays

class SourceFinderTests extends FlatSpec with Matchers with SetUp {


  file1.getParentFile.mkdirs()// In travis docker src/test/resources doesn't exist because is empty

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