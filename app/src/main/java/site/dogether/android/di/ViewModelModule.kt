package site.dogether.android.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import site.dogether.presentation.screen.create_group.CreateGroupViewModel
import site.dogether.presentation.screen.force_update.ForceUpdateViewModel
import site.dogether.presentation.screen.group_created.GroupCreatedViewModel
import site.dogether.presentation.screen.on_boarding.OnBoardingViewModel
import site.dogether.presentation.screen.participation_method.ParticipationMethodViewModel
import site.dogether.presentation.screen.splash.SplashViewModel

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { ForceUpdateViewModel() }
    viewModel { OnBoardingViewModel() }
    viewModel { ParticipationMethodViewModel() }
    viewModel { CreateGroupViewModel() }
    viewModel { GroupCreatedViewModel() }
}