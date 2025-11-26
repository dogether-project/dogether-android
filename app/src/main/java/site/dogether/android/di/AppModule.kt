package site.dogether.android.di

val appModule = listOf(
    viewModelModule,
    useCaseModule,
    repositoryModule,
    localDataSourceModule,
    apiRouteModule,
    networkModule,
    dispatcherModule
)