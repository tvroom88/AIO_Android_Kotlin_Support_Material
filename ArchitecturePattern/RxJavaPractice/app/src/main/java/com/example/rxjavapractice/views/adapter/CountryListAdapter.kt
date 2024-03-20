package com.example.rxjavapractice.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavapractice.R
import com.example.rxjavapractice.dataModel.CountryModel
import com.example.rxjavapractice.utils.Util

class CountryListAdapter(countries: MutableList<CountryModel>) :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    private val countries: MutableList<CountryModel>

    init {
        this.countries = countries
    }

    fun updateCountries(newCountries: List<CountryModel>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class CountryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var countryImage = itemView.findViewById<ImageView>(R.id.countryimageview)
        var countryName = itemView.findViewById<TextView>(R.id.name)
        var countryCapital = itemView.findViewById<TextView>(R.id.capital)


        fun bind(country: CountryModel) {
            countryName?.text = country.countryName
            countryCapital?.text = (country.capital)
            Util.loadImage(
                countryImage,
                country.flag,
                Util.getProgressDrawable(countryImage!!.context)
            )
        }
    }
}