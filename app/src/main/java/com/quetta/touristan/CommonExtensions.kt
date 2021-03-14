package com.quetta.touristan

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.quetta.touristan.model.Location
import kotlinx.coroutines.CoroutineScope

const val USER_SHARED_PREFS = "tour-shared-prefs"

val Context.sharedPrefs: SharedPreferences
    get() {
        return getSharedPreferences(USER_SHARED_PREFS, Context.MODE_PRIVATE)
    }

val LatLng.stringVal: String
    get() {
        return "${latitude},$longitude"
    }

val Location.latLng: LatLng
    get() {
        return LatLng(lat, lng)
    }

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Activity.log(msg: String, tag: String = "ffnet") {
    Log.d(tag, msg)
}

fun ContextWrapper.log(msg: String, tag: String = "ffnet") {
    Log.d(tag, msg)
}

fun Fragment.log(msg: String, tag: String = "ffnet") {
    Log.d(tag, msg)
}

fun CoroutineScope.log(msg: String, tag: String = "ffnet") {
    Log.d(tag, msg)
}