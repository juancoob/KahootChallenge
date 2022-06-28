package com.juancoob.kahootchallenge.common

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun View.loadBackground(@DrawableRes drawableId: Int) {
    Glide.with(context).load(drawableId).into(object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            this@loadBackground.background = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) = Unit
    })
}

fun ImageView.loadDrawable(@DrawableRes drawableId: Int) {
    Glide.with(context).load(drawableId).into(this)
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).into(this)
}
