package site.dogether.android.di

import org.koin.dsl.module
import site.dogether.data.remote.ApiRouteManager
import site.dogether.data.remote.ApiRouteManagerImpl

val apiRouteModule = module {
    single<ApiRouteManager> { ApiRouteManagerImpl }
}

