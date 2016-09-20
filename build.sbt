name := "basic-project"

organization := "example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.4", "2.11.2")

resolvers ++= Seq(
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases"  at "http://scala-tools.org/repo-releases")

resolvers ++= Seq(
  "sonatype-snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "sonatype-releases"  at "http://oss.sonatype.org/content/repositories/releases")

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

val scalaReflect     = "org.scala-lang"  %  "scala-reflect"     % "2.11.7"   % "provided"
val specs2Core       = "org.specs2"      %% "specs2-core"       % "2.4.17"   % "test"
val specs2ScalaCheck = "org.specs2"      %% "specs2-scalacheck" % "2.4.17"   % "test"

libraryDependencies ++= Seq(
  //"org.scalatest" %% "scalatest" % "2.2.1" % "test",
  //"org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  //"org.scalatest" % "scalatest_2.9.2" % "1.8" % "test" ,
  //"joda-time" % "joda-time" % "1.6.2" ,
  //"junit" % "junit" % "4.10",
  //"org.testng" % "testng" % "6.1.1" % "test",
  "org.parboiled" %% "parboiled" % "2.1.3",
 "org.specs2" %% "specs2-core" % "2.4.17" % "test",
  "org.specs2" %% "specs2-scalacheck" % "2.4.17" % "test"
)


scalacOptions in Test ++= Seq("-Yrangepos")

initialCommands := "import example._"
