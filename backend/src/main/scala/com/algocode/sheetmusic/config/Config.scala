package com.algocode.sheetmusic.config

import com.algocode.sheetmusic.email.EmailConfig
import com.algocode.sheetmusic.http.HttpConfig
import com.algocode.sheetmusic.infrastructure.DBConfig
import com.algocode.sheetmusic.logging.Logging
import com.algocode.sheetmusic.passwordreset.PasswordResetConfig
import com.algocode.sheetmusic.user.UserConfig
import com.algocode.sheetmusic.version.BuildInfo
import pureconfig.{ConfigReader, ConfigSource}

import scala.collection.immutable.TreeMap

/** Maps to the `application.conf` file. Configuration for all modules of the application. */
case class Config(db: DBConfig, api: HttpConfig, email: EmailConfig, passwordReset: PasswordResetConfig, user: UserConfig)
    derives ConfigReader

object Config extends Logging:
  def log(config: Config): Unit =
    val baseInfo = s"""
                      |Sheetmusic configuration:
                      |-----------------------
                      |DB:             ${config.db}
                      |API:            ${config.api}
                      |Email:          ${config.email}
                      |Password reset: ${config.passwordReset}
                      |User:           ${config.user}
                      |
                      |Build & env info:
                      |-----------------
                      |""".stripMargin

    val info = TreeMap(BuildInfo.toMap.toSeq*).foldLeft(baseInfo) { case (str, (k, v)) =>
      str + s"$k: $v\n"
    }

    logger.info(info)
  end log

  def read: Config = ConfigSource.default.loadOrThrow[Config]
end Config
