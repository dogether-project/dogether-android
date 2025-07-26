package site.dogether.android.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import site.dogether.common.DefaultDispatcher
import site.dogether.common.IoDispatcher
import site.dogether.common.MainDispatcher

val dispatcherModule = module {
    single<CoroutineDispatcher>(named(IoDispatcher)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DefaultDispatcher)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(MainDispatcher)) { Dispatchers.Main.immediate }
}