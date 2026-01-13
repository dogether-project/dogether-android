package site.dogether.android.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import site.dogether.android.MainActivity
import site.dogether.android.R
import site.dogether.domain.use_case.user.RegisterFcmTokenUseCase

class PushReceiver : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let { notification ->
            sendNotification(
                title = notification.title ?: "Dogether",
                messageBody = notification.body.orEmpty(),
                data = remoteMessage.data
            )
        } ?: run {
            // 알림이 없고 데이터만 있는 경우
            if (remoteMessage.data.isNotEmpty()) {
                val title = remoteMessage.data["title"] ?: "Dogether"
                val body = remoteMessage.data["body"].orEmpty()
                sendNotification(
                    title = title,
                    messageBody = body,
                    data = remoteMessage.data
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendFcmToken(token)
    }

    private fun sendFcmToken(token: String) {
        serviceScope.launch {
            registerFcmTokenUseCase(token)
        }
    }

    private fun sendNotification(
        title: String,
        messageBody: String,
        data: Map<String, String>
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // 딥링크나 특정 화면으로 이동하는 경우 data를 Intent에 추가
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}

enum class PushType {
    JOIN, CERTIFICATION, REVIEW;
    // TODO_CERTIFICATION_REMINDER, TODO_CERTIFICATION_REVIEW_REMINDER // - 알림 재촉하기
}

