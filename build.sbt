organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lagomCassandraEnabled in ThisBuild := false
lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://via-at.com:9042")


val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val mariadbJavaClient = "org.mariadb.jdbc" % "mariadb-java-client" % "1.5.6"
val jug = "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.4"

lazy val security = (project in file("security"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-practice` = (project in file("."))
  .aggregate(`lagom-practice-api`, `lagom-practice-impl`, `lagom-practice-stream-api`, `lagom-practice-stream-impl`, security)

lazy val `lagom-practice-api` = (project in file("lagom-practice-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-practice-impl` = (project in file("lagom-practice-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceJdbc,
      lagomScaladslTestKit,
      mariadbJavaClient,
      jug,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`lagom-practice-api`, security)

lazy val `lagom-practice-stream-api` = (project in file("lagom-practice-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-practice-stream-impl` = (project in file("lagom-practice-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-practice-stream-api`, `lagom-practice-api`)

