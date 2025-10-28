package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

/**
 * S3 Presigned URL 발급 UseCase
 */
class GetPresignedUrlsUseCase(
    private val repository: TodoRepository
) {

    /**
     * S3 Presigned URL 발급
     * @param dailyTodoId 인증할 데일리 투두 ID
     * @param fileCount 업로드할 파일 개수
     * @return Presigned URL 데이터
     */
    suspend operator fun invoke(
        dailyTodoId: Int,
        fileCount: Int = 1
    ): Result<List<String>> {
        return repository.getPresignedUrls(dailyTodoId, fileCount).map { data ->
            data.presignedUrls
        }
    }
}