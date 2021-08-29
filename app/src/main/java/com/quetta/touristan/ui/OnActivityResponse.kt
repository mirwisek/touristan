package com.quetta.touristan.ui

import android.content.Intent

interface OnActivityResponse {
    fun permissionResult(requestCode: Int,
                         permissions: Array<out String>,
                         grantResults: IntArray) {

    }

    fun activityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {

    }
}