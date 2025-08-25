package site.dogether.android.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import site.dogether.data.local.DataStoreManager
import site.dogether.data.local.PreferenceKey
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.ApiRoutes.BASE_URL

val networkModule = module {
    single {
        val dataStoreManager: DataStoreManager = get()
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
                url(BASE_URL)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = dataStoreManager.loadString(PreferenceKey.USER_TOKEN).getOrElse { "" }

                        if (accessToken.isNotEmpty()) {
                            BearerTokens(accessToken, "")
                        } else {
                            return@loadTokens null
                        }
                    }

                    sendWithoutRequest { request ->
                        !request.url.encodedPath.startsWith(ApiRoutes.CHECK_UPDATE_REQUIRED)
                    }
                }
            }

            engine {
                requestTimeout = 10000L
            }
        }
    }
}