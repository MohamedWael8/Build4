package osfora.itworx.com.osfora.view_controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_search.*
import android.view.View
import osfora.itworx.com.osfora.R
import osfora.itworx.com.osfora.data.Tweeter
import osfora.itworx.com.osfora.data_controller.SearchDataController


class SearchActivity : AppCompatActivity() {

    private lateinit var searchDataController: SearchDataController

    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        prepareRecyclerView()
        prepareEditText()
        prepareDataController()
        prepareTryAgainButton()
    }

    // Initialization

    private fun prepareRecyclerView() {
        searchResultsRecyclerView.setHasFixedSize(true)
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun prepareEditText() {
        searchEditText.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                startSearch()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun prepareDataController() {
        searchDataController = SearchDataController.Factory.newInstance(this.applicationContext)
    }

    private fun prepareTryAgainButton() {
        retryButton.setOnClickListener {
            startSearch()
        }
    }

    private fun startSearch() {
        prepareView(State.SEARCHING)
        searchFor(searchEditText.text.toString())
    }

    private fun prepareView(state: State) {
        when (state) {
            State.SEARCHING -> {
                progressBar.visibility = View.VISIBLE
                statusLayout.visibility = View.GONE
                searchResultsRecyclerView.visibility = View.GONE
            }
            State.WITH_RESULTS -> {
                progressBar.visibility = View.GONE
                searchResultsRecyclerView.visibility = View.VISIBLE
                statusLayout.visibility = View.GONE
            }
            State.EMPTY_RESULTS -> {
                progressBar.visibility = View.GONE
                searchResultsRecyclerView.visibility = View.GONE
                statusImageView.setImageDrawable(getDrawable(R.drawable.ic_empty_results))
                statusTextView.text = getString(R.string.empty_results_text) + " \"${searchEditText.text}\""
                retryButton.visibility = View.GONE
                statusLayout.visibility = View.VISIBLE
            }
            State.OFFLINE_ERROR -> {
                progressBar.visibility = View.GONE
                searchResultsRecyclerView.visibility = View.GONE
                statusImageView.setImageDrawable(getDrawable(R.drawable.ic_error))
                statusTextView.text = getString(R.string.offline_error)
                retryButton.visibility = View.VISIBLE
                statusLayout.visibility = View.VISIBLE
            }
            State.SERVER_ERROR -> {
                progressBar.visibility = View.GONE
                searchResultsRecyclerView.visibility = View.GONE
                statusImageView.setImageDrawable(getDrawable(R.drawable.ic_error))
                statusTextView.text = getString(R.string.server_error)
                retryButton.visibility = View.VISIBLE
                statusLayout.visibility = View.VISIBLE
            }
            State.TIMEOUT_ERROR -> {
                progressBar.visibility = View.GONE
                searchResultsRecyclerView.visibility = View.GONE
                statusImageView.setImageDrawable(getDrawable(R.drawable.ic_error))
                statusTextView.text = getString(R.string.timeout_error)
                retryButton.visibility = View.VISIBLE
                statusLayout.visibility = View.VISIBLE
            }
        }
    }

    // Searching

    private fun searchFor(searchTerm: String?) {
        if (searchTerm != null &&
            searchTerm.isNotEmpty() &&
            searchTerm.trim().isNotEmpty()) {
            searchDataController.searchFor(searchEditText.text.trim().toString(),
                successHandler = {
                    handleResponse(it)
            }, errorHandler = {
                searchFailed(it)
            })
        } else {
            prepareView(State.EMPTY_RESULTS)
        }
    }

    private fun handleResponse(listOfTweeters: List<Tweeter>) {
        if (listOfTweeters.isEmpty()) {
            prepareView(State.EMPTY_RESULTS)
        } else {
            searchResultsRecyclerView.adapter = SearchResultsRecyclerViewAdapter(searchDataController)
            prepareView(State.WITH_RESULTS)
        }
    }

    fun searchFailed(searchError: SearchDataController.SearchError) {
        when (searchError) {
            SearchDataController.SearchError.ERROR_OFFLINE -> {
                prepareView(State.OFFLINE_ERROR)
            }
            SearchDataController.SearchError.ERROR_SERVER -> {
                prepareView(State.SERVER_ERROR)
            }
            SearchDataController.SearchError.ERROR_TIMEOUT -> {
                prepareView(State.TIMEOUT_ERROR)
            }
        }
    }

    private enum class State {
        SEARCHING,
        EMPTY_RESULTS,
        WITH_RESULTS,
        OFFLINE_ERROR,
        TIMEOUT_ERROR,
        SERVER_ERROR
    }

}
