package site.dogether.android.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import site.dogether.common.DefaultDispatcher
import site.dogether.presentation.screen.create_group.CreateGroupViewModel
import site.dogether.presentation.screen.error.ErrorViewModel
import site.dogether.presentation.screen.force_update.ForceUpdateViewModel
import site.dogether.presentation.screen.group_created.GroupCreatedViewModel
import site.dogether.presentation.screen.group_participated.GroupParticipatedViewModel
import site.dogether.presentation.screen.home.HomeViewModel
import site.dogether.presentation.screen.my_page.MyPageViewModel
import site.dogether.presentation.screen.my_page.screen.certification_list.CertificationListViewModel
import site.dogether.presentation.screen.my_page.screen.group_management.GroupManagementViewModel
import site.dogether.presentation.screen.my_page.screen.settings.SettingsViewModel
import site.dogether.presentation.screen.my_page.screen.statistics.StatisticsViewModel
import site.dogether.presentation.screen.on_boarding.OnBoardingViewModel
import site.dogether.presentation.screen.participate_group.ParticipateGroupViewModel
import site.dogether.presentation.screen.participation_method.ParticipationMethodViewModel
import site.dogether.presentation.screen.splash.SplashViewModel

val viewModelModule = module {
    viewModel {
        SplashViewModel(
            checkUpdateRequired = get(),
            getUserInfo = get(),
            checkParticipating = get()
        )
    }
    viewModel { ForceUpdateViewModel() }
    viewModel {
        OnBoardingViewModel(
            loginWithKakao = get(),
            storeUserInfo = get(),
            checkParticipating = get()
        )
    }
    viewModel { ParticipationMethodViewModel() }
    viewModel {
        CreateGroupViewModel(createGroup = get())
    }
    viewModel { GroupCreatedViewModel(get()) }
    viewModel { ParticipateGroupViewModel() }
    viewModel { GroupParticipatedViewModel() }
    viewModel { ErrorViewModel() }
    viewModel {
        HomeViewModel(defaultDispatcher = get(named(DefaultDispatcher)))
    }
    viewModel { MyPageViewModel() }
    viewModel { StatisticsViewModel() }
    viewModel { CertificationListViewModel() }
    viewModel { SettingsViewModel() }
    viewModel { GroupManagementViewModel() }
}