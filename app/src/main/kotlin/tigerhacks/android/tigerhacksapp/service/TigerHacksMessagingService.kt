package tigerhacks.android.tigerhacksapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tigerhacks.android.tigerhacksapp.R

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
private const val CHANNEL_ID = "tigerhacks.android.tigerhacksapp.TigerHacks"
private const val CHANNEL_NAME = "TigerHacks"

class TigerHacksMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val message = remoteMessage.notification?.body ?: ""
        createChannelsIfNeeded()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(builder.hashCode(), builder.build())
        }
    }

    private var createdChannels = false

    private fun createChannelsIfNeeded() {
        if (createdChannels) return
        createdChannels = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = Color.argb(100, 249, 154, 58)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                ?.createNotificationChannel(channel)
        }
    }
}


