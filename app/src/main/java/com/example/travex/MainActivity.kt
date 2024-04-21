package com.example.travex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), placeAdapter.OnItemClickListener {


        private lateinit var recyclerView: RecyclerView
        private lateinit var placeAdapter: placeAdapter
        private lateinit var searchView: SearchView
        private val db = FirebaseFirestore.getInstance()

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)

                val btnU: Button = findViewById(R.id.BtnUm)

                btnU.setOnClickListener{
                        val intent = Intent(this,UserManual::class.java)
                        startActivity(intent)
                }

                val cityCultureCard: CardView = findViewById(R.id.citycultureCard)
                val natureCard: CardView = findViewById(R.id.natureCard)
                val islandsCard: CardView = findViewById(R.id.IslandCard)

                cityCultureCard.setOnClickListener {
                        val intent = Intent(this, CitycultureActivity::class.java)
                        startActivity(intent)
                }

                natureCard.setOnClickListener {
                        val intent = Intent(this, NatureActivity::class.java)
                        startActivity(intent)
                }

                islandsCard.setOnClickListener {
                        val intent = Intent(this, IslandsbeachesActivity::class.java)
                        startActivity(intent)
                }

                val fab1: FloatingActionButton = findViewById(R.id.fab1)


                fab1.setOnClickListener {
                        // Handle click for FAB 1
                        val intent = Intent(this, ReviewActivity::class.java)
                        startActivity(intent)
                }



                val toolbar: Toolbar = findViewById(R.id.toolbar)
                setSupportActionBar(toolbar)

                // Set the title of the Toolbar to the app name
                supportActionBar?.title = getString(R.string.app_name)

                // Enable the Up button
                supportActionBar?.setDisplayHomeAsUpEnabled(true)

                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)

                searchView = findViewById(R.id.search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                                placeAdapter.filter.filter(newText)
                                return false
                        }
                })

                // Fetch data from Firestore and populate RecyclerView
                fetchPlaces()
        }

        override fun onItemClick(place: Place) {
                // Handle item click here
                val intent = Intent(this, PlaceDetails::class.java)
                intent.putExtra("placeName", place.placeName)
                intent.putExtra("placeImage", place.placeImage)
                startActivity(intent)
                Toast.makeText(this, "Clicked on " + place.placeName, Toast.LENGTH_SHORT).show()
        }

        private fun fetchPlaces() {
                db.collection("places")
                        .get()
                        .addOnSuccessListener { documents ->
                                val places = mutableListOf<Place>()
                                for (document in documents) {
                                        val name = document.getString("placeName") ?: ""
                                        val imageUrl = document.getString("placeImage") ?: ""
                                        places.add(Place(name, imageUrl))
                                }

                                placeAdapter = placeAdapter(places, this@MainActivity)
                                recyclerView.adapter = placeAdapter

                                Log.d("com.example.travex.MainActivity", "Places retrieved successfully: $places")
                        }
                        .addOnFailureListener { exception ->
                                Log.e("com.example.travex.MainActivity", "Error getting places", exception)
                        }
        }
}
