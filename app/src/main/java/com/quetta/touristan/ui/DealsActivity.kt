package com.quetta.touristan.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.quetta.touristan.R
import com.quetta.touristan.model.DealItem
import com.quetta.touristan.model.ListingItemText


class DealsActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    private val number = "+923359528381"

    private val pageColors = listOf(R.color.economy, R.color.standard, R.color.premium)
    private val dealNames = listOf(R.string.page_economy, R.string.page_standard, R.string.page_premium)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deals)

        val listings = arrayListOf(
            ListingItemText("Tour comprising of 5 days"),
            ListingItemText("First visit to Harnai"),
            ListingItemText("The second day will be trip to Ziarat"),
            ListingItemText("The next day will go to Bostan"),
            ListingItemText("Finally, to Kan Materzai"),
            ListingItemText("Will wind up with grand dinner")
        )

        val listings2 = arrayListOf(
            ListingItemText("Tour comprising of 6 days"),
            ListingItemText("Starting with visit to Hana Urak"),
            ListingItemText("Followed by stay in Pishin Rest House"),
            ListingItemText("The next day will travel to Shahban"),
            ListingItemText("Will wind up with grand dinner at Usmania Restaurant")
        )

        val listings3 = arrayListOf(
            ListingItemText("Tour will last a complete week"),
            ListingItemText("Travel towards Zhob"),
            ListingItemText("Next day at Loralai (Rest House)"),
            ListingItemText("Next day to discover the forestry of Ziarat (Rest House)"),
            ListingItemText("A visis to Toba Karkari"),
            ListingItemText("BBQ serving at Pishin"),
            ListingItemText("Will finish off with dinner at Bolan")
        )

        val dealItem = DealItem(getString(dealNames[0]), "15,00", listings)
        val dealItem2 = DealItem(getString(dealNames[1]), "19,000", listings2)
        val dealItem3 = DealItem(getString(dealNames[2]), "25,000", listings3)

        val fragments = listOf(
            DealFragment(dealItem, pageColors[0]) { onSelection() },
            DealFragment(dealItem2, pageColors[1]) { onSelection() },
            DealFragment(dealItem3, pageColors[2]) { onSelection() }
        )

        val pagerAdaper = DealsPagerAdapter(this, fragments, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager_deals)
        viewPager.adapter = pagerAdaper

    }

    private fun onSelection() {
        val index = viewPager.currentItem
        val deal = getString(dealNames[index])

        val msg = "I would like to avail Touristan's picnic deal '${deal}'. Please let me know about the payment method and departure timing!"

        val uri = Uri.parse("smsto:$number")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", msg)
        startActivity(intent)
    }

}