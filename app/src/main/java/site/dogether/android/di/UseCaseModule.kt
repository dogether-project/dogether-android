package site.dogether.android.di

import org.koin.dsl.module
import site.dogether.domain.use_case.app_info.CheckUpdateRequiredUseCase
import site.dogether.domain.use_case.user.CheckParticipatingUseCase
import site.dogether.domain.use_case.user.GetUserInfoUseCase
import site.dogether.domain.use_case.user.LoginWithKakaoUseCase
import site.dogether.domain.use_case.user.StoreUserInfoUseCase

val useCaseModule = module {
    factory { LoginWithKakaoUseCase(repository = get()) }
    factory { CheckUpdateRequiredUseCase(repository = get()) }
    factory { GetUserInfoUseCase(repository = get()) }
    factory { CheckParticipatingUseCase(repository = get()) }
    factory { StoreUserInfoUseCase(repository = get()) }
}