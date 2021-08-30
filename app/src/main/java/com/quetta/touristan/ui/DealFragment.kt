package com.quetta.touristan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.quetta.touristan.R
import com.quetta.touristan.custom.DealListingAdapter
import com.quetta.touristan.model.DealItem

class DealFragment(
    private val dealItem: DealItem,
    private val themeColor: Int,
    private val onSelected: (() -> Unit)? = null
) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DealListingAdapter
//    private var dealItem: DealItem? = null

    companion object {
        const val TAG = "DealFragment"
        const val KEY_ARG_DEAL_ITEM = "SuggestedPlacesFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let { args ->
//            dealItem = args.getParcelable(KEY_ARG_DEAL_ITEM)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.layout_deal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val color = ContextCompat.getColor(requireContext(), themeColor)

        recyclerView = view.findViewById(R.id.rvListings)
        val packageTitle = view.findViewById<TextView>(R.id.packageTitle).apply {
            text = dealItem.packageTitle
        }
        val priceTitle = view.findViewById<TextView>(R.id.price).apply {
            text = "Rs. ${dealItem.price}"
        }
        val btn = view.findViewById<MaterialButton>(R.id.btnPurchase).apply {
            setBackgroundColor(color)
        }

        val card = view.findViewById<CircularRevealCardView>(R.id.card).apply {
            setCardBackgroundColor(color)
        }

        adapter = DealListingAdapter(requireContext())

        dealItem.listingItems?.let { adapter.updateItems(it) }

        btn.setOnClickListener {
            // TODO: Update index
            onSelected?.invoke()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

    }
}