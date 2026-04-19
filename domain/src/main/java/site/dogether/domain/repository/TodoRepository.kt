package site.dogether.domain.repository

import site.dogether.domain.model.certificate.PresignedUrlData
import site.dogether.domain.model.todo.GetMyTodoSpecificDateInfo
import site.dogether.domain.model.todo.MemberTodoHistory
import site.dogether.domain.model.todo.MyActivity
import site.dogether.domain.model.todo.PendingReviewCertifications
import site.dogether.domain.model.todo.Todo

interface TodoRepository {
    suspend fun getMyTodoSpecificDate(
        groupId: Int,
        date: String,
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
        mediaUrl: String,
    ): Result<Unit>

    /**
     * 데일리 투두 전체 조회
     * @param groupId 조회할 그룹 ID
     * @param date 조회할 투두 생성일 (yyyy-MM-dd)
     */
    suspend fun getMyTodosByDate(
        groupId: Int,
        date: String,
    ): Result<List<Todo>>

    /**
     * 활동 통계 및 작성한 인증 목록 전체 조회
     * @param sortBy 정렬 방식
     * @param status 데일리 투두 상태
     * @page page
     */
    suspend fun getMyActivity(
        sortBy: String,
        status: String?,
        page: Int
    ): Result<MyActivity>

    /**
     * 특정 투두 히스토리 읽음 처리
     * @param todoId 읽은 투두 ID
     */
    suspend fun readTodo(todoId: Long): Result<Unit>

    /**
     * 멤버의 투두 히스토리 조회
     * @param groupId 챌린지 그룹 ID
     * @param memberId 조회할 챌린지 그룹 멤버 ID
     */
    suspend fun getMemberTodoHistory(
        groupId: Int,
        memberId: Int,
    ): Result<MemberTodoHistory>

    /**
     * 투두 검사 (인정/노인정)
     * @param todoId 검사할 투두 ID
     * @param isApprove 인정 여부 (true: 인정, false: 노인정)
     * @param feedback 노인정 사유 (노인정인 경우 필수)
     */
    suspend fun reviewTodo(
        todoId: Int,
        isApprove: Boolean,
        feedback: String = "",
    ): Result<Unit>

    /**
     * 검사 대기 중인 인증 목록 조회
     * @return 검사 대기 중인 인증 목록
     */
    suspend fun getPendingReviewCertifications(): Result<PendingReviewCertifications>

    /**
     * 투두 리마인드 (재촉하기)
     * @param todoId 대상 투두 ID
     * @param reminderType 리마인드 타입 ("CERTIFICATION" or "REVIEW")
     */
    suspend fun remindTodo(
        todoId: Long,
        reminderType: String
    ): Result<Unit>
}