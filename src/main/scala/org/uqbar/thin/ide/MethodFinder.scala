package org.uqbar.thin.ide
import scala.reflect.runtime.universe._
import scala.reflect.ClassTag


object MethodFinder {
   
  type Types = Seq[Type]
 
  type Methods = Seq[MethodSymbol]
  
  def matchesType(expectedType: Type, actualType: Type) = actualType <:< expectedType 
    
  implicit class ReflectedMethodList(methods:Methods){
    def hookedMethods(parameterTypes:Type*) = methods.filter(_.matchesSignature(parameterTypes))
  }
  
  implicit class ReflectedMethod(method:MethodSymbol){
    def matchesSignature(types:Types) =
      types.forall(aType =>
        method.paramLists.exists(_.exists(parameter
            => matchesType(aType, parameter.typeSignature))))
  }

  implicit class ReflectedClass[ClassType: ClassTag](aClass: Class[ClassType]) {
     
    def mirror:Mirror = runtimeMirror(aClass.getClassLoader)
    
    def classSymbol:ClassSymbol = mirror.classSymbol(aClass)
    
    def getType:Type = classSymbol.toType
    
    def methods:Methods = classSymbol.toType.members.toList.filter(_.isMethod).map(_.asMethod)
    
    def method(methodName:String) = getType.member(TermName(methodName)) match {
      case NoSymbol => None
      case noMethod if !noMethod.isMethod => None
      case method => Some(method.asMethod)
    }
    
    def hookedMethods:(Type*)=>Methods = methods.hookedMethods
    
  }
}