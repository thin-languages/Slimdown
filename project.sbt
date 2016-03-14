name := "Thin IDE"

scalaVersion := "2.11.7"

lazy val proyecto = FDProject( 
    "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil	
unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil