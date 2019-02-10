package osfora.itworx.com.osfora.view_controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*
import osfora.itworx.com.osfora.R

class ProfileActivity : AppCompatActivity() {

    companion object {
        public val TWEETER_ID: String = "TWEETER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        tweeterIdTextView.text = intent.getStringExtra(TWEETER_ID)
    }
}
