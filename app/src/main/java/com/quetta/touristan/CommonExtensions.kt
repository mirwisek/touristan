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
import java.io.IOException

const val USER_SHARED_PREFS = "tour-shared-prefs"

val String.latLng: LatLng
get() {
    val str = this.split(", ")
    return LatLng(str[0].toDouble(), str[1].toDouble())
}

@Throws(IOException::class)
fun Context.readRawJson(rawResourceId: Int): String {
    val inputStream = resources.openRawResource(rawResourceId)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charsets.UTF_8)
}

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

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context!!, msg, duration).show()
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

fun log(msg: String, tag: String = "ffnet") {
    Log.d(tag, msg)
}

fun CoroutineScope.log(msg: String, tag: String = "ffnet") {
    Log.d(tag, msg)
}