package com.websarva.wings.android.implicitintentsample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private var _latitude = 0.0
    private var _longitude = 0.0

    private lateinit var _fusedLocationClient: FusedLocationProviderClient
    private lateinit var _locationRequest: LocationRequest
    private lateinit var _onUpdateLocation: OnUpdateLocation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        _locationRequest = LocationRequest.create()
        _locationRequest?.let{
            it.interval = 5000
            it.fastestInterval = 1000
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        _onUpdateLocation = OnUpdateLocation()
    }

    fun onMapSearchButtonClick(view: View){

        val etSearchWord = findViewById<EditText>(R.id.etSearchWord)
        var searchWord = etSearchWord.text.toString()
        searchWord = URLEncoder.encode(searchWord, "UTF-8")
        val uriStr = "geo:0,0?q=${searchWord}"
        val uri = Uri.parse(uriStr)
        val intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    fun onMapShowCurrentButtonClick(view: View){
        val uriStr = "geo:${_latitude},${_longitude}"
        val uri = Uri.parse(uriStr)

        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private inner class OnUpdateLocation: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
           locationResult?.let {
               val location = it.lastLocation
               location?.let {
                   _latitude = it.latitude
                   _longitude = it.longitude
                   val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
                   tvLatitude.text = _latitude.toString()
                   val tvLongitude = findViewById<TextView>(R.id.tvLongitude)
                   tvLongitude.text = _longitude.toString()
               }
           }
        }
    }
    override fun onResume(){
        super.onResume()

        //ACCESS_FINE_LOCATIONの許可が降りていないなら
        if (ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           val premissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this@MainActivity,premissions,1000)
            return
        }
        _fusedLocationClient.requestLocationUpdates(_locationRequest, _onUpdateLocation, mainLooper)
    }

    override fun onPause(){
        super.onPause()
        _fusedLocationClient.removeLocationUpdates(_onUpdateLocation)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions:Array<String>,grantResult:IntArray){
        if(requestCode == 1000 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return
            }
            _fusedLocationClient.requestLocationUpdates(_locationRequest,_onUpdateLocation,mainLooper)
        }
    }
}