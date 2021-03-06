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
package silhouette.jwt

import silhouette.util.{ Reads, Writes }

import scala.util.Try

/**
 * Transforms a string into a [[JwtClaims]] object.
 */
trait JwtReads extends Reads[String, Try[JwtClaims]]

/**
 * Transforms a [[JwtClaims]] object into a string.
 */
trait JwtWrites extends Writes[JwtClaims, Try[String]]

/**
 * JWT transformer combinator.
 */
trait JwtFormat extends JwtReads with JwtWrites
