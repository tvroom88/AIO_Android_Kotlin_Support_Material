package com.example.rxjavaexample.views.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavaexample.R
import com.example.rxjavaexample.dataModel.SampleModel

class SearchExampleAdapter(private var dataSet: List<SampleModel>) :
    RecyclerView.Adapter<SearchExampleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.myTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].title

        holder.itemView.setOnClickListener {
            Log.d("data set", "data title ${dataSet[position].title}")
        }
    }

    override fun getItemCount(): Int {
        Log.d("checkcheckcheck", "${dataSet.size}")
        return dataSet.size
    }

    fun setDataSet(dataSet: List<SampleModel>) {
        this.dataSet = dataSet
    }

    fun dataChanged() {
        notifyDataSetChanged()
    }

}
