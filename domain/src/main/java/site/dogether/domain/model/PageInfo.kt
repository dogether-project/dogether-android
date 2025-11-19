package site.dogether.domain.model

data class PageInfo(
    val totalPageCount: Int = 0,
    val recentPageNumber: Int = 0,
    val hasNext: Boolean = false,
    val pageSize: Int = 0
) : DomainModel
