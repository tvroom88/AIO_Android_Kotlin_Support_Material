package com.example.androidwebrtc.utils

import com.example.androidwebrtc.models.MessageModel

interface MessageInterface {
    fun onNewMessage(message: MessageModel)
}