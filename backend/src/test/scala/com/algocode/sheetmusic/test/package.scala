package com.algocode.sheetmusic

import com.algocode.sheetmusic.config.Config
import com.softwaremill.quicklens.*

import scala.concurrent.duration.*

package object test:
  val DefaultConfig: Config = Config.read
  val TestConfig: Config = DefaultConfig.modify(_.email.emailSendInterval).setTo(100.milliseconds)
