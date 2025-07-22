package com.example.smstransactionuploader

import android.content.Context
import android.util.Log
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class GoogleSheetsUploader(private val context: Context) {

    private val spreadsheetId = "YOUR_SPREADSHEET_ID" // Replace with your spreadsheet ID
    private val sheetName = "Sheet1" // Replace with your sheet name

    fun uploadData(transaction: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val credential = getCredentials()
                val sheetsService = Sheets.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                    .setApplicationName("SmsTransactionUploader")
                    .build()

                val values = listOf(
                    listOf(
                        transaction.amount,
                        transaction.merchant,
                        transaction.balance
                    )
                )

                val body = ValueRange().setValues(values)
                val result: AppendValuesResponse = sheetsService.spreadsheets().values()
                    .append(spreadsheetId, sheetName, body)
                    .setValueInputOption("RAW")
                    .execute()
                Log.d("GoogleSheetsUploader", "Appended data: ${result.updates.updatedRange}")
            } catch (e: IOException) {
                Log.e("GoogleSheetsUploader", "Error uploading data", e)
            }
        }
    }

    private fun getCredentials(): Credential {
        // This is where you would implement your OAuth 2.0 flow.
        // For this example, we'll use a service account for simplicity.
        // You would need to create a service account in the Google Cloud Console
        // and add the JSON key file to your project's `res/raw` directory.
        val inputStream = context.resources.openRawResource(R.raw.credentials)
        return GoogleCredential.fromStream(inputStream)
            .createScoped(listOf("https.www.googleapis.com/auth/spreadsheets"))
    }
}
