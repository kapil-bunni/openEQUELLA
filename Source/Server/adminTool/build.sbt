
libraryDependencies ++= Seq(
  "org.slf4j" % "jcl-over-slf4j" % "1.7.5",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "org.jvnet.hudson" % "xstream" % "1.3.1-hudson-8",
  "com.pearson.equella" % "httpinvoker" % "2.5.5.1",
  "com.google.guava" % "guava" % "18.0",
  "jpf" % "jpf" % "1.0.7",
  "com.fifesoft" % "rsyntaxtextarea" % "1.5.2",
  "com.miglayout" % "miglayout-swing" % "4.2",
  "jnlp" % "jnlp" % "1.0" % Provided
)

fork in run := true

mainClass in (Compile, run) := Some("com.tle.client.harness.ClientLauncher")