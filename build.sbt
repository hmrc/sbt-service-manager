import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtAutoBuildPlugin

val pluginName = "sbt-service-manager"

lazy val root = Project(pluginName, base = file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    sbtPlugin := true,
    targetJvm := "jvm-1.8",
    scalaVersion := "2.12.8",
    organization := "uk.gov.hmrc",
    makePublicallyAvailableOnBintray := true,
    ArtifactDescription()
  )
  .settings(
    majorVersion := 0,
    crossSbtVersions := List("0.13.18", "1.3.13"),
    crossScalaVersions := List("2.11.12", "2.12.12"),
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo
    ))
