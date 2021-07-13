import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._

val pluginName = "sbt-service-manager"

lazy val root = Project(pluginName, base = file("."))
  .settings(
    sbtPlugin := true,
    targetJvm := "jvm-1.8",
    scalaVersion := "2.12.12",
    organization := "uk.gov.hmrc",
    isPublicArtefact := true,
    ArtifactDescription()
  )
  .settings(
    majorVersion := 0,
    crossSbtVersions := List("0.13.18", "1.3.13"),
    crossScalaVersions := List("2.11.12", "2.12.12"),
    resolvers := Seq(
      Resolver.typesafeRepo("releases"),
      Resolver.jcenterRepo
    ))
