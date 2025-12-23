package site.dogether.data.repository_impl

import android.content.Context
import android.net.Uri
import io.ktor.client.HttpClient
import site.dogether.data.remote.ApiRoutes
import site.dogether.data.remote.model.req.todo.CreateTodoReq
import site.dogether.data.remote.model.req.todo.certificate.CertificateTodoReq
import site.dogether.data.remote.model.req.todo.certificate.PresignedUrlReq
import site.dogether.data.remote.model.res.todo.GetMyTodoSpecificDateResMapper
import site.dogether.data.remote.model.res.todo.MemberTodoHistoryResMapper
import site.dogether.data.remote.model.res.todo.MyActivityResMapper
import site.dogether.data.remote.model.res.todo.TodosResMapper
import site.dogether.data.remote.model.res.todo.certificate.PresignedUrlResMapper
import site.dogether.data.utils.safeGet
import site.dogether.data.utils.safeGetWithoutRes
import site.dogether.data.utils.safePost
import site.dogether.data.utils.safePostWithoutRes
import site.dogether.data.utils.safePutToS3
import site.dogether.domain.model.certificate.PresignedUrlData
import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo
import site.dogether.domain.model.todo.MemberTodoHistory
import site.dogether.domain.model.todo.MyActivity
import site.dogether.domain.model.todo.Todo
import site.dogether.domain.repository.TodoRepository

class TodoRepositoryImpl(
    private val context: Context,
    private val httpClient: HttpClient,
) : TodoRepository {
    override suspend fun getMyTodoSpecificDate(
        groupId: Int,
        date: String,
    ): Result<GetMyTodoSpecificDateInfo> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.getMyTodoSpecificDate(groupId),
            params = mapOf("date" to date.replace(".", "-")),
            mapper = GetMyTodoSpecificDateResMapper
        )
    }

    override suspend fun createMyTodo(groupId: Int, todos: List<String>): Result<Unit> {
        return httpClient.safePostWithoutRes(
            apiRoute = ApiRoutes.createMyTodos(groupId = groupId),
            body = CreateTodoReq(todos = todos),
        )
    }

    /**
     * S3 Presigned Url 요청
     * 현재는 갯수에 맞춰 이미지만 업로드 하도록 되어있는데, 추후 확장시 수정 필요
     * */
    override suspend fun getPresignedUrls(
        dailyTodoId: Int,
        fileCount: Int,
    ): Result<PresignedUrlData> {
        return httpClient.safePost(
            apiRoute = ApiRoutes.GET_PRESIGNED_URLS,
            body = PresignedUrlReq(
                dailyTodoId = dailyTodoId,
                uploadFileTypes = List(fileCount) { "IMAGE" }
            ),
            mapper = PresignedUrlResMapper
        )
    }

    /**
     * S3 Presigned Url로 이미지 업로드
     * */
    override suspend fun uploadImageToS3(
        presignedUrl: String,
        imageUri: String,
    ): Result<Unit> {
        return safePutToS3(
            presignedUrl = presignedUrl,
            imageUri = Uri.parse(imageUri),
            context = context
        )
    }

    override suspend fun certifyTodo(
        dailyTodoId: Int,
        content: String,
        mediaUrl: String,
    ): Result<Unit> {
        return httpClient.safePostWithoutRes(
            apiRoute = ApiRoutes.certifyTodo(dailyTodoId),
            body = CertificateTodoReq(
                content = content,
                mediaUrl = mediaUrl
            )
        )
    }

    override suspend fun getMyTodosByDate(
        groupId: Int,
        date: String,
    ): Result<List<Todo>> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.getMyTodosByDate(groupId),
            params = mapOf("date" to date),
            mapper = TodosResMapper
        ).map { it.todos }
    }

    override suspend fun getMyActivity(
        sortBy: String,
        status: String?,
        page: Int
    ): Result<MyActivity> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.MY_ACTIVITY,
            params = mapOf(
                "sortBy" to sortBy,
                "status" to status.orEmpty(),
                "page" to page
            ),
            mapper = MyActivityResMapper
        )
    }

    override suspend fun readTodo(todoId: Long): Result<Unit> {
        return httpClient.safeGetWithoutRes(ApiRoutes.readTodo(todoId))
    }

    override suspend fun getMemberTodoHistory(
        groupId: Int,
        memberId: Int,
    ): Result<MemberTodoHistory> {
        return httpClient.safeGet(
            apiRoute = ApiRoutes.getMemberTodoHistory(groupId, memberId),
            params = emptyMap(),
            mapper = MemberTodoHistoryResMapper
        )
    }
}