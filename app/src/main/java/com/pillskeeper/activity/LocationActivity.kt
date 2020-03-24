package com.pillskeeper.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pillskeeper.R
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {

    val REQUEST_POSITION_PERMISSION_ID = 1
    private val searchUrl =
        "https://www.google.com/maps/search/?api=1&query="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        search_location.setOnClickListener {
            openMaps()
            finish()
        }
        back_location_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun openMaps() {
        lateinit var fusedLocationClient: FusedLocationProviderClient
        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl+R.string.query_location))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)

            }
        } else {
            // Make a request for foreground-only location access.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_POSITION_PERMISSION_ID
            )
        }
    }
}