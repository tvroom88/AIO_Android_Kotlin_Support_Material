package com.example.androidwebrtc.models

data class MessageModel(
     val type: String,
     val name: String? = null,
     val target: String? = null,
     val data:Any?=null,
     val msg:String ?= null
)
