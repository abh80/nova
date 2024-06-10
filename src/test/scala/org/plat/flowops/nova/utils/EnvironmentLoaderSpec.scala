package org.plat.flowops.nova.utils

import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EnvironmentLoaderSpec extends AnyFlatSpec with MockFactory with Matchers {

  "getEnvironmentValue" should "return the value of environment value if set" in {
    val varName = "TEST_VAR"
    val envValue = "test_value"

    val result = EnvironmentLoader.getEnvironmentVariable(varName, DefaultEnvironmentConstants.PORT)

    result shouldEqual envValue
  }

  it should "return the default value if env variable is not set and not required" in {
    val varName = "UNSET_VARIABLE"

    val result = EnvironmentLoader.getEnvironmentVariable(varName, DefaultEnvironmentConstants.PORT)

    result shouldEqual DefaultEnvironmentConstants.PORT.value
  }

  it should "throw a RuntimeException when env variable is not set and required" in {
    val varName = "UNSET_VARIABLE"

    val exception = intercept[RuntimeException] {
      EnvironmentLoader.getRequiredEnvironmentVariable(varName)
    }

    exception.getMessage shouldBe s"Environment variable $varName is required but not set"
  }

}