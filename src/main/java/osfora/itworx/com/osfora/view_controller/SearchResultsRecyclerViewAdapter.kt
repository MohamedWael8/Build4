package osfora.itworx.com.osfora.view_controller

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import osfora.itworx.com.osfora.R
import osfora.itworx.com.osfora.data_controller.SearchDataController

class SearchResultsRecyclerViewAdapter(val searchDataController: SearchDataController) : RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.TweeterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweeterViewHolder {
        val mainView = LayoutInflater.from(parent.context).inflate(R.layout.tweeter_item, parent, false)
        return TweeterViewHolder(mainView)
    }

    override fun getItemCount(): Int {
        return searchDataController.sizeOfListOfTweeters()
    }

    override fun onBindViewHolder(holder: TweeterViewHolder, position: Int) {
        holder.nameTextView.text = searchDataController.getTweeterNameAtPosition(position)
        holder.screenNameTextView.text = searchDataController.getTweeterScreenNameAtPosition(position)
        holder.numberOfFollowersTextView.text = searchDataController.getTweeterNumberOfFollowersAtPosition(position)
        Glide.with(holder.profilePictureImageView.context)
            .load(searchDataController.getTweeterProfilePictureUrlAtPosition(position))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.profilePictureImageView)
        holder.isVerifiedImageView.visibility = if (searchDataController.isTweeterVerifiedAtPosition(position)) {
            View.VISIBLE
        } else {
            View.GONE
        }
        holder.mainView.setOnClickListener {
            val tweeterId = searchDataController.getTweeterIdAtPosition(position)
            val profileActivityIntent = Intent(it.context, ProfileActivity::class.java)
            profileActivityIntent.putExtra(ProfileActivity.TWEETER_ID, tweeterId)
            it.context.startActivity(profileActivityIntent)
        }
    }

    class TweeterViewHolder(val mainView:View) : RecyclerView.ViewHolder(mainView) {
        val nameTextView: TextView
        val screenNameTextView: TextView
        val numberOfFollowersTextView: TextView
        val profilePictureImageView: ImageView
        val isVerifiedImageView: ImageView

        init {
            nameTextView= mainView.findViewById(R.id.nameTextView)
            screenNameTextView = mainView.findViewById(R.id.screenNameTextView)
            numberOfFollowersTextView = mainView.findViewById(R.id.numberOfFollowersTextView)
            profilePictureImageView = mainView.findViewById(R.id.profilePictureImageView)
            isVerifiedImageView = mainView.findViewById(R.id.isVerifiedImageView)
        }
    }


}
