/**
 * Licensed to the Minutemen Group under one or more contributor license
 * agreements. See the COPYRIGHT file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import sbt._

object Dependencies {

  object Version {
    val specs2 = "3.8.6"
    val circe = "0.7.0"
  }

  val resolvers = Seq()

  object Library {

    object Specs2 {
      val core = "org.specs2" %% "specs2-core" % Version.specs2
      val matcherExtra = "org.specs2" %% "specs2-matcher-extra" % Version.specs2
      val mock = "org.specs2" %% "specs2-mock" % Version.specs2
    }

    object Circe {
      val core = "io.circe" %% "circe-core" % Version.circe
      val generic = "io.circe" %% "circe-generic" % Version.circe
      val parser = "io.circe" %% "circe-parser" % Version.circe
    }

    val mockito = "org.mockito" % "mockito-core" % "1.10.19"
    val jbcrypt = "org.mindrot" % "jbcrypt" % "0.3m"
    val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.13"
    val inject = "javax.inject" % "javax.inject" % "1"
    val commonCodec = "commons-codec" % "commons-codec" % "1.10"
    val jose4j = "org.bitbucket.b_c" % "jose4j" % "0.5.4"
    val jsonAst = "org.mdedetrich" %% "scala-json-ast" % "1.0.0-M7"
    val bouncyCastle = "org.bouncycastle" % "bcprov-jdk15on" % "1.56"
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  }
}
