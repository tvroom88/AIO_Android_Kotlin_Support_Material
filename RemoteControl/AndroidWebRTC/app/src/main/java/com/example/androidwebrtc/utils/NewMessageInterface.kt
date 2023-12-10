package com.example.androidwebrtc.utils

import com.example.androidwebrtc.models.MessageModel

interface NewMessageInterface {
    fun onNewMessage(message: MessageModel)
}