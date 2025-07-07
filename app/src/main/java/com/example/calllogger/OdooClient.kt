package com.example.calllogger

import android.content.Context
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

object OdooClient {
    fun fetchCaseInfo(context: Context, number: String): Map<String, Any>? {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val url = prefs.getString("odoo_url", null) ?: return null
        val db = prefs.getString("odoo_db", null) ?: return null
        val user = prefs.getString("odoo_user", null) ?: return null
        val password = prefs.getString("odoo_password", null) ?: return null
        return try {
            val config = XmlRpcClientConfigImpl()
            config.serverURL = URL("$url/xmlrpc/2/object")
            val client = XmlRpcClient()
            client.setConfig(config)
            val ids = client.execute("execute_kw", arrayOf(
                db, 0, password,
                "res.partner", "search",
                listOf(listOf(listOf("phone", "=", number)))
            )) as Array<Int>
            if (ids.isEmpty()) return null
            val records = client.execute("execute_kw", arrayOf(
                db, 0, password,
                "res.partner", "read",
                ids, listOf("comment", "case_no")
            )) as Array<Map<String, Any>>
            records.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
