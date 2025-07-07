package com.example.calllogger

import android.content.Context
import android.content.SharedPreferences

object BlockedNumbers {

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences("blocked", Context.MODE_PRIVATE)

    fun isBlocked(context: Context, number: String): String? {
        return prefs(context).getString(number, null)
    }

    fun store(context: Context, number: String, comment: String) {
        prefs(context).edit().putString(number, comment).apply()
    }
}
