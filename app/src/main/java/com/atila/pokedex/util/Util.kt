package com.atila.pokedex.util
import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.atila.pokedex.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


// function for glide
fun ImageView.downloadImage(url: String?, placeholder: CircularProgressDrawable) {
   // val options = RequestOptions().placeholder(placeholder).error(R.mipmap.ic_launcher_round)

   // .setDefaultRequestOptions(options)
    Glide.with(context)
        .load(url)
        .into(this)
}

fun createPlaceHolder(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}
