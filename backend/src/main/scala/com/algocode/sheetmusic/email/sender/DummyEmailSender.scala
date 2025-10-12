package com.algocode.sheetmusic.email.sender

import com.algocode.sheetmusic.email.EmailData
import com.algocode.sheetmusic.logging.Logging

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}
import scala.jdk.CollectionConverters.*

object DummyEmailSender extends EmailSender with Logging:
  private val sentEmails: BlockingQueue[EmailData] = new LinkedBlockingQueue[EmailData]()

  def reset(): Unit = sentEmails.clear()

  override def apply(email: EmailData): Unit =
    sentEmails.put(email)
    logger.info(s"Would send email, if this wasn't a dummy email service implementation: $email")

  def findSentEmail(recipient: String, subjectContains: String): Option[EmailData] =
    sentEmails.iterator().asScala.find(email => email.recipient == recipient && email.subject.contains(subjectContains))
end DummyEmailSender
