package site.dogether.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.chottulink.lib.ChottuLink
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import site.dogether.android.di.appModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Firebase 초기화 (auto_init_enabled가 false이므로 수동 초기화)
        FirebaseApp.initializeApp(this)
        
        // Notification Channel 생성
        createNotificationChannel()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        ChottuLink.init(this, "c_app_O6DbHOINzQfp7Cu6a1Bmk3PYBGsyOwAj")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val channelDescription = getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}