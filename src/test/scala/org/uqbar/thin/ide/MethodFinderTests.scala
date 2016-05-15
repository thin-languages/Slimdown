package org.uqbar.thin.ide
import org.scalatest._
import org.uqbar.thin.ide.MethodFinder.ReflectedClass
import scala.reflect.runtime.universe._
import org.scalatest.Matchers._


class MethodFinderTests  extends FlatSpec with Matchers with OneInstancePerTest {
  
  trait Hook
  trait GrapplingHook extends Hook
  
  class Link(x:Int,y:Int) {
    val position = (x,y)
    def getOverThere(hook:GrapplingHook,x:Int,y:Int) = new Link(x,y)
  }
  
  val linkClass:Class[Link] = classOf[Link]
  
  "Asking for a method that doesn't exist" should "return None" in {
    linkClass.method("inexistentMethod") shouldBe None
  }
  
  "Asking for a method" should "return an option with the methodsymbol of that method" in {
    linkClass.method("getOverThere").isDefined shouldBe true
  }
  
  "Asking for the methods with the hook parameter" should "return the methods that expect that parameter type" in {
    linkClass.hookedMethods(typeOf[GrapplingHook]) should contain (linkClass.method("getOverThere").get)
  }
  
  "Asking for the methods with the hook parameter" should "return the methods that expect a superclass of that parameter type" in {
    linkClass.hookedMethods(typeOf[Hook]) should contain (linkClass.method("getOverThere").get)
  }
  
  "Asking for the methods with the hook parameter" should "return an empty list if no method satisfies" in {
    linkClass.hookedMethods(typeOf[Link]) shouldBe empty
  }
  
    
}