package com.example.mvc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

class MainActivity : AppCompatActivity(), PropertyChangeListener, View.OnClickListener {

    // creating object of Model class
    private var myModel: Model? = null

    // creating object of Button class
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // creating relationship between the observable Model and the aobserver Activity
        myModel = Model()
        myModel!!.addPropertyChangeListener(this)

        // assigning button IDs to the objects
        button1 = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)


        // transfer the control to Onclick() method when a button is clicked by passing argument "this"
        button1?.setOnClickListener(this)
        button2?.setOnClickListener(this)
        button3?.setOnClickListener(this)
    }

    // calling setValueAtIndex() method by passing appropriate arguments for different buttons
    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> myModel?.setValueAtIndex(0)
            R.id.button2 -> myModel?.setValueAtIndex(1)
            R.id.button3 -> myModel?.setValueAtIndex(2)
        }
    }

    override fun propertyChange(evt: PropertyChangeEvent?) {
        // changing text of the buttons, according to updated values
        button1!!.text = String.format(getString(R.string.count2, myModel!!.getValueAtIndex(0)))
        button2!!.text = String.format(getString(R.string.count2, myModel!!.getValueAtIndex(1)))
        button3!!.text = String.format(getString(R.string.count2, myModel!!.getValueAtIndex(2)))
    }
}
