package com.example.smstransactionuploader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    private val smsParser = SmsParser()
    private lateinit var googleSheetsUploader: GoogleSheetsUploader

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in messages) {
                val smsBody = message.messageBody
                val transaction = smsParser.parseSms(smsBody)
                if (transaction != null) {
                    Log.d("SmsReceiver", "Transaction parsed: $transaction")
                    // Initialize and use the GoogleSheetsUploader
                    googleSheetsUploader = GoogleSheetsUploader(context)
                    googleSheetsUploader.uploadData(transaction)
                }
            }
        }
    }
}
