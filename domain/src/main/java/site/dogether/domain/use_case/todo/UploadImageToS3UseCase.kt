package site.dogether.domain.use_case.todo

import site.dogether.domain.repository.TodoRepository

class UploadImageToS3UseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(
        presignedUrl: String,
        imageUri: String
    ): Result<Unit> {
        return repository.uploadImageToS3(presignedUrl, imageUri)
    }
}
