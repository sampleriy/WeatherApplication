package com.example.weatherapplication.ui.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.WindowManager

fun applyTheme(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = Color.parseColor("#2196F3")
    }
}