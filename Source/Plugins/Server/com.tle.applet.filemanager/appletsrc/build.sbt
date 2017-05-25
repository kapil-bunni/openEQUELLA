libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "18.0",
  "com.github.insubstantial" % "flamingo" % "7.3",
  "com.miglayout" % "miglayout-swing" % "4.2"
)
dependsOn(platformSwing, LocalProject("com_tle_common_applet"), LocalProject("com_tle_applet_filemanager"))

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
