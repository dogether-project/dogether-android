package site.dogether.android

import android.app.Application
import com.chottulink.lib.ChottuLink
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import site.dogether.android.di.appModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        ChottuLink.init(this, "c_app_O6DbHOINzQfp7Cu6a1Bmk3PYBGsyOwAj")
    }
}