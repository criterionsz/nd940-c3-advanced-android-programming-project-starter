package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        file_name.text = intent.getStringExtra(FILE_NAME)
        status_title.text = intent.getStringExtra(STATUS)
        button_ok.setOnClickListener {
            onBackPressed()
        }
    }
}
