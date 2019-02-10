package osfora.itworx.com.osfora.data_controller

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.itworx.osfoora.makeApiCall
import org.json.JSONArray
import osfora.itworx.com.osfora.R
import osfora.itworx.com.osfora.data.Tweeter

open class SearchDataController(val context: Context) {

    private lateinit var listOfTweeters: ArrayList<Tweeter>

    public open fun searchFor(searchTerm: String,
                              successHandler: (List<Tweeter>) -> Unit,
                              errorHandler: (SearchError) -> Unit) {
        makeApiCall("https://api.twitter.com/1.1/users/search.json?q=$searchTerm&count=20",
            successResponseAction = 
			{
                val responseJson = JSONArray(it)
                if (responseJson.length() > 0) 
				{
                    listOfTweeters = ArrayList<Tweeter>()
                    for (i in 0..(responseJson.length() - 1)) 
					{
                        val tweeterJsonObject = responseJson.getJSONObject(i)
                        val tweeter = Tweeter(name = tweeterJsonObject.getString("name"),
                            screenName = tweeterJsonObject.getString("screen_name"),
                            numberOfFollowers = tweeterJsonObject.getInt("followers_count"),
                            profilePictureUrl = tweeterJsonObject.getString("profile_image_url_https"),
                            isVerified = tweeterJsonObject.getBoolean("verified"),
                            tweeterId = tweeterJsonObject.getString("id_str"))
                        listOfTweeters.add(tweeter)
                        if (i == 500) break
                    }
                    successHandler(listOfTweeters)
                } 
				else 
				{
                    successHandler(ArrayList())
                }
        }, failedResponseAction = 
		{
            errorHandler(it)
        })
    }

    open fun sizeOfListOfTweeters(): Int 
	{
        return listOfTweeters.size
    }

    open fun getTweeterNameAtPosition(position: Int): String {
        return listOfTweeters[position].name
    }

    open fun getTweeterScreenNameAtPosition(position: Int): String {
        return listOfTweeters[position].screenName
    }

    open fun getTweeterNumberOfFollowersAtPosition(position: Int): String {
        return "${listOfTweeters[position].getNumberOfFollowersAsString()} " + context.getString(R.string.followers)
    }

    open fun isTweeterVerifiedAtPosition(position: Int): Boolean {
        return listOfTweeters[position].isVerified
    }

    open fun getTweeterProfilePictureUrlAtPosition(position: Int): String {
        return listOfTweeters[position].profilePictureUrl
    }

    open fun getTweeterIdAtPosition(position: Int): String {
        return listOfTweeters[position].tweeterId
    }

    class Factory {

        companion object {
            private val isSimulatingResults: Boolean = false

            public fun newInstance(context: Context): SearchDataController {
                return if (isSimulatingResults) {
                    SearchDataControllerProxy(context)
                } else {
                    SearchDataController(context)
                }
            }
        }

    }

    enum class SearchError {
        ERROR_TIMEOUT,
        ERROR_SERVER,
        ERROR_OFFLINE
    }

}

class SearchDataControllerProxy(context: Context) : SearchDataController(context) {

    private val isSimulatingOffline = false
    private val isSimulatingAPIError = false
    private val isSimulatingTimeoutError = false
    private val isSimulatingEmptyResults = false

    private lateinit var listOfTweeters: ArrayList<Tweeter>

    public override fun searchFor(
        searchTerm: String,
        successHandler: (List<Tweeter>) -> Unit,
        errorHandler: (SearchError) -> Unit) {
        if (isSimulatingOffline) {
            object : Thread(){
                override fun run() {
                    Thread.sleep(2500)
                    Handler(Looper.getMainLooper()).post {
                        errorHandler(SearchError.ERROR_OFFLINE)
                    }
                }
            }.start()
        } else if (isSimulatingAPIError) {
            object : Thread(){
                override fun run() {
                    Thread.sleep(2500)
                    Handler(Looper.getMainLooper()).post {
                        errorHandler(SearchError.ERROR_SERVER)
                    }
                }
            }.start()
        } else if (isSimulatingTimeoutError) {
            object : Thread(){
                override fun run() {
                    Thread.sleep(2500)
                    Handler(Looper.getMainLooper()).post {
                        errorHandler(SearchError.ERROR_TIMEOUT)
                    }
                }
            }.start()
        } else if (isSimulatingEmptyResults) {
            object : Thread(){
                override fun run() {
                    Thread.sleep(2500)
                    val listOfTweeters = ArrayList<Tweeter>()
                    Handler(Looper.getMainLooper()).post {
                        successHandler(listOfTweeters)
                    }
                }
            }.start()
        } else {
            object : Thread(){
                override fun run() {
                    Thread.sleep(2500)
                    listOfTweeters = ArrayList<Tweeter>()
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook 2", "@T_Cook 2", 120, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", false, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    listOfTweeters.add(Tweeter("Tim Cook", "@T_Cook", 3456, "https://pbs.twimg.com/profile_images/1035649273721847809/B0f8n_oe_400x400.jpg", true, "avcd"))
                    Handler(Looper.getMainLooper()).post {
                        successHandler(listOfTweeters)
                    }
                }
            }.start()
        }
    }

    override fun sizeOfListOfTweeters(): Int {
        return listOfTweeters.size
    }

    override fun getTweeterNameAtPosition(position: Int): String {
        return listOfTweeters[position].name
    }

    override fun getTweeterScreenNameAtPosition(position: Int): String {
        return listOfTweeters[position].screenName
    }

    override fun getTweeterNumberOfFollowersAtPosition(position: Int): String {
        return "${listOfTweeters[position].numberOfFollowers} " + context.getString(R.string.followers)
    }

    override fun isTweeterVerifiedAtPosition(position: Int): Boolean {
        return listOfTweeters[position].isVerified
    }

    override fun getTweeterProfilePictureUrlAtPosition(position: Int): String {
        return listOfTweeters[position].profilePictureUrl
    }

    override fun getTweeterIdAtPosition(position: Int): String {
        return listOfTweeters[position].tweeterId
    }
}
