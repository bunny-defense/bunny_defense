
val swing      = "org.scala-lang" % "scala-swing" % "2.10+"
val scalafx    = "org.scalafx" %% "scalafx" % "8.0.60-R9"
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
val scalactic  = "org.scalactic" %% "scalactic" % "3.0.1"
val scalatest  = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
fork in run := true

lazy val root = (project in file(".")).
  settings(
    name := "Tower Defense",
    mainClass in Compile := Some("runtime.TowerDefense"),
    libraryDependencies += swing,
    libraryDependencies += scalafx,
    libraryDependencies += scalacheck,
    libraryDependencies += scalactic,
    libraryDependencies += scalatest,
    scalacOptions += "-deprecation",
    javaOptions += "-agentlib:hprof=cpu=samples"
  )
