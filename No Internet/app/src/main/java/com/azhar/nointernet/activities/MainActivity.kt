package com.azhar.nointernet.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.azhar.nointernet.R
import com.azhar.nointernet.adapter.MainAdapter
import com.azhar.nointernet.model.ModelMain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_no_connection.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var mainAdapter: MainAdapter? = null
    var modelMain: MutableList<ModelMain> = ArrayList()
    var mProgress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mProgress = ProgressDialog(this)
        mProgress?.setTitle("Mohon tunggu")
        mProgress?.setCancelable(false)
        mProgress?.setMessage("Sedang menampilkan data")

        rvListMain.setHasFixedSize(true)
        rvListMain.setLayoutManager(LinearLayoutManager(this))

        layoutConnection.visibility = View.GONE
        iconConnection.setAnimation(R.raw.no_internet_connection)

        //Show No Connection layout if No Internet
        if (isNetworkStatusAvialable(applicationContext)) {
            //get Data
            loadJSON()
        } else {
            rvListMain.setVisibility(View.GONE)
            layoutConnection.visibility = View.VISIBLE
        }

        btnTryAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

        btnSetNetwork.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }

    }

    private fun loadJSON() {
        mProgress?.show()
        AndroidNetworking.get("https://api.learn2crack.com/android/jsonandroid/")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            mProgress?.dismiss()
                            val jsonArray = response.getJSONArray("android")
                            for (i in 0 until jsonArray.length()) {
                                val temp = jsonArray.getJSONObject(i)
                                val dataApi = ModelMain()
                                dataApi.strName = temp.getString("name")
                                dataApi.strVersion = temp.getString("ver")
                                dataApi.strApi = temp.getString("api")
                                modelMain.add(dataApi)
                            }
                            mainAdapter = MainAdapter(modelMain)
                            rvListMain.adapter = mainAdapter
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this@MainActivity, "Gagal menampilkan data!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        mProgress?.dismiss()
                        Toast.makeText(this@MainActivity, "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    companion object {
        fun isNetworkStatusAvialable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }

}