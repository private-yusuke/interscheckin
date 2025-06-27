package pub.yusuke.interscheckin.ui.friendselection

import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.interscheckin.navigation.entity.Friend
import javax.inject.Inject

class FriendSelectionInteractor @Inject constructor(
    private val foursquareClient: FoursquareClient,
) : FriendSelectionContract.Interactor {
    override suspend fun fetchFriends(): List<Friend> =
        foursquareClient.getFriends().map { apiFriend ->
            Friend(
                id = apiFriend.id,
                firstName = apiFriend.firstName,
                lastName = apiFriend.lastName,
                bio = apiFriend.bio,
                photo = apiFriend.photo?.let {
                    Friend.Photo(
                        prefix = it.prefix,
                        suffix = it.suffix,
                    )
                },
                homeCity = apiFriend.homeCity,
            )
        }
}
