package site.dogether.common.utils

object DeeplinkConstants {
    const val PATH_INVITE = "/invite"

    const val QUERY_CODE = "code"
}

object DeeplinkUtil {
    private const val FORMAT_DEEPLINK_INVITE = "https://dogether.site/invite?code=%s"
    private const val FORMAT_INVITE_TEXT = "[캘린더 투두 뿌시기]에서 당신의 참여를 기다리고 있어요\n" +
            "작심삼일도 괜찮아요.\n" +
            "투두 챌린지 서비스 두게더에서\n" +
            "팀원들과 함께 목표 달성을 시작해보세요 \n" +
            "초대코드: %s\n" +
            "%s"
    const val DEEPLINK_DOMAIN = "dogether-app.chottu.link"


    fun generateInviteDeeplink(code: String): String {
        return FORMAT_DEEPLINK_INVITE.format(code)
    }

    fun generateInviteText(code: String, url: String): String {
        return FORMAT_INVITE_TEXT.format(code, url)
    }
}