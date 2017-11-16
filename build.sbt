lazy val scalaV = "2.12.4"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.1",
    "com.lihaoyi" %% "upickle" % "0.4.4",
    "com.lihaoyi" %% "autowire" % "0.2.6",
    guice,
    filters,
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  scalacOptions ++= Seq("-Xmax-classfile-name","78"),
  persistLauncher in Test := false,
  scalaJSUseMainModuleInitializer := true,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.3",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    "com.thoughtworks.binding" %%% "dom" % "11.0.0-M5",
    "com.thoughtworks.binding" %%% "futurebinding" % "11.0.0-M5",
    "com.lihaoyi" %%% "upickle" % "0.4.4",
    "com.lihaoyi" %%% "autowire" % "0.2.6",
    "fr.hmil" %%% "roshttp" % "2.0.2"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
