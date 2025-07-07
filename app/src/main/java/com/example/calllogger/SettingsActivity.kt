package com.example.calllogger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calllogger.databinding.ActivitySettingsBinding
import okhttp3.OkHttpClient
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        binding.etApiUrl.setText(prefs.getString("api_url", ""))
        binding.etOdooUrl.setText(prefs.getString("odoo_url", ""))
        binding.etOdooDb.setText(prefs.getString("odoo_db", ""))
        binding.etOdooUser.setText(prefs.getString("odoo_user", ""))
        binding.etOdooPassword.setText(prefs.getString("odoo_password", ""))

        binding.btnSave.setOnClickListener {
            prefs.edit()
                .putString("api_url", binding.etApiUrl.text.toString())
                .putString("odoo_url", binding.etOdooUrl.text.toString())
                .putString("odoo_db", binding.etOdooDb.text.toString())
                .putString("odoo_user", binding.etOdooUser.text.toString())
                .putString("odoo_password", binding.etOdooPassword.text.toString())
                .apply()
            finish()
        }

        binding.btnTestConnection.setOnClickListener {
            Thread {
                try {
                    val config = XmlRpcClientConfigImpl()
                    config.serverURL = URL(binding.etOdooUrl.text.toString() + "/xmlrpc/2/common")
                    val client = XmlRpcClient()
                    client.setConfig(config)
                    val result = client.execute("authenticate", arrayOf(
                        binding.etOdooDb.text.toString(),
                        binding.etOdooUser.text.toString(),
                        binding.etOdooPassword.text.toString(),
                        emptyMap<String, Any>()
                    )) as Int
                    runOnUiThread {
                        binding.btnTestConnection.text = if (result > 0) "Success" else "Failed"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { binding.btnTestConnection.text = "Error" }
                }
            }.start()
        }
    }
}
