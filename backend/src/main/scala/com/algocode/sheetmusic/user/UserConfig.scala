package com.algocode.sheetmusic.user

import pureconfig.ConfigReader

import scala.concurrent.duration.Duration

case class UserConfig(defaultApiKeyValid: Duration) derives ConfigReader
