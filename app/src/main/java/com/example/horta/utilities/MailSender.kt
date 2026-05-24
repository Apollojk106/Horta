package com.example.horta.utils

import android.os.AsyncTask
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties

class MailSender : AsyncTask<Void, Void, Boolean>() {

    private lateinit var recipient: String
    private lateinit var subject: String
    private lateinit var messageBody: String

    // ⚠️ SUBSTITUA PELOS SEUS DADOS
    private val fromEmail = "seu-email@gmail.com"
    private val appPassword = "sua-senha-de-app-16-digitos"

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

    override fun onPostExecute(success: Boolean) {
        // Callback opcional para saber se enviou
    }
}