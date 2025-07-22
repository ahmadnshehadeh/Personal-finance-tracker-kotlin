package com.example.smstransactionuploader

data class Transaction(
    val amount: String,
    val merchant: String,
    val balance: String
)

class SmsParser {

    fun parseSms(sms: String): Transaction? {
        val amountRegex = "You spent \\$(\\d+\\.\\d+)".toRegex()
        val merchantRegex = "at (.*?)\\.".toRegex()
        val balanceRegex = "Remaining balance: \\$(\\d+\\.\\d+)".toRegex()

        val amountMatch = amountRegex.find(sms)
        val merchantMatch = merchantRegex.find(sms)
        val balanceMatch = balanceRegex.find(sms)

        return if (amountMatch != null && merchantMatch != null && balanceMatch != null) {
            Transaction(
                amount = amountMatch.groupValues[1],
                merchant = merchantMatch.groupValues[1],
                balance = balanceMatch.groupValues[1]
            )
        } else {
            null
        }
    }
}
