
val swing = "org.scala-lang" % "scala-swing" % "2.10+"

fork in run := true

lazy val root = (project in file(".")).
  settings(
    name := "Tower Defense",
    mainClass in Compile := Some("runtime.TowerDefense"),
    libraryDependencies += swing,
    scalacOptions += "-deprecation"
  )
