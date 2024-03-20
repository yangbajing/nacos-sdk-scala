lazy val root = Project(id = "nacos-sdk-scala", base = file("."))
  .aggregate(nacosDocs, nacosPlayWs, nacosPekko, nacosClientScala)
  .settings(publish / skip := true)

lazy val nacosDocs = _project("nacos-docs")
  .enablePlugins(ParadoxMaterialThemePlugin, GhpagesPlugin)
  .dependsOn(nacosPlayWs, nacosPekko, nacosClientScala)
  .settings(publish / skip := true)
  .settings(
    Compile / paradoxMaterialTheme ~= {
      _.withLanguage(java.util.Locale.SIMPLIFIED_CHINESE)
        .withColor("indigo", "red")
        .withRepository(uri("https://github.com/yangbajing/nacos-sdk-scala"))
        .withSocial(
          uri("http://yangbajing.github.io/nacos-sdk-scala/"),
          uri("https://github.com/yangbajing"))
    },
    paradoxProperties ++= Map(
      "github.base_url" -> s"https://github.com/yangbajing/nacos-sdk-scala/tree/${version.value}",
      "version" -> version.value,
      "scala.version" -> scalaVersion.value,
      "scala.binary_version" -> scalaBinaryVersion.value,
      "scaladoc.pekko.base_url" -> s"http://doc.pekko.io/api/$versionPekko",
      "pekko.version" -> versionPekko),
    git.remoteRepo := "https://github.com/yangbajing/nacos-sdk-scala.git",
    // ThisProject / GitKeys.gitReader := baseDirectory(base => new DefaultReadableGit(base)).value,
    siteSourceDirectory := target.value / "paradox" / "site" / "main",
    ghpagesNoJekyll := true)

lazy val nacosPlayWs = _project("nacos-play-ws")
  .dependsOn(nacosClientScala % "compile->compile;test->test")
  .settings(libraryDependencies ++= Seq(
    "org.apache.pekko" %% "pekko-stream-typed" % versionPekko,
    "org.apache.pekko" %% "pekko-actor-testkit-typed" % versionPekko % Test,
    ("org.playframework" %% "play-ahc-ws" % versionPlay).excludeAll(ExclusionRule("org.apache.pekko"))))

lazy val nacosPekko = _project("nacos-pekko")
  .dependsOn(nacosClientScala % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-testkit-typed" % versionPekko % Test,
      "org.apache.pekko" %% "pekko-discovery" % versionPekko))

lazy val nacosClientScala = _project("nacos-client-scala").settings(
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-collection-compat" % versionScalaCollectionCompat,
    "com.typesafe" % "config" % versionConfig,
    "com.alibaba.nacos" % "nacos-client" % versionNacos,
    "org.scalatest" %% "scalatest" % versionScalatest % Test))

val versionScala3 = "3.3.3"
val versionPekko = "1.0.2"
val versionNacos = "2.3.1"
val versionPlay = "3.0.2"
val versionConfig = "1.4.3"
val versionScalaCollectionCompat = "2.11.0"
val versionScalatest = "3.2.18"

ThisBuild / scalaVersion := versionScala3
ThisBuild / version := "2.0.0"

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
    licenses += ("Apache-2.0", new URI("https://www.apache.org/licenses/LICENSE-2.0.txt").toURL),
    headerLicense := Some(HeaderLicense.ALv2("2020", "me.yangbajing")),
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8", // yes, this is 2 args
      "-feature",
      "-deprecation",
      "-unchecked"),
    run / javaOptions ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
    shellPrompt := { s =>
      Project.extract(s).currentProject.id + " > "
    },
    assembly / test := {},
    assembly / assemblyMergeStrategy := {
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
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    libraryDependencies ++= Seq("ch.qos.logback" % "logback-classic" % "1.5.3" % Test),
    run / fork := true,
    Test / fork := true,
    Test / parallelExecution := false)

def publishing =
  Seq(
//    bintrayOrganization := Some("helloscala"),
//    bintrayRepository := "maven",
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
