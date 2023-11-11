package com.dicoding.sub1storyapp.utils.extension

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}


fun ImageView.setImageUrl(url: String, isCenterCrop: Boolean) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .into(this)
}