package com.example.mvc

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*
import kotlin.collections.ArrayList

class Model : PropertyChangeListener {

    // declaring a list of integer
    private val list: MutableList<Int>

    private val propertyChangeSupport = PropertyChangeSupport(this)

    // constructor to initialize the list
    init {
        // reserving the space for list elements
        list = ArrayList(3)

        // adding elements into the list
        list.add(0)
        list.add(0)
        list.add(0)
    }

    // defining getter and setter functions function to return appropriate count value at correct index
    @Throws(IndexOutOfBoundsException::class)
    fun getValueAtIndex(index: Int): Int {
        return list[index]
    }

    // function to make changes in the activity button's count value when user touch it
    @Throws(IndexOutOfBoundsException::class)
    fun setValueAtIndex(index: Int) {
        val oldValue = list[index]
        list[index] = oldValue + 1
        propertyChangeSupport.firePropertyChange("List", oldValue, list[index])
    }

    // Implementing the PropertyChangeListener interface
    override fun propertyChange(evt: PropertyChangeEvent?) {
        // Handle property change events if needed
    }

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        propertyChangeSupport.removePropertyChangeListener(listener)
    }
}