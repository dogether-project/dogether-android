package site.dogether.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import site.dogether.common.exception.NetworkFailureException
import site.dogether.domain.model.user.UserInfo
import site.dogether.domain.repository.UserRepository
import site.dogether.domain.use_case.user.LoginWithKakaoUseCase

class LoginWithKakaoUseCaseTest : BehaviorSpec({

    val name = "두게더"
    val idToken = "test_id_token"

    val repository: UserRepository = mockk(relaxed = true)
    val useCase = LoginWithKakaoUseCase(repository)

    given("카카오 로그인 유즈케이스가 실행될 때") {

        `when`("정상적으로 로그인 정보가 주어지면") {
            val expectedUserInfo = UserInfo(
                name = name,
                accessToken = "access"
            )

            coEvery {
                repository.loginWithKakao(
                    name = name,
                    idToken = idToken
                )
            } returns Result.success(expectedUserInfo)

            then("UserInfo 를 성공적으로 반환해야 한다.") {
                val result = useCase(
                    name = name,
                    idToken = idToken
                )

                result.isSuccess shouldBe true
                result.getOrNull() shouldBe expectedUserInfo

                coVerify {
                    repository.loginWithKakao(
                        name = name,
                        idToken = idToken
                    )
                }
            }
        }

        `when`("로그인 중 예외가 발생하면") {
            val exception = NetworkFailureException("400", "로그인 실패")

            coEvery {
                repository.loginWithKakao(
                    name = name,
                    idToken = idToken
                )
            } returns Result.failure(exception)

            then("동일한 예외 메시지를 포함한 실패 결과를 반환해야 한다.") {
                val result = useCase(
                    name = name,
                    idToken = idToken
                )

                result.isFailure shouldBe true
                result.exceptionOrNull()?.message shouldBe exception.message

                coVerify {
                    repository.loginWithKakao(
                        name = name,
                        idToken = idToken
                    )
                }
            }
        }
    }
})
