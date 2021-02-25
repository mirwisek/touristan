package com.quetta.touristan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.circularreveal.cardview.CircularRevealCardView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomSheet = SuggestedPlacesFragment()

        val cardView = findViewById<CircularRevealCardView>(R.id.cardView)
        cardView.setOnClickListener {
            bottomSheet.show(supportFragmentManager, SuggestedPlacesFragment.TAG)
        }

    }

}