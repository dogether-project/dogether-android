package site.dogether.data

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.mockk.mockk
import site.dogether.data.repository_impl.UserRepositoryImpl


class UserRepositoryImplBehaviorTest : BehaviorSpec({

    val userIdSub = "4374801238"

    lateinit var repo: UserRepositoryImpl


    given("카카오 idToken 이 주어졌을 때") {
        val jwt = "eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyYTY2YmI4ZTk2MGYyNjVjNDUyNmY3ZGU0ZWIzMDAyMyIsInN1YiI6IjQzNzQ4MDEyMzgiLCJhdXRoX3RpbWUiOjE3NTQ5MDEyNzcsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLsp4DtmLgiLCJleHAiOjE3NTQ5NDQ0NzcsImlhdCI6MTc1NDkwMTI3N30.NMXwVhdpm8FBOOHaq1vB29eW4Vvi_g84OYV-JeFEMHICJjf8vWg4_Vki5b8xSdDaApg04Yca0KoicRK3S5KaGBcR-auVLsVrcCt54plWlbad91zPZ89jrzp7yC8phip5IhSYEobOCpvjdD_j4O53wVCiSEbX6fOnz8GcAwE9bL-EgrzJLbT8QL0Hjg8JHuhrqtoHYL4Q0UxGXd0QJxg_4rlEm4gv_EBowSDNVSm8Yz-VX1Uibgaair6FEYqjoz0TbJK89WB9KGm9csrVNbTdlVjL-957zNTRc5stqFx8MxL7HbtDSTanBLAK9QtsANxvU90l9x149rPdxbxka8a_zw"

        beforeEach {
            val httpClient = mockk<HttpClient>(relaxed = true)
            repo = UserRepositoryImpl(httpClient)
        }

        `when`("extractUserIdFromJwt를 호출하면") {
            then("JWT의 sub를 반환한다") {
                val result = repo.extractUserIdFromJwt(jwt)
                result shouldBe userIdSub
            }
        }
    }
})