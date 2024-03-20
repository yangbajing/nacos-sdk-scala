/*
 * Copyright 2020 me.yangbajing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yangbajing.nacos4s.client.util

import scala.annotation.tailrec

object StringUtils {
  val BLACK_CHAR: Char = ' '

  @inline def isNoneEmpty(s: CharSequence): Boolean = !isEmpty(s)

  def isEmpty(s: CharSequence): Boolean =
    (s eq null) || s.length() == 0

  @inline def isNoneBlank(s: CharSequence): Boolean = !isBlank(s)

  def isBlank(s: CharSequence): Boolean = {
    @tailrec def isNoneBlankChar(s: CharSequence, i: Int): Boolean =
      if (i < s.length()) s.charAt(i) != BLACK_CHAR || isNoneBlankChar(s, i + 1) else false

    isEmpty(s) || !isNoneBlankChar(s, 0)
  }
}
