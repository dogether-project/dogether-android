package site.dogether.data

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.utils.safeApiCall
import site.dogether.data.utils.safeGet
import site.dogether.data.utils.safePost
import site.dogether.domain.model.DomainModel

class RemoteUtilsTest : BehaviorSpec({

    @Serializable
    data class TestDataModel(
        val id: Int,
        val name: String,
    ) : DataModel

    data class TestDomainModel(
        val id: Int,
        val name: String,
    ) : DomainModel

    @Serializable
    data class TestRequest(
        val data: String,
    )

    val testMapper = object : DataMapper<TestDataModel, TestDomainModel> {
        override fun TestDataModel.toDomainModel(): TestDomainModel {
            return TestDomainModel(
                id = id,
                name = name
            )
        }
    }

    given("DataModel 객체") {
        val testData = TestDataModel(
            id = 1,
            name = "두게더"
        )

        `when`("toDomainModel 메서드를 호출할 때") {
            val result = testMapper.run { testData.toDomainModel() }

            then("DomainModel 을 반환해야 한다.") {
                (result is DomainModel) shouldBe true
            }
        }
    }

    given("safeApiCall 메서드") {

        `when`("성공적인 응답을 받았을 때") {
            val mockEngine = MockEngine { _ ->
                respond(
                    content = """
                        {
                            "code": 200,
                            "message": "API 호출 성공",
                            "data": {
                                "id": 1,
                                "name": "두게더"
                            }
                        }
                    """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
            }

            val httpClient = HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            then("Result.success 를 반환해야 한다.") {
                val result = safeApiCall<TestDataModel> {
                    httpClient.get("https://fake.api.com/test")
                }

                result.isSuccess shouldBe true
            }
        }

        `when`("실패한 응답을 받았을 때") {
            val mockEngine = MockEngine { _ ->
                respond(
                    content = """
                        {
                            "code": 400,
                            "message": "API 호출 실패",
                            "data": null
                        }
                    """.trimIndent(),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
            }

            val httpClient = HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            then("Result.failure 를 반환해야 한다.") {
                val result = safeApiCall<TestDataModel> {
                    httpClient.get("https://fake.api.com/test")
                }

                result.isFailure shouldBe true
            }
        }

        `when`("safePost 성공 응답 시") {
            val mockEngine = MockEngine { _ ->
                respond(
                    content = """
                         {
                            "code": 200,
                            "message": "API 호출 성공",
                            "data": {
                                "id": 1,
                                "name": "두게더"
                            }
                        }
                    """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
            }

            val httpClient = HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            then("DomainModel 을 반환해야 한다.") {
                val result = httpClient.safePost(
                    apiRoute = "",
                    body = TestRequest("두게더"),
                    mapper = testMapper
                )

                result.onSuccess { data ->
                    (data is DomainModel) shouldBe true
                }
            }
        }

        `when`("safeGet 실패 응답 시") {
            val mockEngine = MockEngine { _ ->
                respond(
                    content = """
                        {
                            "code": 400,
                            "message": "API 호출 실패",
                            "data": null
                        }
                    """.trimIndent(),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
            }

            val httpClient = HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            then("Result.failure 를 반환해야 한다.") {
                val result = httpClient.safeGet(
                    apiRoute = "",
                    params = mapOf(),
                    mapper = testMapper
                )

                result.isFailure shouldBe true
            }
        }
    }
})
