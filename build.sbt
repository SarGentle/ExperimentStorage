import scala.language.postfixOps

name := "experimentstorage"
organization := "ru.nir"
version := "1.0.0"

val defaultSettings: Seq[Setting[_]] = Seq(
  scalaVersion := "2.13.8",
  scalacOptions := Seq(
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-deprecation",
    "-unchecked",
    "-Ymacro-annotations",
    "-Xlint:_,-byname-implicit"
  ),
  Compile / packageDoc / publishArtifact := false,
  ThisBuild / scalafmtOnCompile := true,
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForVersion),
  addCompilerPlugin(
    "org.typelevel" %% "kind-projector" % kindProjectorVersion cross CrossVersion.full
  )
)

val projectSettings: Seq[Setting[_]] = Seq(
  Universal / mappings ++= Seq(
    (Compile / resourceDirectory).value / "application.conf" -> "conf/example/application.conf",
    (Compile / resourceDirectory).value / "jvmopts" -> "conf/jvmopts"
  ),
  makeBatScripts := Seq.empty
)

lazy val zioVersion              = "2.0.9"
lazy val zioCatsVersion          = "23.0.0.2"
lazy val zioKafkaVersion         = "2.1.1"
lazy val catsVersion             = "2.9.0"
lazy val catsEffectVersion       = "3.4.4"
lazy val doobieVersion           = "1.0.0-RC2"
lazy val hikariVersion           = "5.0.1"
lazy val postgresqlVersion       = "42.5.4"
lazy val pureconfigVersion       = "0.17.2"
lazy val logbackVersion          = "1.4.5"
lazy val circeVersion            = "0.14.3"
lazy val scalatestVersion        = "3.2.15"
lazy val http4sVersion           = "0.23.15"
lazy val kindProjectorVersion    = "0.13.2"
lazy val betterMonadicForVersion = "0.3.1"


lazy val zio = Seq(
  "dev.zio" %% "zio"              % zioVersion,
  "dev.zio" %% "zio-streams"      % zioVersion,
  "dev.zio" %% "zio-kafka"        % zioKafkaVersion,
  "dev.zio" %% "zio-interop-cats" % zioCatsVersion
)

lazy val circe = Seq(
  "io.circe" %% "circe-generic"        % circeVersion,
  "io.circe" %% "circe-literal"        % circeVersion,
  "io.circe" %% "circe-parser"         % circeVersion,
  "io.circe" %% "circe-core"           % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion
)

lazy val doobie = Seq(
  "org.tpolecat"  %% "doobie-core"           % doobieVersion,
  "org.tpolecat"  %% "doobie-postgres"       % doobieVersion,
  "org.tpolecat"  %% "doobie-hikari"         % doobieVersion,
  "org.tpolecat"  %% "doobie-postgres-circe" % doobieVersion,
  "com.zaxxer"     % "HikariCP"              % hikariVersion exclude ("org.slf4j", "slf4j-api"),
  "org.postgresql" % "postgresql"            % postgresqlVersion
)

lazy val tests = Seq(
  "org.scalatest" %% "scalatest"    % scalatestVersion % Test,
  "dev.zio"       %% "zio-test"     % zioVersion       % Test,
  "dev.zio"       %% "zio-test-sbt" % zioVersion       % Test
)

val dependenciesSettings = Seq(
  libraryDependencies ++= Seq(
    "com.github.pureconfig"         %% "pureconfig"                    % pureconfigVersion,
    "org.typelevel"                 %% "cats-effect"                   % catsEffectVersion,
    "org.typelevel"                 %% "cats-effect-kernel"            % catsEffectVersion,
    "org.typelevel"                 %% "cats-effect-std"               % catsEffectVersion,
    "ch.qos.logback"                 % "logback-classic"               % logbackVersion,
    "org.http4s"                    %% "http4s-blaze-server"           % http4sVersion,
    "org.http4s"                    %% "http4s-blaze-client"           % http4sVersion,
    "org.http4s"                    %% "http4s-circe"                  % http4sVersion,
    "org.http4s"                    %% "http4s-dsl"                    % http4sVersion,
    "org.slf4j"                      % "slf4j-api"                     % "1.7.36",
    "org.webjars"                   % "bootstrap"                      % "5.3.0",
    "org.webjars"                   % "jquery"                         % "3.6.4",
  ) ++ circe ++ doobie ++ zio ++ tests
)

val jvmOpts = Seq(
  "-Xss8m",
  "-Xmx2g",
  "-Dfile.encoding=UTF-8",
  "-Djdk.http.auth.tunneling.disabledSchemes=\"\""
)

val debug = Seq(
  Revolver.enableDebugging(port = 5050, suspend = true),
  reStart / javaOptions ++= jvmOpts,
  Test / javaOptions ++= jvmOpts
)

lazy val testproject = project
  .in(file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(Compile / mainClass := Some("ru.nir.testproject.Main"))
  .settings(defaultSettings ++ projectSettings)
  .settings(dependenciesSettings)
  .settings(debug)
