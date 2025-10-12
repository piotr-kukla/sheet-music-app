package com.algocode.sheetmusic.util

import com.algocode.sheetmusic.util.Strings.{asId, Id}

trait IdGenerator:
  def nextId[U](): Id[U]

object DefaultIdGenerator extends IdGenerator:
  override def nextId[U](): Id[U] = SecureRandomIdGenerator.Strong.generate.asId[U]
