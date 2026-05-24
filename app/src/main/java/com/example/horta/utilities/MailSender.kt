/* tentativa frustada de mandar email depois retomo

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

    private val fromEmail = "hortaybe@gmail.com"
    private val appPassword = ""

    fun setData(recipient: String, subject: String, messageBody: String) {
        this.recipient = recipient
        this.subject = subject
        this.messageBody = messageBody
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(fromEmail, appPassword)
            }
        })

        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(fromEmail))
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

*/
