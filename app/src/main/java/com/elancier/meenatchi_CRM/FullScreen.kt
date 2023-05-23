package com.elancier.meenatchi_CRM

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        var name=intent.extras!!.getString("from")
        var image=intent.extras!!.getString("image")
        ab!!.setTitle(name)

        Glide.with(this).load(image).placeholder(R.drawable.loading_icon).into(fullImage)


    }
}