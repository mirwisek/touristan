package com.quetta.touristan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SuggestedPlacesFragment : BottomSheetDialogFragment() {

    private lateinit var vmHome: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlacesAdapter

    companion object {
        const val TAG = "SuggestedPlacesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggested_places_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // We need to send PlacesClient initialized in Application to Repository through ViewModel
        val vmFactory = HomeViewModelFactory((requireActivity().application as TouristanApp))
        vmHome = ViewModelProvider(requireActivity(), vmFactory).get(HomeViewModel::class.java)

        recyclerView = view.findViewById(R.id.list)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)
        val tvHint = view.findViewById<TextView>(R.id.textHint)

        adapter = PlacesAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            vmHome.placesIntent.send(HomeIntent.GetPlaces)
            vmHome.state.collect { state ->
                log("State Arrived ${state}")
                when(state) {
                    is HomeState.Idle -> { }
                    is HomeState.Loading -> {
                        progressBar.visible()
                    }
                    is HomeState.Error -> {
                        progressBar.gone()
                        tvHint.text = state.error ?: "Unknown error"
                        tvHint.visible()
                    }
                    is HomeState.Places -> {
                        progressBar.gone()
                        state.places.let {
                            if(it.isEmpty()) {
                                tvHint.text = getString(R.string.empty_list)
                                tvHint.visible()
                            } else {
                                adapter.updateItems(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private inner class ViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.fragment_suggested_places_list_dialog_item,
            parent,
            false
        )
    ) {

        val title: TextView = itemView.findViewById(R.id.title)
        val primary: TextView = itemView.findViewById(R.id.textPrimary)
        val secondary: TextView = itemView.findViewById(R.id.textSecondary)
    }

    private inner class PlacesAdapter(private var places: List<AutocompletePrediction>? = null) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            places?.get(position)?.let { item ->
                holder.title.text = item.getFullText(null)
                holder.primary.text = item.getPrimaryText(null)
                holder.secondary.text = item.getSecondaryText(null)
            }
        }

        fun updateItems(places: List<AutocompletePrediction>) {
            this.places = places
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return places?.size ?: 0
        }
    }
}