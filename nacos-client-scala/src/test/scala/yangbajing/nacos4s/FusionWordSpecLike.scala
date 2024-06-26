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

package yangbajing.nacos4s

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{EitherValues, OptionValues}

import scala.concurrent.duration.*

trait FusionTestValues extends OptionValues with EitherValues with Matchers

trait FusionScalaFutures extends ScalaFutures {
  implicit override def patienceConfig: PatienceConfig = PatienceConfig(patienceTimeout, 15.millis)

  protected def patienceTimeout: FiniteDuration = 10.second
}

trait FusionWordSpecLike extends AnyWordSpecLike with FusionTestValues
