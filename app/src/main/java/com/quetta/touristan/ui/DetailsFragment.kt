package com.quetta.touristan.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.quetta.touristan.*
import com.quetta.touristan.R
import com.quetta.touristan.model.PlaceItem
import com.quetta.touristan.ui.MainActivity.Companion.permissions
import com.quetta.touristan.viewmodel.HomeViewModel
import com.quetta.touristan.viewmodel.LocationViewModel

class DetailsFragment : Fragment(), OnActivityResponse, MapListener {

    private lateinit var fusedApi: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var activity: MainActivity
    private lateinit var vmLocation: LocationViewModel
    private lateinit var vmHome: HomeViewModel

    private val callback = OnMapReadyCallback { gMap ->
        mMap = gMap

        fusedApi = LocationServices.getFusedLocationProviderClient(activity)
        if (!activity.hasPermissions(*permissions)) {
            activity.requestPermissions()
        } else {
            onPermissionGranted()
        }

        vmHome.allPlaces.observe(viewLifecycleOwner) { list ->
            mMap.clear()
            list.forEach { placeItem ->
                mMap.addMarker(
                    MarkerOptions()
                        .position(placeItem.location.latLng)
                        .title(placeItem.name)
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
        activity.addListener(this)
        activity.setMapListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vmLocation = ViewModelProvider(activity).get(LocationViewModel::class.java)

        val vmFactory = (activity.application as TouristanApp).vmHomeFactory
        vmHome = ViewModelProvider(activity, vmFactory).get(HomeViewModel::class.java)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private fun forceLocation() {
        log("Forcing location")
        isLocationEnabled()
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                log("Location received ${result.locations}")
                onLocationReceived(result.locations[0])
                fusedApi.removeLocationUpdates(this)
            }
        }
        fusedApi.requestLocationUpdates(MainActivity.locationRequest, callback, Looper.getMainLooper())
    }

    /**
     * Method to perform operation on Location
     */
    private fun onLocationReceived(location: Location) {

        val loc = LatLng(location.latitude, location.longitude)

        activity.sharedPrefs.edit(true) {
            putString(MainActivity.KEY_USER_SAVED_LOCATION, loc.stringVal)
        }

        val position = CameraPosition.builder()
            .target(loc)
            .zoom(14f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    override fun permissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MainActivity.REQUEST_PERMISSION_LOCATION -> {
                if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    toast("Permission denied!")
                } else {
                    onPermissionGranted()
                }
            }
        }
    }

    override fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MainActivity.REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    forceLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun onPermissionGranted() {
        log("OnPermissionGrant")
        if (activity.hasPermissions(*permissions)) {
            // Get last location
            fusedApi.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    // If last location isn't returned, make a new request
                    if (location == null) {
                        // Before retrieving location check if gps is enabled
                        if (isLocationEnabled())
                            forceLocation()
                        else
                            enableGPS()
                    } else {
                        onLocationReceived(location)
                    }
                } else {
                    toast("Your location couldn't be determined")
                }
            }
        } else
            toast("No location permission granted")
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    //region Turn On GPS if it's OFF
    private fun enableGPS(callback: ((LocationSettingsResponse) -> Unit)? = null) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(MainActivity.locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { callback?.invoke(it) }

        // This shows the Alert Dialog to display user to enable GPS
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, MainActivity.REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }

            }
        }
    }

    override fun drawOnMap(placeItem: PlaceItem) {
        val loc = placeItem.location.latLng
        mMap.clear()

        mMap.addMarker(
            MarkerOptions()
                .position(loc)
                .title(placeItem.name)
        )

        val position = CameraPosition.builder()
            .target(loc)
            .zoom(17f)
            .build()

//                    setLatLngBoundsForCameraTarget(LatLngBounds(
//                        geo.viewport.southwest.latLng,
//                        geo.viewport.northeast.latLng
//                    ))

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))
    }
    //endregion


}