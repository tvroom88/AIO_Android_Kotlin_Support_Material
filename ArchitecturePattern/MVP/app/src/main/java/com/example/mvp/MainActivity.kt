package com.example.mvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity(), Contract.View {
    // creating object of TextView class
    private var textView: TextView? = null

    // creating object of Button class
    private var button: Button? = null

    // creating object of ProgressBar class
    private var progressBar: ProgressBar? = null

    // creating object of Presenter interface in Contract
    private var presenter: Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // assigning ID of the TextView
        textView = findViewById(R.id.textView)

        // assigning ID of the Button
        button = findViewById(R.id.button)

        // assigning ID of the ProgressBar
        progressBar = findViewById(R.id.progressBar)

        // instantiating object of Presenter Interface
        presenter = Presenter(this, Model())

        // operations to be performed when
        // user clicks the button
        this.button!!.setOnClickListener { presenter!!.onButtonClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onDestroy()
    }

    // method to display the Course Detail TextView
    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
        textView!!.visibility = View.INVISIBLE
    }

    // method to hide the Course Detail TextView
    override fun hideProgress() {
        progressBar!!.visibility = View.GONE
        textView!!.visibility = View.VISIBLE
    }

    // method to set random string in the Course Detail TextView
    override fun setString(string: String?) {
        textView!!.text = string
    }
}