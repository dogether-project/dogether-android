package site.dogether.data.remote.model.res

import kotlinx.serialization.Serializable
import site.dogether.data.model.DataMapper
import site.dogether.data.model.DataModel
import site.dogether.domain.model.PageInfo

@Serializable
data class PageInfoRes(
    val totalPageCount: Int,
    val recentPageNumber: Int,
    val hasNext: Boolean,
    val pageSize: Int
) : DataModel

object PageInfoResMapper : DataMapper<PageInfoRes, PageInfo> {
    override fun PageInfoRes.toDomainModel(): PageInfo {
        return PageInfo(
            totalPageCount = totalPageCount,
            recentPageNumber = recentPageNumber,
            hasNext = hasNext,
            pageSize = pageSize
        )
    }
}