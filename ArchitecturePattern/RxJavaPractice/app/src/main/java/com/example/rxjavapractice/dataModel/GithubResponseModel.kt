package com.example.rxjavapractice.dataModel

import com.google.gson.annotations.SerializedName

data class GithubResponseModel(
    // 나라 이름을 가짐
    @SerializedName("id") var countryName: String, // 나라 수도 이름을 가짐
    @SerializedName("capital") var capital: String,
    @SerializedName("flagPNG") var flag: String
)
