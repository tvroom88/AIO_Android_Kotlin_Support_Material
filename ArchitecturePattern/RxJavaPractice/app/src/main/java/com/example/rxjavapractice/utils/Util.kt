package com.example.rxjavapractice.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rxjavapractice.R

object Util {
    fun loadImage(view: ImageView, url: String?, progressDrawable: CircularProgressDrawable?) {
        // 이미지 로드할 때 옵션 설정
        val options: RequestOptions = RequestOptions()
            .placeholder(progressDrawable) // 이미지 로딩하는 동안 보여줄 원형 프로그레스
            .error(R.mipmap.ic_launcher_round) // url 로드할 때 error 발생시 보여줄 이미지
        Glide.with(view.context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(view)
    }

    // 이미지 로딩 중에 보여줄 원형 프로그레스 만들기
    fun getProgressDrawable(context: Context?): CircularProgressDrawable {
        val progressDrawable = CircularProgressDrawable(context!!)
        progressDrawable.strokeWidth = 10f
        progressDrawable.centerRadius = 50f
        progressDrawable.start()
        return progressDrawable
    }
}
