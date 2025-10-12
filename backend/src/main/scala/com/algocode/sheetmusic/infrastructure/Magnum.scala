package com.algocode.sheetmusic.infrastructure

import com.augustnagro.magnum.DbCodec
import com.algocode.sheetmusic.logging.Logging
import com.algocode.sheetmusic.util.Strings.*

import java.time.{Instant, OffsetDateTime, ZoneOffset}

/** Magnum codecs for custom types, useful when writing SQL queries. */
object Magnum extends Logging:
  given DbCodec[Instant] = summon[DbCodec[OffsetDateTime]].biMap(_.toInstant, _.atOffset(ZoneOffset.UTC))

  given idCodec[T]: DbCodec[Id[T]] = DbCodec.StringCodec.biMap(_.asId[T], _.toString)
  given DbCodec[Hashed] = DbCodec.StringCodec.biMap(_.asHashed, _.toString)
  given DbCodec[LowerCased] = DbCodec.StringCodec.biMap(_.toLowerCased, _.toString)
end Magnum
