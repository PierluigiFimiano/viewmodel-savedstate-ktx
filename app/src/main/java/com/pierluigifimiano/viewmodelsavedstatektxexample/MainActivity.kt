package com.pierluigifimiano.viewmodelsavedstatektxexample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.putExtra(FIRST_VALUE_KEY, 5)

        val tv: TextView = findViewById(R.id.counter)
        viewModel.myValue.observe(this) {
            tv.text = it.toString()
        }

        findViewById<View>(R.id.inc_button).setOnClickListener { viewModel.increase() }
        findViewById<View>(R.id.dec_button).setOnClickListener { viewModel.decrease() }
    }

}