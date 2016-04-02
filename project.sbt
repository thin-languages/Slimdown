name := "slimdown"

scalaVersion := "2.11.7"

lazy val proyecto = FDProject( 
	"org.scalatest" %% "scalatest" % "2.2.1" % "test",
	"org.scala-lang" % "scala-reflect" % "2.11.7"
)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Compile += baseDirectory.value / "src/main/resources"

unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil

unmanagedSourceDirectories in Test += baseDirectory.value / "src/test/resources"
