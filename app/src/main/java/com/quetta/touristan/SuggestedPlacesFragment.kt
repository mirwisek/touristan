package com.quetta.touristan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quetta.touristan.custom.PlacesAdapter
import com.quetta.touristan.model.HomeIntent
import com.quetta.touristan.model.HomeState
import com.quetta.touristan.model.TourPlace
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SuggestedPlacesFragment : BottomSheetDialogFragment() {

    private lateinit var vmHome: HomeViewModel
    private lateinit var vmMapsActivity: MapsActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlacesAdapter
    private var category: String? = null
    private var job: Job? = null

    companion object {
        const val TAG = "SuggestedPlacesFragment"
        const val RADIUS = 1000
        const val KEY_ARG_CATEGORY = "category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = requireArguments().getString(KEY_ARG_CATEGORY, null)
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
        vmMapsActivity = ViewModelProvider(requireActivity()).get(MapsActivityViewModel::class.java)

        recyclerView = view.findViewById(R.id.list)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)
        val tvHint = view.findViewById<TextView>(R.id.textHint)

        adapter = PlacesAdapter(requireContext(), vmHome) { tourPlace ->

            (requireActivity() as MapsActivity).drawOnMap {
                tourPlace.geometry?.let { geo ->
                    clear()

                    addMarker(
                        MarkerOptions()
                            .position(tourPlace.geometry!!.location.latLng)
                            .title(tourPlace.name)
                    )

                    val position = CameraPosition.builder()
                        .target(geo.location.latLng)
                        .zoom(14f)
                        .build()

//                    setLatLngBoundsForCameraTarget(LatLngBounds(
//                        geo.viewport.southwest.latLng,
//                        geo.viewport.northeast.latLng
//                    ))

                    animateCamera(CameraUpdateFactory.newCameraPosition(position))
                }
                dismiss()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter



        job = lifecycleScope.launch {

            val location = requireContext().sharedPrefs.getString(MapsActivity.KEY_USER_SAVED_LOCATION, null)

            category?.let { categoryPlace ->
                vmHome.handleIntent(location, RADIUS, categoryPlace)

//            vmMapsActivity.selectedCategory.observe(requireActivity()) { selectedType ->
//            }

                // Only send request if there are no items
//            if(adapter.itemCount <= 0)

                vmHome.placesIntent.send(HomeIntent.GetPlaces)

                vmHome.state.collect { state ->
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
                        is HomeState.Result -> {
                            progressBar.gone()
                            state.places?.results?.let {
                                if(it.isEmpty()) {
                                    tvHint.text = getString(R.string.empty_list)
                                    tvHint.visible()
                                } else {
                                    adapter.updateItems(it)
                                }
                            }
                        }
                    }

                    // Inform activity to draw markers
                    vmMapsActivity.state.value = state
                }
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}