package org.uqbar.thin.ide
import org.scalatest._
import java.io.File
import scala.util.Failure
import scala.util.Success
import scala.util.Try

class LoaderTests extends FlatSpec with Matchers with OneInstancePerTest {

  val resourcesPath = """src\test\resources\org\\uqbar\thin\ide\"""

  val dummyJar = new File(resourcesPath + "Dummy.jar")

  val dummyClass = Loader(dummyJar).loadClass("org.uqbar.thin.ide.Dummy").get

  val dummyInstance = dummyClass.newInstance

  val dummyMethod = dummyClass.getMethod("returnMe", classOf[Any])

  val sayHello = dummyClass.getMethod("sayHello")

  "Trying to load a class that isn't in a jar" should "return a Failure" in {
    Loader(dummyJar).loadClass("org.uqbar.thin.ide.Sarlomp") should matchPattern { case Failure(_) => }
  }

  "Trying to load a class that is in the jar" should "return a Success" in {
    Loader(dummyJar).loadClass("org.uqbar.thin.ide.Dummy") should matchPattern { case Success(_) => }
  }

  "loadedClasses from a jar with only a class" should "return a list with only that class" in {
    Loader(dummyJar).loadedClasses shouldBe Seq(dummyClass)
  }
  
  "failedClasses from a jar with only correct classes" should "return an empty list" in {
    Loader(dummyJar).failedClasses shouldBe Seq()
  }
  
  "loadAll from a class with only correct classes" should "be equal to loadedClasses" in {
    Loader(dummyJar).loadAll shouldEqual Loader(dummyJar).loadedClasses
  }

  "Dummy's method returnMe" should "return the same as passed" in {
    dummyMethod.invoke(dummyInstance, new Integer(5)) shouldBe 5
  }

  "Dummy's method sayHello" should "return hello world" in {
    sayHello.invoke(dummyInstance) shouldBe "Hello, World!"
  }

}