package site.dogether.android.di

import org.koin.dsl.module
import site.dogether.domain.use_case.app_info.CheckUpdateRequiredUseCase
import site.dogether.domain.use_case.user.LoginWithKakaoUseCase

val useCaseModule = module {
    factory { LoginWithKakaoUseCase(repository = get()) }
    factory { CheckUpdateRequiredUseCase(repository = get()) }
}