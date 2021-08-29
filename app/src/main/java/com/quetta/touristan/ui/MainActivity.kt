package com.quetta.touristan.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.quetta.touristan.*
import com.quetta.touristan.login.LoginActivity
import com.quetta.touristan.model.PlaceItem

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var navController: NavController
    private var mapListener: MapListener? = null

    private val listeners = arrayListOf<OnActivityResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_hospitals,
                R.id.navigation_picnic,
                R.id.navigation_banks,
                R.id.navigation_restaurant,
                R.id.navigation_university
            )
        )

        tabLayout = findViewById(R.id.tabs)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun selectTab(index: Int) {
        tabLayout.selectTab(tabLayout.getTabAt(index))
    }

    fun setMapListener(listener: MapListener) {
        mapListener = listener
    }

    fun addListener(listener: OnActivityResponse) {
        listeners.add(listener)
    }

    //region Permission Handling
    fun requestPermissions() {
        // Permission is not granted
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this,
            permissions,
            REQUEST_PERMISSION_LOCATION
        )
    }

    fun onPlaceClicked(placeItem: PlaceItem) {
        selectTab(1)
        mapListener?.drawOnMap(placeItem)
    }

    fun hasPermissions(vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        log("MainActivity - is called onRequestPermission")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        log("MainActivity - is called onRequestPermission")


        listeners.forEach {  listener ->
            listener.permissionResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log("MainActivity is called onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        log("MainActivity is called onActivityResult")
        listeners.forEach {  listener ->
            listener.activityResult(requestCode, resultCode, data)
        }
    }

    //endregion Permission Handling

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                signout()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun signout() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_signin_token))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        // This will sign out the Google account but firebase auth is still signed in
        googleSignInClient.signOut().addOnSuccessListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            toast("Couldn't sign out")
        }

    }

    companion object {
        const val REQUEST_PERMISSION_LOCATION = 4251
        const val REQUEST_CHECK_SETTINGS = 3422
        val QUETTA = LatLng(30.183270, 66.996452)
        const val KEY_USER_SAVED_LOCATION = "userLocationSvd"

        val permissions: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val locationRequest = LocationRequest.create().apply {
            interval = 2_000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}