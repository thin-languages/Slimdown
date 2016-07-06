name := "slimdown"

scalaVersion := "2.11.7"

lazy val proyecto = FDProject( 
	"org.scala-lang" % "scala-reflect" % "2.11.7",
	"com.github.pathikrit" %% "better-files" % "latest.integration",
	"org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil

unmanagedSourceDirectories in Test += baseDirectory.value / "src/test/resources"
