package site.dogether.android.di

import org.koin.dsl.module
import site.dogether.data.repository_impl.AppInfoRepositoryImpl
import site.dogether.data.repository_impl.UserRepositoryImpl
import site.dogether.domain.repository.AppInfoRepository
import site.dogether.domain.repository.UserRepository

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(httpClient = get()) }
    single<AppInfoRepository> { AppInfoRepositoryImpl(httpClient = get()) }
}
