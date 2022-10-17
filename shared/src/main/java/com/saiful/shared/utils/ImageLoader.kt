package com.saiful.shared.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.shared.R

fun ImageView.loadBackDropSizeImage(url: String?, drawable: Int = R.drawable.no_image_icon) {
    Glide.with(this.context)
        .load(AppConstants.imageBaseUrl + AppConstants.backdropSize + url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .error(drawable)
        .into(this)

}

fun ImageView.loadPosterSizeImage(url: String?, drawable: Int = R.drawable.no_image_icon) {
    Glide.with(this.context)
        .load(AppConstants.imageBaseUrl + AppConstants.posterSize + url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .error(drawable)
        .into(this)

}

fun ImageView.loadYoutubeImage(key: String?) {
    Glide.with(this.context)
        .load(AppConstants.youtubeImageUrlPrefix + key + AppConstants.youtubeImageUrlSuffix)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .error(drawable)
        .into(this)
}