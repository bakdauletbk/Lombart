package kz.pillikan.lombart.common.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kz.pillikan.lombart.R
import java.util.*

class SertFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification!!.title?.let {
            remoteMessage.notification!!
                .body?.let { it1 ->
                    showNotification(
                        remoteMessage.data, it, it1
                    )
                }
        }
    }
    private fun showNotification(params: Map<String, String>, title: String, body: String) {
        val temp = params.toString()
        val resultPendingIntent: PendingIntent? = null

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "PACKAGE"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotifficationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotifficationChannel.description = "Client Channel"
            mNotifficationChannel.enableLights(true)
            mNotifficationChannel.lightColor = Color.BLUE
            notificationManager.createNotificationChannel(mNotifficationChannel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_logo_lombart)
        if (resultPendingIntent != null) {
            notificationBuilder.setContentIntent(resultPendingIntent)
        }
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }
}