package site.dogether.domain.repository

import site.dogether.domain.model.certificate.PresignedUrlData
import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo

interface TodoRepository {
    suspend fun getMyTodoSpecificDate(
        groupId: Int,
        date: String
    ): Result<GetMyTodoSpecificDateInfo>

    suspend fun createMyTodo(groupId: Int, todos: List<String>): Result<Unit>

    /**
     * S3 Presigned URL 발급
     * @param dailyTodoId 인증할 데일리 투두 ID
     * @param fileCount 업로드할 파일 개수
     * @return Presigned URL 데이터
     */
    suspend fun getPresignedUrls(dailyTodoId: Int, fileCount: Int): Result<PresignedUrlData>

    /**
     * S3에 이미지 업로드
     * @param presignedUrl S3 Presigned URL
     * @param imageUri 업로드할 이미지 URI (String 형태)
     * @return 업로드 성공 여부
     */
    suspend fun uploadImageToS3(presignedUrl: String, imageUri: String): Result<Unit>

    /**
     * 데일리 투두 인증
     * @param dailyTodoId 인증할 데일리 투두 ID
     * @param content 인증 본문 (1-40자)
     * @param mediaUrl 업로드된 S3 URL
     */
    suspend fun certifyTodo(
        dailyTodoId: Int,
        content: String,
        mediaUrl: String
    ): Result<Unit>
}