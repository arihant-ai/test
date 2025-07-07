package com.example.calllogger

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.calllogger.databinding.DialogCallInfoBinding

class CallPopupActivity : AppCompatActivity() {

    private lateinit var binding: DialogCallInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCallInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getStringExtra(EXTRA_NUMBER) ?: ""
        binding.tvNumber.text = number

        binding.btnSubmit.setOnClickListener {
            val prefs = getSharedPreferences("settings", MODE_PRIVATE)
            val apiUrl = prefs.getString("api_url", "")
            val data = mapOf(
                "number" to number,
                "case_no" to binding.etCaseNo.text.toString(),
                "comment" to binding.etComment.text.toString()
            )
            if (binding.cbBlock.isChecked) {
                BlockedNumbers.store(this, number, binding.etComment.text.toString())
            } else {
                ApiClient.send(apiUrl!!, data)
            }
            finish()
        }
    }

    companion object {
        private const val EXTRA_NUMBER = "number"

        fun enqueue(context: Context, number: String) {
            val intent = Intent(context, CallPopupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(EXTRA_NUMBER, number)
            context.startActivity(intent)
        }
    }
}
