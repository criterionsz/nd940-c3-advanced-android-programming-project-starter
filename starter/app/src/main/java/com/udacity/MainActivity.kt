package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_description)
        )
        custom_button.setOnClickListener {
            val checkedId = radioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                custom_button.setStateButton(ButtonState.Loading)
                when (checkedId) {
                    R.id.glide_radio_button -> {
                        fileName = getString(R.string.content_glide_title)
                        download(URL_GLIDE)
                    }
                    R.id.load_app_radio_button -> {
                        fileName = getString(R.string.content_loadapp_title)
                        download(URL)
                    }
                    R.id.retrofit_radio_button -> {
                        fileName = getString(R.string.content_retrofit_title)
                        download(URL_RETROFIT)
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.toast_title_select_option),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.setStateButton(ButtonState.Completed)
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(DownloadManager.Query())
                if (cursor.moveToFirst()) {
                    val title =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    notificationManager = ContextCompat.getSystemService(
                        context!!,
                        NotificationManager::class.java
                    ) as NotificationManager
                    val statusStr = if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        "Success"
                    } else {
                        "Failed"
                    }
                    notificationManager.cancelAll()
                    notificationManager.sendNotification(
                        getString(R.string.notification_description),
                        applicationContext,
                        title,
                        statusStr
                    )
                }
            }
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { setShowBadge(false) }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                channelName

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RETROFIT =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
