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
package silhouette.http

/**
 * Decorates a framework specific response implementation.
 *
 * Frameworks should create an implicit conversion between the implementation of this pipeline and
 * the Framework specific response instance.
 *
 * @tparam P The type of the response.
 */
protected[silhouette] trait ResponsePipeline[P] {

  /**
   * The framework specific response implementation.
   */
  val response: P

  /**
   * A marker flag which indicates that an operation on an authenticator was processed and
   * therefore it shouldn't be updated automatically.
   *
   * Due the fact that the update method gets called on every subsequent request to update the
   * authenticator related data in the backing store and in the result, it isn't possible to
   * discard or renew the authenticator simultaneously. This is because the "update" method would
   * override the result created by the "renew" or "discard" method, because it will be executed
   * as last in the chain.
   *
   * As example:
   * If we discard the session in a Silhouette endpoint then it will be removed from session. But
   * at the end the update method will embed the session again, because it gets called with the
   * result of the endpoint.
   */
  protected[silhouette] val touched = false

  /**
   * Gets all headers.
   *
   * The HTTP RFC2616 allows duplicate response headers with the same name. Therefore we must define a
   * header values as sequence of values.
   *
   * @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2
   *
   * @return All headers.
   */
  def headers: Map[String, Seq[String]]

  /**
   * Gets the values for a header.
   *
   * The HTTP RFC2616 allows duplicate response headers with the same name. Therefore we must define a
   * header values as sequence of values.
   *
   * @see https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2
   *
   * @param name The name of the header for which the values should be returned.
   * @return A list of header values for the given name or an empty list if no header for the given name could be found.
   */
  def header(name: String): Seq[String] = headers.getOrElse(name, Nil)

  /**
   * Creates a new response pipeline with the given headers.
   *
   * This method must override any existing header with the same name. If multiple headers with the
   * same name are given to this method, then the values must be composed into a list.
   *
   * If a response holds the following headers, then this method must implement the following behaviour:
   * {{{
   *   Map(
   *     "TEST1" -> Seq("value1", "value2"),
   *     "TEST2" -> Seq("value1")
   *   )
   * }}}
   *
   * Append a new header:
   * {{{
   *   withHeaders("TEST3" -> "value1")
   *
   *   Map(
   *     "TEST1" -> Seq("value1", "value2"),
   *     "TEST2" -> Seq("value1"),
   *     "TEST3" -> Seq("value1")
   *   )
   * }}}
   *
   * Override the header `TEST1` with a new value:
   * {{{
   *   withHeaders("TEST1" -> "value3")
   *
   *   Map(
   *     "TEST1" -> Seq("value3"),
   *     "TEST2" -> Seq("value1")
   *   )
   * }}}
   *
   * Compose headers with the same name:
   * {{{
   *   withHeaders("TEST1" -> "value3", "TEST1" -> "value4")
   *
   *   Map(
   *     "TEST1" -> Seq("value3", "value4"),
   *     "TEST2" -> Seq("value1")
   *   )
   * }}}
   *
   * @param headers The headers to set.
   * @return A new response pipeline instance with the set headers.
   */
  def withHeaders(headers: (String, String)*): ResponsePipeline[P]

  /**
   * Gets the list of cookies.
   *
   * @return The list of cookies.
   */
  def cookies: Seq[Cookie]

  /**
   * Gets a cookie.
   *
   * @param name The name for which the cookie should be returned.
   * @return Some cookie or None if no cookie for the given name could be found.
   */
  def cookie(name: String): Option[Cookie] = cookies.find(_.name == name)

  /**
   * Creates a new response pipeline with the given cookies.
   *
   * This method must override any existing cookie with the same name. If multiple cookies with the
   * same name are given to this method, then the last cookie in the list wins.
   *
   * If a response holds the following cookies, then this method must implement the following behaviour:
   * {{{
   *   Seq(
   *     Cookie("test1", "value1"),
   *     Cookie("test2", "value2")
   *   )
   * }}}
   *
   * Append a new cookie:
   * {{{
   *   withCookies(Cookie("test3", "value3"))
   *
   *   Seq(
   *     Cookie("test1", "value1"),
   *     Cookie("test2", "value2"),
   *     Cookie("test3", "value3")
   *   )
   * }}}
   *
   * Override the cookie `test1`:
   * {{{
   *   withCookies(Cookie("test1", "value3"))
   *
   *   Seq(
   *     Cookie("test1", "value3"),
   *     Cookie("test2", "value2")
   *   )
   * }}}
   *
   * Use the last cookie if multiple cookies with the same name are given:
   * {{{
   *   withCookies(Cookie("test1", "value3"), Cookie("test1", "value4"))
   *
   *   Seq(
   *     Cookie("test1", "value4"),
   *     Cookie("test2", "value2")
   *   )
   * }}}
   *
   * @param cookies The cookies to set.
   * @return A new response pipeline instance with the set cookies.
   */
  def withCookies(cookies: Cookie*): ResponsePipeline[P]

  /**
   * Gets the session data.
   *
   * @return The session data.
   */
  def session: Map[String, String]

  /**
   * Creates a new response pipeline with the given session data.
   *
   * This method must override any existing session data with the same name. If multiple session data with the
   * same key are given to this method, then the last session data in the list wins.
   *
   * If a response holds the following session data, then this method must implement the following behaviour:
   * {{{
   *   Map(
   *     "test1" -> "value1",
   *     "test2" -> "value2"
   *   )
   * }}}
   *
   * Append new session data:
   * {{{
   *   withSession("test3" -> "value3")
   *
   *   Map(
   *     "test1" -> "value1",
   *     "test2" -> "value2",
   *     "test3" -> "value3"
   *   )
   * }}}
   *
   * Override the session data with the key `test1`:
   * {{{
   *   withSession("test1" -> "value3")
   *
   *   Map(
   *     "test1" -> "value3",
   *     "test2" -> "value2"
   *   )
   * }}}
   *
   * Use the last session data if multiple session data with the same key are given:
   * {{{
   *   withSession("test1" -> "value3", "test1" -> "value4")
   *
   *   Map(
   *     "test1" -> "value4",
   *     "test2" -> "value2"
   *   )
   * }}}
   *
   * @param data The session data to set.
   * @return A new response pipeline instance with the set session data.
   */
  def withSession(data: (String, String)*): ResponsePipeline[P]

  /**
   * Creates a new response pipeline without the given session keys.
   *
   * @param keys The session keys to remove.
   * @return A new response pipeline instance with the removed session data.
   */
  def withoutSession(keys: String*): ResponsePipeline[P]

  /**
   * Unboxes the framework specific response implementation.
   *
   * @return The framework specific response implementation.
   */
  def unbox: P

  /**
   * Touches a response.
   *
   * @return A touched response pipeline.
   */
  protected[silhouette] def touch: ResponsePipeline[P]
}
