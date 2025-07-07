package com.example.calllogger

import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView

object CallInfoOverlay {
    private var view: View? = null
    private var windowManager: WindowManager? = null

    fun show(context: Context, number: String, info: String) {
        if (view != null) return
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        view!!.findViewById<TextView>(android.R.id.text1).text = number
        view!!.findViewById<TextView>(android.R.id.text2).text = info
        windowManager!!.addView(view, params)
    }

    fun hide() {
        view?.let {
            windowManager?.removeView(it)
            view = null
            windowManager = null
        }
    }
}
