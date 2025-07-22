package com.example.smstransactionuploader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val smsPermissionsRequestCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermissionsButton: Button = findViewById(R.id.request_permissions_button)
        requestPermissionsButton.setOnClickListener {
            requestSmsPermissions()
        }
    }

    private fun requestSmsPermissions() {
        val receiveSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        val readSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)

        if (receiveSmsPermission != PackageManager.PERMISSION_GRANTED || readSmsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                smsPermissionsRequestCode
            )
        } else {
            Toast.makeText(this, "SMS permissions are already granted.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == smsPermissionsRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permissions granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS permissions are required to use this app.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
