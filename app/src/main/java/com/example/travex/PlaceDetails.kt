package com.example.travex

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class PlaceDetails : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var locationTextView: TextView
    private lateinit var titleLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placedetails)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the title of the Toolbar to the app name
        supportActionBar?.title = getString(R.string.app_name)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        db = FirebaseFirestore.getInstance()

        val placeName = intent.getStringExtra("placeName")
        val placeImage = intent.getStringExtra("placeImage")

        val nameTextView: TextView = findViewById(R.id.tVplaceName)
        val detailsTextView: TextView = findViewById(R.id.tVplaceDetails)
        val imageView: ImageView = findViewById(R.id.iVplaceImage)
        val titleLocation: TextView = findViewById(R.id.tVLocation)
        locationTextView = findViewById(R.id.tVplaceLocation)

        nameTextView.text = placeName

        Glide.with(this).load(placeImage).into(imageView)

        // Retrieve place details from Firestore based on placeName
        if (placeName != null) {
            retrievePlaceDetails(placeName)
        }

        // Handle TextView click to open Google Maps
        locationTextView.setOnClickListener {
            val locationLink = locationTextView.text.toString()
            openGoogleMaps(locationLink)
        }
    }

    private fun retrievePlaceDetails(placeName: String) {
        db.collection("places")
            .whereEqualTo("placeName", placeName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val placeDetails = document.getString("placeDetails")
                    val placeLocation = document.getString("placeLocation") // Assuming the field name is "placeLocation"
                    if (placeDetails != null) {
                        // Display placeDetails in detailsTextView
                        findViewById<TextView>(R.id.tVplaceDetails).text = placeDetails
                    }
                    if (placeLocation != null) {
                        // Display placeLocation in locationTextView
                        locationTextView.text = placeLocation
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PlaceDetails", "Error getting documents", exception)
            }
    }

    private fun openGoogleMaps(locationLink: String) {
        val gmmIntentUri = Uri.parse(locationLink)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}
