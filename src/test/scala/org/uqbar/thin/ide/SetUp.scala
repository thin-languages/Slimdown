package org.uqbar.thin.ide

import org.scalatest._

import java.nio.file.Paths
import java.io.File

trait SetUp extends FlatSpec with BeforeAndAfter{
  
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
}