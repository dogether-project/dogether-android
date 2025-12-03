package site.dogether.data.remote.model.res.todo

import kotlinx.serialization.Serializable
import site.dogether.common.utils.DateTimeUtils.DATE_FORMAT_FULL_YEAR
import site.dogether.common.utils.DateTimeUtils.dayOfWeek
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.data.remote.model.res.PageInfoRes
import site.dogether.data.remote.model.res.PageInfoResMapper.toDomainModel
import site.dogether.data.remote.model.res.todo.DailyTodoStatsResMapper.toDomainModel
import site.dogether.data.remote.model.res.todo.GroupedCertificationResMapper.toDomainModel
import site.dogether.data.remote.model.res.todo.TodoResMapper.toDomainModel
import site.dogether.domain.model.todo.DailyTodoStats
import site.dogether.domain.model.todo.GroupedCertification
import site.dogether.domain.model.todo.MyActivity
import java.time.LocalDate

@Serializable
data class MyActivityRes(
    val dailyTodoStats: DailyTodoStatsRes,
    val certificationsGroupedByTodoCompletedAt: List<GroupedCertificationsRes>? = listOf(),
    val certificationsGroupedByGroupCreatedAt: List<GroupedCertificationsRes>? = listOf(),
    val pageInfo: PageInfoRes
) : DataModel

object MyActivityResMapper : DataMapper<MyActivityRes, MyActivity> {
    override fun MyActivityRes.toDomainModel(): MyActivity {
        return MyActivity(
            dailyTodoStats = dailyTodoStats.toDomainModel(),
            certificationsGroupedByTodoCompletedAt = certificationsGroupedByTodoCompletedAt?.map { it.toDomainModel() } ?: emptyList(),
            certificationsGroupedByGroupCreatedAt = certificationsGroupedByGroupCreatedAt?.map { it.toDomainModel() } ?: emptyList(),
            pageInfo = pageInfo.toDomainModel()
        )
    }
}

@Serializable
data class DailyTodoStatsRes(
    val totalCertificatedCount: Int,
    val totalApprovedCount: Int,
    val totalRejectedCount: Int
) : DataModel

object DailyTodoStatsResMapper : DataMapper<DailyTodoStatsRes, DailyTodoStats> {
    override fun DailyTodoStatsRes.toDomainModel(): DailyTodoStats {
        return DailyTodoStats(
            totalCertificatedCount = totalCertificatedCount,
            totalApprovedCount = totalApprovedCount,
            totalRejectedCount = totalRejectedCount
        )
    }
}

@Serializable
data class GroupedCertificationsRes(
    val createdAt: String? = "",
    val groupName: String? = "",
    val certificationInfo: List<TodoRes>
) : DataModel


object GroupedCertificationResMapper : DataMapper<GroupedCertificationsRes, GroupedCertification> {
    override fun GroupedCertificationsRes.toDomainModel(): GroupedCertification {
        val dayOfWeekString = if (!createdAt.isNullOrEmpty()) {
            try {
                LocalDate.parse(createdAt, DATE_FORMAT_FULL_YEAR).dayOfWeek()
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }

        return GroupedCertification(
            createdAt = createdAt.orEmpty(),
            dayOfWeek = dayOfWeekString,
            groupName = groupName.orEmpty(),
            certificationInfo = certificationInfo.map { it.toDomainModel() }
        )
    }
}