package site.dogether.android.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import site.dogether.data.local.DataStoreManager
import site.dogether.data.local.PreferenceKey
import site.dogether.data.remote.ApiRouteManager
import site.dogether.data.remote.ApiRoutes

val networkModule = module {
    single {
        val dataStoreManager: DataStoreManager = get()
        val apiRouteManager: ApiRouteManager = get()
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    encodeDefaults = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                url(apiRouteManager.getBaseUrl())
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                val accessToken = runBlocking {
                    dataStoreManager.loadString(PreferenceKey.USER_TOKEN).getOrElse { "" }
                }

                val path = url.encodedPath
                if (accessToken.isNotEmpty() && !path.startsWith(ApiRoutes.CHECK_UPDATE_REQUIRED) && !path.startsWith(
                        ApiRoutes.KAKAO_LOGIN
                    )
                ) {
                    header(HttpHeaders.Authorization, "Bearer $accessToken")
                }
            }

            engine {
                requestTimeout = 10000L
            }
        }
    }
}