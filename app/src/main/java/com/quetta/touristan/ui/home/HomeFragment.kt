package com.quetta.touristan.ui.home

import android.content.Intent
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.quetta.touristan.*
import com.quetta.touristan.custom.FabExtendedOnScrollListener
import com.quetta.touristan.custom.PlacesAdapter
import com.quetta.touristan.model.HomeState
import com.quetta.touristan.ui.DealsActivity
import com.quetta.touristan.ui.MainActivity
import com.quetta.touristan.ui.MapsActivity
import com.quetta.touristan.viewmodel.HomeViewModel
import com.quetta.touristan.viewmodel.MapsActivityViewModel
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

        val key = getString(R.string.arg_category)
        arguments?.let { args ->
            category = args.getString(key)
        }
        log("Category is $category")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // We need to send PlacesClient initialized in Application to Repository through ViewModel
        val vmFactory = (requireActivity().application as TouristanApp).vmHomeFactory
        vmHome = ViewModelProvider(requireActivity(), vmFactory).get(HomeViewModel::class.java)
        vmMapsActivity = ViewModelProvider(requireActivity()).get(MapsActivityViewModel::class.java)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)
        val tvHint = view.findViewById<TextView>(R.id.textHint)
        val fab = view.findViewById<ExtendedFloatingActionButton>(R.id.fab)

        val activity = requireActivity() as MainActivity
        fab.setOnClickListener {
            val i = Intent(activity, DealsActivity::class.java)
            startActivity(i)
        }


        adapter = PlacesAdapter(requireContext(), vmHome) { placeItem ->
            activity.onPlaceClicked(placeItem)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val picnicCategory = getString(R.string.category_picnic)

        job = lifecycleScope.launch {

            val location = MapsActivity.QUETTA.let { "${it.latitude},${it.longitude}" }
//                requireContext().sharedPrefs.getString(MapsActivity.KEY_USER_SAVED_LOCATION, null)

            category?.let { categoryPlace ->
                vmHome.getPlaces(categoryPlace)

                vmHome.state.collect { state ->
                    when (state) {
                        is HomeState.Idle -> {
                        }
                        is HomeState.Loading -> {
                            progressBar.visible()
                            recyclerView.invisible()
                        }
                        is HomeState.Error -> {
                            progressBar.gone()
                            tvHint.text = state.error ?: "Unknown error"
                            tvHint.visible()
                        }
                        is HomeState.Result -> {
                            progressBar.gone()
                            state.places?.let {
                                if (it.isEmpty()) {
                                    tvHint.text = getString(R.string.empty_list)
                                    tvHint.visible()
                                } else {
                                    adapter.updateItems(it)
                                    recyclerView.visible()
                                    if(categoryPlace == picnicCategory) {
                                        recyclerView.addOnScrollListener(FabExtendedOnScrollListener(fab))
                                        fab.visible()
                                    } else {
                                        fab.invisible()
                                    }
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