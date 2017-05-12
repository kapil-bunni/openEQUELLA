
val jacksonVersion = "2.4.1"

lazy val collectJars = taskKey[Unit]("Collect jars")

lazy val learningedge_config = project in file("Dev/learningedge-config")

lazy val allPlugins = LocalProject("allPlugins")

val legacyPaths = Seq(
  javaSource in Compile := baseDirectory.value / "src",
  javaSource in Test := baseDirectory.value / "test",
  unmanagedSourceDirectories in Compile := (javaSource in Compile).value :: Nil,
  unmanagedSourceDirectories in Test := (javaSource in Test).value :: Nil
)

lazy val equellaserver = (project in file("Source/Server/equellaserver")).settings(legacyPaths).settings(
  version := "1.0",
  scalaVersion := "2.11.7",
  updateOptions := updateOptions.value.withCachedResolution(true),
  unmanagedClasspath in Runtime += (baseDirectory in learningedge_config).value,
  libraryDependencies ++= Seq(
    "axis" % "axis" % "1.4",
    "blackboard" % "bb-bridge" % "9.1",
    "blackboard" % "bb-platform" % "9.1.201410.160373",
    "blackboard" % "bb-taglibs" % "9.1.40071.3",
    "cglib" % "cglib" % "2.2",
    "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.jaxrs" % "jackson-jaxrs-base" % jacksonVersion,
    "com.fasterxml.jackson.jaxrs" % "jackson-jaxrs-json-provider" % jacksonVersion,
    "com.flickr4java" % "flickr4java" % "2.16" excludeAll (
      ExclusionRule(organization = "org.apache.axis", name = "axis")
      ),
    "com.google.api-client" % "google-api-client" % "1.20.0",
    "com.google.apis" % "google-api-services-books" % "v1-rev72-1.20.0",
    "com.google.apis" % "google-api-services-youtube" % "v3-rev136-1.20.0",
    "com.google.checkout.sdk" % "checkout-sdk" % "2.5.1",
    "com.google.code.findbugs" % "jsr305" % "2.0.3",
    "com.google.code.gson" % "gson" % "1.6",
    "com.google.gdata" % "core" % "1.47.1",
    "com.google.guava" % "guava" % "18.0",
    "com.google.inject" % "guice" % "3.0",
    "com.google.inject.extensions" % "guice-assistedinject" % "3.0",
    "com.google.inject.extensions" % "guice-spring" % "3.0",
    "com.ibm.icu" % "icu4j" % "4.6.1.1",
    "com.kaltura" % "kalturaClient" % "3.2.1",
    "com.microsoft.sqlserver" % "mssql-jdbc" % "6.1.0.jre8",
    "com.miglayout" % "miglayout-swing" % "4.2",
    "com.ning" % "async-http-client" % "1.7.8",
    "com.oracle" % "ojdbc6" % "11.2.0.3",
    "com.paypal.sdk" % "paypal-base" % "1.0",
    "com.rometools" % "rome" % "1.7.2",
    "com.tle.reporting" % "reporting-common-6.2" % "2",
    "com.wordnik" % "swagger-annotations" % "1.3.12",
    "com.wordnik" % "swagger-core_2.11" % "1.3.12" excludeAll (
      ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
      ),
    "com.wordnik" % "swagger-jaxrs_2.11" % "1.3.12" excludeAll(
      ExclusionRule(organization = "com.sun.jersey"),
      ExclusionRule(organization = "javax.servlet", name = "javax.servlet-api"),
      ExclusionRule(organization = "javax.ws.rs", name = "jsr311-api")
    ),
    "com.zaxxer" % "HikariCP" % "2.6.1",
    "commons-beanutils" % "commons-beanutils-equella" % "1.8.2.1",
    "commons-codec" % "commons-codec" % "1.7",
    "commons-collections" % "commons-collections" % "3.2.1",
    "commons-configuration" % "commons-configuration" % "1.9",
    "commons-daemon" % "commons-daemon" % "1.0.15",
    "commons-discovery" % "commons-discovery" % "0.5",
    "commons-fileupload" % "commons-fileupload" % "aarons-hacked-1.2.1.2",
    "commons-httpclient" % "commons-httpclient" % "3.1",
    "commons-io" % "commons-io" % "2.4",
    "commons-lang" % "commons-lang" % "2.6",
    "dom4j" % "dom4j" % "1.6.1",
    "edu.asu.itunesu" % "itunesu-api-java" % "1.7",
    "edu.harvard.hul" % "mets" % "1.0",
    "flamingo" % "flamingo" % "1.0",
    "it.uniroma3.mat" % "extendedset" % "2.2",
    "javax.inject" % "javax.inject" % "1",
    "javax.mail" % "mail" % "1.4.3",
    "javax.servlet" % "jstl" % "1.1.2",
    "javax.xml" % "jaxrpc" % "1.1",
    "jdom" % "jdom" % "1.0",
    "jnlp" % "jnlp" % "1.0",
    "joda-time" % "joda-time" % "2.2",
    "jpf" % "jpf" % "1.0.7",
    "jpf" % "jpf-tools" % "1.0.5",
    "log4j" % "log4j" % "1.2.17",
    "net.oauth.core" % "oauth" % "20100527",
    "net.oauth.core" % "oauth-provider" % "20100527",
    "net.sf.beanlib" % "beanlib" % "5.0.3beta",
    "net.sf.beanlib" % "beanlib-hibernate" % "5.0.3beta",
    "net.sf.ezmorph" % "ezmorph" % "1.0.4",
    "net.sf.json-lib" % "json-lib" % "2.4" classifier "jdk15",
    "net.sf.transmorph" % "transmorph" % "3.1.0",
    "org.apache.axis2" % "addressing" % "1.6.2" classifier "classpath-module",
    "org.apache.axis2" % "axis2-adb" % "1.6.2",
    "org.apache.axis2" % "axis2-adb-codegen" % "1.6.2",
    "org.apache.axis2" % "axis2-kernel" % "1.6.2",
    "org.apache.axis2" % "axis2-transport-http" % "1.5.5",
    "org.apache.axis2" % "axis2-transport-local" % "1.6.2",
    "org.apache.commons" % "commons-compress" % "1.1",
    "org.apache.curator" % "curator-client" % "2.6.0",
    "org.apache.curator" % "curator-framework" % "2.6.0",
    "org.apache.curator" % "curator-recipes" % "2.6.0",
    "org.apache.cxf" % "cxf-bundle" % "2.7.6" excludeAll(
      ExclusionRule(organization = "org.apache.geronimo.specs"),
      ExclusionRule(organization = "javax.xml.bind"),
      ExclusionRule(organization = "javax.xml.soap"),
      ExclusionRule(organization = "xml-resolver"),
      ExclusionRule(organization = "asm"),
      ExclusionRule(organization = "org.springframework"),
      ExclusionRule(organization = "aopalliance"),
      ExclusionRule(organization = "org.jvnet"),
      ExclusionRule(organization = "antlr"),
      ExclusionRule(organization = "org.apache.xmlbeans"),
      ExclusionRule(organization = "javax.ws.rs"),
      ExclusionRule(organization = "org.codehaus.jettison"),
      ExclusionRule(organization = "org.eclipse.jetty"),
      ExclusionRule(organization = "org.codehaus.jra"),
      ExclusionRule(organization = "rhino"),
      ExclusionRule(organization = "org.mozilla"),
      ExclusionRule(organization = "org.apache.ws.security"),
      ExclusionRule(organization = "org.apache.santuario"),
      ExclusionRule(organization = "org.opensaml"),
      ExclusionRule(organization = "joda-time"),
      ExclusionRule(organization = "com.sun.xml.messaging.saaj"),
      ExclusionRule(organization = "xalan"),
      ExclusionRule(organization = "com.sun.xml.fastinfoset"),
      ExclusionRule(organization = "net.sf.ehcache")
    ),
    "org.apache.httpcomponents" % "httpclient" % "4.3.4",
    "org.apache.httpcomponents" % "httpcore" % "4.3.2",
    "org.apache.lucene" % "lucene-analyzers" % "3.5.0",
    "org.apache.lucene" % "lucene-core" % "3.5.0",
    "org.apache.lucene" % "lucene-queries" % "3.5.0",
    "org.apache.pdfbox" % "pdfbox" % "1.8.7",
    "org.apache.poi" % "poi-ooxml" % "3.9",
    "org.apache.rampart" % "rampart-core" % "1.6.2" excludeAll(
      ExclusionRule(organization = "org.apache.xalan"),
      ExclusionRule(organization = "org.apache.xerces")
    ),
    "org.apache.rampart" % "rampart-policy" % "1.6.2" excludeAll(
      ExclusionRule(organization = "org.apache.xalan"),
      ExclusionRule(organization = "org.apache.xerces")
    ),
    "org.apache.rampart" % "rampart-trust" % "1.6.2" excludeAll(
      ExclusionRule(organization = "org.apache.xalan"),
      ExclusionRule(organization = "org.apache.xerces")
    ),
    "org.apache.struts" % "struts-core" % "1.3.10" excludeAll (
      ExclusionRule(organization = "antlr", name = "antlr")
      ),
    "org.apache.struts" % "struts-extras" % "1.3.10",
    "org.apache.struts" % "struts-taglib" % "1.3.10",
    "org.apache.tika" % "tika-core" % "1.3",
    "org.apache.tika" % "tika-parsers" % "1.3",
    "org.apache.tomcat" % "tomcat-annotations-api" % "8.0.15",
    "org.apache.tomcat" % "tomcat-api" % "8.0.15",
    "org.apache.tomcat" % "tomcat-catalina" % "8.0.15",
    "org.apache.tomcat" % "tomcat-catalina-ha" % "8.0.15",
    "org.apache.tomcat" % "tomcat-coyote" % "8.0.15",
    "org.apache.tomcat" % "tomcat-jsp-api" % "8.0.15",
    "org.apache.tomcat" % "tomcat-juli" % "8.0.15",
    "org.apache.tomcat" % "tomcat-servlet-api" % "8.0.15",
    "org.apache.tomcat" % "tomcat-tribes" % "8.0.15",
    "org.apache.tomcat" % "tomcat-util" % "8.0.15",
    "org.apache.tomcat" % "tomcat-util-scan" % "8.0.15",
    "org.apache.ws.commons.axiom" % "axiom-api" % "1.2.13",
    "org.apache.ws.commons.axiom" % "axiom-impl" % "1.2.13",
    "org.apache.ws.security" % "wss4j" % "1.6.2",
    "org.apache.zookeeper" % "zookeeper" % "3.4.6" excludeAll (
      ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
      ),
    "org.bouncycastle" % "bcprov-jdk15on" % "1.51",
    "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
    "org.codehaus.woodstox" % "stax2-api" % "3.1.3",
    "org.codehaus.woodstox" % "woodstox-core-asl" % "4.2.0",
    "org.codehaus.xfire" % "xfire-aegis" % "1.2.1",
    "org.dspace" % "cql-java" % "1.0",
    //  "org.dspace.oclc" % "oclc-srw" % "1.0.20080328",
    "org.dts.spell" % "jmyspell-core" % "1.0.0-beta-2",
    "org.eclipse.birt" % "birt-api" % "3.7.2",
    "org.eclipse.birt.runtime" % "org.eclipse.datatools.connectivity.oda" % "3.3.3.v201110130935",
    "org.eclipse.jetty" % "jetty-util" % "8.1.7.v20120910",
    "org.freemarker" % "freemarker" % "2.3.18",
    "org.guice-recipes" % "guice-recipes-core" % "3.0",
    "org.guice-recipes" % "guice-recipes-spring" % "3.0",
    "org.hurl" % "hurl" % "1.1",
    "org.javassist" % "javassist" % "3.18.2-GA",
    "org.jboss.resteasy" % "resteasy-jackson-provider" % "3.0.10.Final",
    "org.jboss.resteasy" % "resteasy-jaxrs" % "3.0.10.Final",
    "org.jboss.resteasy" % "jaxrs-api" % "3.0.10.Final",
    "org.json4s" % "json4s-jackson_2.11" % "3.2.11",
    "org.jsoup" % "jsoup" % "1.6.1",
    "org.jvnet.hudson" % "xstream" % "1.3.1-hudson-8",
    "org.mozilla" % "rhino" % "1.7R4",
    "org.oclc.oai" % "oaicat" % "1.5.57",
    "org.onemind.jxp" % "jxp" % "1.2.0",
    "org.opensaml" % "xmltooling" % "1.3.1" excludeAll (
      ExclusionRule(organization = "org.slf4j")
      ),
    "org.ow2.asm" % "asm" % "5.0.3",
    "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
    "org.rococoa" % "rococoa-core" % "0.5",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5",
    "org.scannotation" % "scannotation" % "1.0.3",
    "org.slf4j" % "jcl-over-slf4j" % "1.7.5",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.slf4j" % "slf4j-log4j12" % "1.6.1",
    "org.springframework" % "spring-aop" % "2.5.5",
    "org.springframework" % "spring-context" % "2.5.5",
    "org.springframework" % "spring-context-support" % "2.5.5" excludeAll (
      ExclusionRule(organization = "jasperreports", name = "jasperreports")
      ),
    "org.springframework" % "spring-jdbc" % "2.5.5",
    "org.springframework" % "spring-tx" % "2.5.5",
    "org.springframework" % "spring-web" % "2.5.5",
    "org.springframework" % "spring-webmvc" % "2.5.5" excludeAll (
      ExclusionRule(organization = "jasperreports", name = "jasperreports")
      ),
    "platform" % "platform" % "1.0",
    "stax" % "stax-api" % "1.0.1",
    "taglibs" % "standard" % "1.1.2",
    "tomcat" % "jsp-api" % "5.5.23",
    "uk.ac.ed.ph.qtiworks" % "qtiworks-jqtiplus" % "1.0-beta3" excludeAll(
      ExclusionRule(organization = "org.slf4j"),
      ExclusionRule(organization = "ch.qos.logback"),
      ExclusionRule(organization = "net.sf.saxon")
    ),
    "xml-resolver" % "xml-resolver" % "1.2",
    "xpp3" % "xpp3_min" % "1.1.4c"
  ),
  excludeDependencies ++= Seq(
    "com.google.guava" % "guava-jdk5",
    "asm" % "asm",
    "javax.servlet" % "servlet-api",
    "org.mortbay.jetty" % "servlet-api",
    "antlr" % "antlr",
    "javax.ws.rs" % "jsr311-api",
    "org.apache.ws.commons" % "XmlSchema",
    "org.apache.ws.commons.schema" % "XmlSchema",
    "woodstox" % "wstx-asl",
    "org.codehaus.woodstox" % "wstx-asl",
    "javassist" % "javassist",
    "org.sonatype.sisu.inject" % "cglib",
    "commons-beanutils" % "commons-beanutils", // we currently use "commons-beanutils-equella"
    "velocity" % "velocity",
    "rhino" % "js",
    "bouncycastle" % "bcprov-jdk15",
    "org.bouncycastle" % "bcprov-jdk15"
  ),
  dependencyOverrides += "commons-fileupload" % "commons-fileupload" % "aarons-hacked-1.2.1.2",
  collectJars := {
    val destDir = target.value / "jars"
    IO.delete(destDir)
    IO.copy((managedClasspath in Compile).value.map(af => (af.data, destDir / af.data.getName)))
  },
  runnerTasks(allPlugins)
).enablePlugins(JPFRunnerPlugin)

lazy val adminTool = (project in file("Source/Server/adminTool")).settings(legacyPaths).dependsOn(
  LocalProject("com_tle_platform_swing"),
  LocalProject("com_tle_platform_equella"),
  LocalProject("com_tle_webstart_admin")
)

lazy val equella = (project in file(".")).enablePlugins(JPFScanPlugin).aggregate(equellaserver, allPlugins, adminTool)

name := "Equella"
