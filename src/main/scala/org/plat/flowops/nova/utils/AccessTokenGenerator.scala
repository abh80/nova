package org.plat.flowops.nova.utils

import org.apache.commons.lang3.RandomStringUtils

import java.security.MessageDigest
import java.util.{ Base64, UUID }
import java.util.concurrent.atomic.AtomicInteger

object AccessTokenGenerator:
  private val ACCESS_TOKEN_PREFIX = "flat-"
  private val counter             = new AtomicInteger(0)

  def generateAccessToken(): (String, String) =
    val timestamp    = System.currentTimeMillis()
    val randomString = RandomStringUtils.randomAlphabetic(26)
    val hash         = hashString(s"${counter.incrementAndGet()}-$timestamp-$randomString")

    val accessTokenId = UUID.randomUUID().toString

    (s"$ACCESS_TOKEN_PREFIX$hash", accessTokenId)

  private def hashString(input: String): String =
    val digest    = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.getBytes("UTF-8"))
    Base64.getUrlEncoder.withoutPadding.encodeToString(hashBytes)
