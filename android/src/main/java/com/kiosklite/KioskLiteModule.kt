package com.kiosklite

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = KioskLiteModule.NAME)
class KioskLiteModule(reactContext: ReactApplicationContext) :
  NativeKioskLiteSpec(reactContext) {

  override fun getName(): String = NAME

  @ReactMethod
  fun lock() {
    enableImmersiveMode()
  }

  @ReactMethod
  fun unlock() {
    disableImmersiveMode()
  }

  @ReactMethod
  fun bringToFront() {
    val activity: Activity? = currentActivity
    if (activity != null) {
      val intent = Intent(activity, activity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
      activity.startActivity(intent)
    }
  }

  private fun enableImmersiveMode() {
    currentActivity?.window?.decorView?.systemUiVisibility =
      View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
  }

  private fun disableImmersiveMode() {
    currentActivity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
  }

  companion object {
    const val NAME = "KioskLite"
  }
}