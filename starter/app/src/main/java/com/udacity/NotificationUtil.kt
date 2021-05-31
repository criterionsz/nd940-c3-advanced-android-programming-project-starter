package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0
const val FILE_NAME = "FILE_NAME"
const val STATUS = "STATUS"
fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    fileName: String,
    status: String
) {
    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra(FILE_NAME, fileName)
    intent.putExtra(STATUS, status)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}