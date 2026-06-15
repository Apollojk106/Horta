package com.example.horta.utilities

import android.os.AsyncTask
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties

class MailSender : AsyncTask<Void, Void, Boolean>() {

    private lateinit var recipient: String
    private lateinit var subject: String
    private lateinit var messageBody: String

    fun setData(recipient: String, subject: String, messageBody: String) {
        this.recipient = recipient
        this.subject = subject
        this.messageBody = messageBody
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", EmailConfig.SMTP_HOST)
            put("mail.smtp.port", EmailConfig.SMTP_PORT)
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EmailConfig.FROM_EMAIL, EmailConfig.APP_PASSWORD)
            }
        })

        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(EmailConfig.FROM_EMAIL))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                setSubject(subject)
                setText(messageBody)
            }
            Transport.send(message)
            true
        } catch (e: MessagingException) {
            e.printStackTrace()
            false
        }
    }
}