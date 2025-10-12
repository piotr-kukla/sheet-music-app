package com.algocode.sheetmusic.email.sender

import com.algocode.sheetmusic.email.EmailData
import com.algocode.sheetmusic.test.BaseTest

class DummyEmailSenderTest extends BaseTest:
  it should "send scheduled email" in {
    DummyEmailSender(EmailData("test@sml.com", "subject", "content"))
    DummyEmailSender.findSentEmail("test@sml.com", "subject").isDefined shouldBe true
  }
