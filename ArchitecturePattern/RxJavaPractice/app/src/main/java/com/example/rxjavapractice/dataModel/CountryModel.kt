package com.example.rxjavapractice.dataModel

import com.google.gson.annotations.SerializedName

//https://raw.githubusercontent.com/DevTides/countries/master/countriesV2.json
data class CountryModel(
    // 나라 이름을 가짐
    @field:SerializedName("name") var countryName: String, // 나라 수도 이름을 가짐
    @field:SerializedName("capital") var capital: String,
    @field:SerializedName("flagPNG") var flag: String
)
