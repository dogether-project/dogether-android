package site.dogether.domain.use_case.group

import site.dogether.domain.repository.GroupRepository

class StoreLastSelectedGroupIdUseCase(private val repository: GroupRepository) {
    suspend operator fun invoke(groupId: Int): Result<Unit> = repository.storeLastSelectedGroupId(groupId)
}