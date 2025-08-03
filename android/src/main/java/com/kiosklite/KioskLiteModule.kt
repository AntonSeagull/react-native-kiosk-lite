package com.kiosklite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.util.Log
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = KioskLiteModule.NAME)
class KioskLiteModule(reactContext: ReactApplicationContext) :
  NativeKioskLiteSpec(reactContext) {

  override fun getName(): String = NAME

  private var overlayView: View? = null

  @ReactMethod
  override fun lock() {
    enableImmersiveMode()
  }

  @ReactMethod
  override fun unlock() {
    disableImmersiveMode()
  }

@ReactMethod
override fun bringToFront() {
  val context = reactApplicationContext
  val packageManager = context.packageManager
  val intent = packageManager.getLaunchIntentForPackage(context.packageName)

  if (intent != null) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP)
    context.startActivity(intent)
  }
}

  @ReactMethod
 override fun requestOverlayPermission() {
    val context = reactApplicationContext
    if (!Settings.canDrawOverlays(context)) {
      val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:" + context.packageName)
      )
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(intent)
    }
  }

  @ReactMethod
 override fun hasOverlayPermission(promise: Promise) {
    val granted = Settings.canDrawOverlays(reactApplicationContext)
    promise.resolve(granted)
  }

  @ReactMethod
override fun startKioskMonitorService() {
  val context = reactApplicationContext
  val intent = Intent(context, KioskMonitorService::class.java)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    context.startForegroundService(intent)
  } else {
    context.startService(intent)
  }
}

@ReactMethod
override fun stopKioskMonitorService() {
  val context = reactApplicationContext
  val intent = Intent(context, KioskMonitorService::class.java)
  context.stopService(intent)
}
  @ReactMethod
    override fun createOverlay() {
        if (!Settings.canDrawOverlays(reactApplicationContext)) return
        if (overlayView != null) return

        val overlay = View(reactApplicationContext)
       // overlay.setBackgroundColor(Color.parseColor("#88FF0000"))
        overlay.setBackgroundColor(Color.TRANSPARENT)
        overlay.setOnTouchListener { _, event ->
            Log.d("Overlay", "Touch event: ${event.action}")
            true
        }
        overlay.isClickable = true
        overlay.isFocusable = true

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            50, 
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP

        val wm = reactApplicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.addView(overlay, params)
        overlayView = overlay
    }

  @ReactMethod
  override fun removeOverlay() {
    overlayView?.let {
      val wm = reactApplicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
      wm.removeView(it)
      overlayView = null
    }
  }

 private fun enableImmersiveMode() {
  currentActivity?.runOnUiThread {
    currentActivity?.window?.decorView?.systemUiVisibility =
      View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
  }
}

 private fun disableImmersiveMode() {
  currentActivity?.runOnUiThread {
    currentActivity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
  }
}

  companion object {
    const val NAME = "KioskLite"
  }
}