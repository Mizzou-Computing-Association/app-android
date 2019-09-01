package tigerhacks.android.tigerhacksapp.service.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest
import com.amazonaws.services.sns.model.SubscribeRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tigerhacks.android.tigerhacksapp.BuildConfig
import tigerhacks.android.tigerhacksapp.R


private const val TAG = "THMessagingService"
private const val CHANNEL_ID = "tigerhacks.android.tigerhacksapp.TigerHacks"
private const val CHANNEL_NAME = "TigerHacks"

class TigerHacksMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        val message = remoteMessage.data.values.toTypedArray()[0]
        createChannelsIfNeeded()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(builder.hashCode(), builder.build())
        }
    }

    override fun onNewToken(token: String) {
        if (BuildConfig.DEBUG) return
        val credentials = CognitoCachingCredentialsProvider(
            applicationContext,
            SNSSecrets.identityPoolId,
            Regions.US_EAST_1
        )

        val client = AmazonSNSClient(credentials)

        val endpointArnRequest = CreatePlatformEndpointRequest()
            .withPlatformApplicationArn(SNSSecrets.platformArn)
            .withToken(token)

        val endpointArnResult = client.createPlatformEndpoint(endpointArnRequest)
        if (endpointArnResult == null) {
            Log.d(TAG, "Error creating sns platform endpoint!")
            return
        }

        val endpointArn = endpointArnResult.endpointArn

        val subscribeRequest = SubscribeRequest()
            .withProtocol("application")
            .withEndpoint(endpointArn)
            .withTopicArn(SNSSecrets.topicArn)

        val subscribeResult = client.subscribe(subscribeRequest)
        if (subscribeResult == null) {
            Log.d(TAG, "Error subscribing to sns topic")
            return
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


