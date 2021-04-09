package com.quetta.touristan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quetta.touristan.*
import com.quetta.touristan.custom.PlacesAdapter
import com.quetta.touristan.model.HomeIntent
import com.quetta.touristan.model.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var vmHome: HomeViewModel
    private lateinit var vmMapsActivity: MapsActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlacesAdapter
    private var category: String? = null
    private var job: Job? = null

    companion object {
        const val TAG = "SuggestedPlacesFragment"
        const val RADIUS = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let { args ->
            val resource = args.getInt("category")
            category = getString(resource)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // We need to send PlacesClient initialized in Application to Repository through ViewModel
        val vmFactory = HomeViewModelFactory((requireActivity().application as TouristanApp))
        vmHome = ViewModelProvider(requireActivity(), vmFactory).get(HomeViewModel::class.java)
        vmMapsActivity = ViewModelProvider(requireActivity()).get(MapsActivityViewModel::class.java)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)
        val tvHint = view.findViewById<TextView>(R.id.textHint)

        adapter = PlacesAdapter(requireContext(), vmHome) { tourPlace ->

//            (requireActivity() as MapsActivity).drawOnMap {
//                tourPlace.geometry?.let { geo ->
//                    clear()
//
//                    addMarker(
//                        MarkerOptions()
//                            .position(tourPlace.geometry!!.location.latLng)
//                            .title(tourPlace.name)
//                    )
//
//                    val position = CameraPosition.builder()
//                        .target(geo.location.latLng)
//                        .zoom(14f)
//                        .build()
//
////                    setLatLngBoundsForCameraTarget(LatLngBounds(
////                        geo.viewport.southwest.latLng,
////                        geo.viewport.northeast.latLng
////                    ))
//
//                    animateCamera(CameraUpdateFactory.newCameraPosition(position))
//                }
//            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter


        job = lifecycleScope.launch {

            val location = MapsActivity.QUETTA.let { "${it.latitude},${it.longitude}" }
//                requireContext().sharedPrefs.getString(MapsActivity.KEY_USER_SAVED_LOCATION, null)
            // TODO : remove hard coded location
            log("Category is ${category}")
            category?.let { categoryPlace ->
                vmHome.handleIntent(location, RADIUS, categoryPlace)

//            vmMapsActivity.selectedCategory.observe(requireActivity()) { selectedType ->
//            }

                // Only send request if there are no items
//            if(adapter.itemCount <= 0)

                vmHome.placesIntent.send(HomeIntent.GetPlaces)

                vmHome.state.collect { state ->
                    when (state) {
                        is HomeState.Idle -> {
                        }
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
                                if (it.isEmpty()) {
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