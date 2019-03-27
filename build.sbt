import Environment._

buildEnv in Global := {
  sys.props
    .get("build.env")
    .orElse(sys.env.get("BUILD_ENV"))
    .flatMap {
      case "prod"  => Some(BuildEnv.Production)
      case "stage" => Some(BuildEnv.Stage)
      case "test"  => Some(BuildEnv.Test)
      case "dev"   => Some(BuildEnv.Developement)
      case _       => None
    }
    .getOrElse(BuildEnv.Developement)
}

scalaVersion in Global := "2.12.8"

scalafmtOnCompile in Global := true

lazy val root = Project(id = "nacos-sdk-scala", base = file("."))
  .aggregate(nacosDocs, nacosAkka, nacosClientScala)
  .settings(noPublish: _*)
  .settings(Environment.settings: _*)

lazy val nacosDocs = _project("nacos-docs")
  .enablePlugins(ParadoxMaterialThemePlugin)
  .dependsOn(nacosAkka, nacosClientScala)
  .settings(noPublish: _*)
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
      "github.base_url"        -> s"https://github.com/yangbajing/nacos-sdk-scala/tree/${version.value}",
      "version"                -> version.value,
      "scala.version"          -> scalaVersion.value,
      "scala.binary_version"   -> scalaBinaryVersion.value,
      "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/$versionAkka",
      "akka.version"           -> versionAkka))

lazy val nacosAkka = _project("nacos-akka")
  .dependsOn(nacosClientScala % "compile->compile;test->test")
  .settings(libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-stream" % versionAkka))

lazy val nacosClientScala = _project("nacos-client-scala")
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe"  % "config"     % "1.3.3",
      "com.lihaoyi"   %% "requests"  % "0.1.7",
      "org.scalatest" %% "scalatest" % "3.0.7" % Test))
  .settings(noPublish: _*)

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
    .settings(publishing: _*)
//    .settings(libraryDependencies ++= Seq(
//      "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.3" % "test,it",
//      "io.gatling"            % "gatling-test-framework"    % "3.0.3" % "test,it"))

lazy val versionAkka = "2.5.21"

def basicSettings =
  Seq(
    organization := "com.helloscala.fusion",
    organizationName := "ihongka",
    organizationHomepage := Some(url("http://ihongka.cn")),
    homepage := Some(url("http://ihongka.github.cn/akka-fusion")),
    startYear := Some(2018),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    headerLicense := Some(HeaderLicense.ALv2("2019", "ihongka")),
    scalacOptions ++= {
      var list = Seq(
        "-encoding",
        "UTF-8", // yes, this is 2 args
        "-feature",
        "-deprecation",
        "-unchecked",
        "-Xlint",
        "-Yno-adapted-args", //akka-http heavily depends on adapted args and => Unit implicits break otherwise
        "-Ypartial-unification",
        "-opt:l:inline",
        "-opt-inline-from",
        "-Ywarn-dead-code")
      if (scalaVersion.value.startsWith("2.12")) {
        list ++= Seq("-opt:l:inline", "-opt-inline-from")
      }
      if (buildEnv.value != BuildEnv.Developement) {
        list ++= Seq("-Xelide-below", "2001")
      }
      list
    },
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
    //            resolvers ++= Seq(
    //            "elasticsearch-releases" at "https://artifacts.elastic.co/maven"
    //        ),
    fork in run := true,
    fork in Test := true,
    parallelExecution in Test := false) ++ Environment.settings

def publishing =
  Seq(
    publishTo := (if (version.value.endsWith("-SNAPSHOT")) {
                    Some("Helloscala_sbt-public_snapshot".at(
                      "http://118.89.245.43:8081/artifactory/sbt-release;build.timestamp=" + new java.util.Date().getTime))
                  } else {
                    Some("Helloscala_sbt-public_release".at("http://118.89.245.43:8081/artifactory/libs-release"))
                  }),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials_ihongka"))

def noPublish = Seq(publish := ((): Unit), publishLocal := ((): Unit), publishTo := None)
