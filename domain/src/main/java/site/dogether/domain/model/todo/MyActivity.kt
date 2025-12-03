package site.dogether.domain.model.todo

import site.dogether.domain.model.DomainModel
import site.dogether.domain.model.PageInfo

data class MyActivity(
    val dailyTodoStats: DailyTodoStats = DailyTodoStats(),
    val certificationsGroupedByTodoCompletedAt: List<GroupedCertification> = listOf(),
    val certificationsGroupedByGroupCreatedAt: List<GroupedCertification> = listOf(),
    val pageInfo: PageInfo = PageInfo()
) : DomainModel

data class DailyTodoStats(
    val totalCertificatedCount: Int = 0,
    val totalApprovedCount: Int = 0,
    val totalRejectedCount: Int = 0
) : DomainModel

data class GroupedCertification(
    val createdAt: String = "",
    val dayOfWeek: String = "",
    val groupName: String = "",
    val certificationInfo: List<Todo> = emptyList()
) : DomainModel