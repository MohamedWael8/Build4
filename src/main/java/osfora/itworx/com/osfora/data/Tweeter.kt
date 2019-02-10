package osfora.itworx.com.osfora.data

import java.lang.Math.abs

data class Tweeter(val name: String,
                   val screenName: String,
                   val numberOfFollowers: Int,
                   val profilePictureUrl: String,
                   val isVerified: Boolean,
                   val tweeterId: String) {

    fun getNumberOfFollowersAsString(): String {
        if (numberOfFollowers < 1_000) {
            return "${numberOfFollowers}"
        } else if (numberOfFollowers < 1_000_000) {
            return "${abs(numberOfFollowers / 1_000)}k"
        } else {
            return "${abs(numberOfFollowers / 1_000_000)}M"
        }
    }

}
