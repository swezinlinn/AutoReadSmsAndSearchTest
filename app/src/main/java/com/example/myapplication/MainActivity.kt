package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.ArrayList
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() , EasyPermissions.PermissionCallbacks,SmsMessageReceiver.OTPReceiveListener{
    var smsReceiver = SmsMessageReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestSmsReadPermission()
        txt_hello.setOnClickListener {
            val intent = Intent(this,SearchableActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startSMSListener()
        val signatureHelper = AppSignatureHashHelper(this)
        val appSignatures: ArrayList<String>? = signatureHelper.getAppSignatures()
        Log.d("hash key-->","-----"+appSignatures?.get(0))
        Toast.makeText(this,appSignatures?.get(0), Toast.LENGTH_LONG).show()
    }

    fun requestSmsReadPermission(){
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(
                this,
                22,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
            )
                .setRationale("SMS permission request")
                .setPositiveButtonText("Ok")
                .setNegativeButtonText("Cancel")
                .build()
        )
    }


    private fun startSMSListener(){
        try {
            smsReceiver.setOTPListener(this)

            val intentFilter = IntentFilter()
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
            this.registerReceiver(smsReceiver, intentFilter)

            val client = SmsRetriever.getClient(this)

            val task = client.startSmsRetriever()
            task.addOnSuccessListener {

            }

            task.addOnFailureListener {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOTPReceived(otp: String) {
        val otpDigit = getOtpDigit(otp)
        txv_hello.text = otpDigit
        Log.d("otp digit--->,",otpDigit)
    }

    fun getOtpDigit(msg: String): String {
        val pattern = Pattern.compile("(\\d{6})")
        val matcher = pattern.matcher(msg)
        var otp = ""
        if (matcher.find()) {
            otp = matcher.group(0)
        }
        return otp
    }

    override fun onOTPTimeOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOTPReceivedError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        if (smsReceiver != null) {
            this.unregisterReceiver(smsReceiver)
        }
    }
}
