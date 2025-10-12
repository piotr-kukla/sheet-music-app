package com.algocode.sheetmusic.infrastructure

import com.algocode.sheetmusic.config.Sensitive
import pureconfig.ConfigReader

case class DBConfig(username: String, password: Sensitive, url: String, migrateOnStart: Boolean, driver: String) derives ConfigReader
