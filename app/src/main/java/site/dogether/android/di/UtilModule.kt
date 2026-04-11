package site.dogether.android.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import site.dogether.presentation.utils.AndroidDayChangeTracker
import site.dogether.presentation.utils.DayChangeTracker

val utilModule = module {
    single<DayChangeTracker> { AndroidDayChangeTracker(androidContext()) }
}
