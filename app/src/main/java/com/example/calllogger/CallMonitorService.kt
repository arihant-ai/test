package com.example.calllogger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class CallMonitorService : Service() {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var listener: PhoneStateListener
    private var lastNumber: String? = null

    override fun onCreate() {
        super.onCreate()
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        listener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        lastNumber = phoneNumber
                        val comment = BlockedNumbers.isBlocked(applicationContext, phoneNumber ?: "")
                        if (comment != null) {
                            CallInfoOverlay.show(applicationContext, phoneNumber!!, comment)
                        } else {
                            val info = OdooClient.fetchCaseInfo(applicationContext, phoneNumber ?: "")
                            info?.let {
                                CallInfoOverlay.show(applicationContext, phoneNumber!!, "Case: ${'$'}{it["case_no"]} ${'$'}{it["comment"]}")
                            }
                        }
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        lastNumber?.let { number ->
                            if (BlockedNumbers.isBlocked(applicationContext, number) == null) {
                                CallPopupActivity.enqueue(applicationContext, number)
                            }
                        }
                        lastNumber = null
                        CallInfoOverlay.hide()
                    }
                }
            }
        }
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "call_monitor"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Call Monitor", NotificationManager.IMPORTANCE_LOW)
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("Call Monitor")
            .setSmallIcon(android.R.drawable.stat_sys_phone_call)
            .build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
        super.onDestroy()
    }
}
