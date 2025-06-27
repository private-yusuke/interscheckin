package pub.yusuke.interscheckin.ui.friendselection.util

import pub.yusuke.interscheckin.navigation.entity.Friend

object FriendSelectionScreenTestData {
    val sampleFriends = listOf(
        Friend(
            id = "friend1",
            firstName = "太郎",
            lastName = "田中",
            bio = "こんにちは！",
            photo = Friend.Photo(
                prefix = "https://example.com/photo_",
                suffix = ".jpg",
            ),
            homeCity = "東京, 日本",
        ),
        Friend(
            id = "friend2",
            firstName = "花子",
            lastName = "佐藤",
            bio = null,
            photo = null,
            homeCity = "大阪, 日本",
        ),
        Friend(
            id = "friend3",
            firstName = "次郎",
            lastName = "山田",
            bio = "よろしく！",
            photo = Friend.Photo(
                prefix = "https://example.com/photo2_",
                suffix = ".png",
            ),
            homeCity = "京都, 日本",
        ),
    )
}