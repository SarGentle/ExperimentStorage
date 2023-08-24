addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.2")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"        % "2.4.0")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
// project/plugins.sbt
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.34")

//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
addDependencyTreePlugin
