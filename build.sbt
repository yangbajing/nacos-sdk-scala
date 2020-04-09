val versionScala212 = "2.12.10"
val versionScala213 = "2.13.1"
val versionAkka = "2.6.4"
val versionNacos = "1.2.1"
val versionPlay = "2.8.1"
val versionConfig = "1.4.0"
val versionScalaCollectionCompat = "2.1.4"
val versionScalatest = "3.1.1"

ThisBuild / scalaVersion := versionScala212

ThisBuild / crossScalaVersions := Seq(versionScala212, versionScala213)

ThisBuild / scalafmtOnCompile := true

lazy val root = Project(id = "nacos-sdk-scala", base = file("."))
  .aggregate(nacosDocs, nacosPlayWs, nacosAkka, nacosClientScala)
  .settings(skip in publish := true)

lazy val nacosDocs = _project("nacos-docs")
  .enablePlugins(ParadoxMaterialThemePlugin)
  .dependsOn(nacosPlayWs, nacosAkka, nacosClientScala)
  .settings(skip in publish := true)
  .settings(
    Compile / paradoxMaterialTheme ~= {
      _.withLanguage(java.util.Locale.SIMPLIFIED_CHINESE)
        .withColor("indigo", "red")
        .withRepository(uri("https://github.com/yangbajing/nacos-sdk-scala"))
        .withSocial(
          uri("http://yangbajing.github.io/nacos-sdk-scala/"),
          uri("https://github.com/yangbajing"),
          uri("https://weibo.com/yangbajing"))
    },
    paradoxProperties ++= Map(
        "github.base_url" -> s"https://github.com/yangbajing/nacos-sdk-scala/tree/${version.value}",
        "version" -> version.value,
        "scala.version" -> scalaVersion.value,
        "scala.binary_version" -> scalaBinaryVersion.value,
        "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/$versionAkka",
        "akka.version" -> versionAkka))

lazy val nacosPlayWs = _project("nacos-play-ws")
  .dependsOn(nacosClientScala % "compile->compile;test->test")
  .settings(libraryDependencies ++= Seq("com.typesafe.play" %% "play-ahc-ws" % versionPlay))

lazy val nacosAkka = _project("nacos-akka")
  .dependsOn(nacosClientScala % "compile->compile;test->test")
  .settings(libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-discovery" % versionAkka))

lazy val nacosClientScala = _project("nacos-client-scala").settings(
  libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % versionScalaCollectionCompat,
      "com.typesafe" % "config" % versionConfig,
      "com.alibaba.nacos" % "nacos-client" % versionNacos,
      "org.scalatest" %% "scalatest" % versionScalatest % Test))

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(basicSettings: _*)
    .settings(publishing: _*)
//    .settings(libraryDependencies ++= Seq(
//      "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.3" % "test,it",
//      "io.gatling"            % "gatling-test-framework"    % "3.0.3" % "test,it"))

def basicSettings =
  Seq(
    organization := "me.yangbajing.nacos4s",
    organizationName := "yangbajing",
    organizationHomepage := Some(url("https://yangbajing.me")),
    homepage := Some(url("https://yangbajing.github.cn/nacos-sdk-scala")),
    startYear := Some(2020),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    headerLicense := Some(HeaderLicense.ALv2("2020", "me.yangbajing")),
    scalacOptions ++= Seq(
        "-encoding",
        "UTF-8", // yes, this is 2 args
        "-feature",
        "-deprecation",
        "-unchecked",
        "-Xlint",
        "-opt:l:inline",
        "-opt-inline-from",
        "-Ywarn-dead-code"),
    javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
    shellPrompt := { s =>
      Project.extract(s).currentProject.id + " > "
    },
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _*)                => MergeStrategy.first
      case PathList("io", "netty", xs @ _*)                     => MergeStrategy.first
      case PathList("jnr", xs @ _*)                             => MergeStrategy.first
      case PathList("com", "datastax", xs @ _*)                 => MergeStrategy.first
      case PathList("com", "kenai", xs @ _*)                    => MergeStrategy.first
      case PathList("org", "objectweb", xs @ _*)                => MergeStrategy.first
      case PathList(ps @ _*) if ps.last.endsWith(".html")       => MergeStrategy.first
      case "application.conf"                                   => MergeStrategy.concat
      case "META-INF/io.netty.versions.properties"              => MergeStrategy.first
      case PathList("org", "slf4j", xs @ _*)                    => MergeStrategy.first
      case "META-INF/native/libnetty-transport-native-epoll.so" => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    fork in run := true,
    fork in Test := true,
    parallelExecution in Test := false)

def publishing =
  Seq(
    bintrayOrganization := Some("helloscala"),
    bintrayRepository := "maven",
    developers := List(
        Developer(
          id = "yangbajing",
          name = "Yang Jing",
          email = "yang.xunjing@qq.com",
          url = url("https://github.com/yangbajing"))),
    scmInfo := Some(
        ScmInfo(
          url("https://github.com/yangbajing/nacos-sdk-scala"),
          "scm:git:git@github.com:yangbajing/nacos-sdk-scala.git")))
