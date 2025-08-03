package com.kiosklite

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat

class KioskMonitorService : Service() {
  private val handler = Handler(Looper.getMainLooper())
  private val checkInterval = 3000L

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    startForeground(1, buildNotification())
    monitorApp()
    return START_STICKY
  }

  private fun monitorApp() {
    handler.post(object : Runnable {
      override fun run() {
        if (!isAppInForeground()) {
          val intent = Intent(this@KioskMonitorService, MainActivity::class.java)
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
          startActivity(intent)
        }
        handler.postDelayed(this, checkInterval)
      }
    })
  }

  private fun isAppInForeground(): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses
    return appProcesses?.any {
      it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
        it.processName == packageName
    } ?: false
  }

  private fun buildNotification(): Notification {
    val channelId = "kiosk_monitor"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        channelId, "Kiosk Monitor", NotificationManager.IMPORTANCE_LOW
      )
      getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
    return NotificationCompat.Builder(this, channelId)
      .setContentTitle("Kiosk mode active")
      .setSmallIcon(android.R.drawable.ic_lock_lock)
      .build()
  }

  override fun onBind(intent: Intent?): IBinder? = null
}