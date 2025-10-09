package site.dogether.android.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import site.dogether.data.repository_impl.AppInfoRepositoryImpl
import site.dogether.data.repository_impl.GroupRepositoryImpl
import site.dogether.data.repository_impl.TodoRepositoryImpl
import site.dogether.data.repository_impl.UserRepositoryImpl
import site.dogether.domain.repository.AppInfoRepository
import site.dogether.domain.repository.GroupRepository
import site.dogether.domain.repository.TodoRepository
import site.dogether.domain.repository.UserRepository

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(
            dataStoreManager = get(),
            httpClient = get()
        )
    }
    single<AppInfoRepository> { AppInfoRepositoryImpl(httpClient = get()) }
    single<GroupRepository> { GroupRepositoryImpl(httpClient = get()) }
    single<TodoRepository> {
        TodoRepositoryImpl(
            context = get(),
            httpClient = get()
        )
    }
}
