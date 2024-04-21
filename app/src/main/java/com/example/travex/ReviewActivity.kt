package com.example.travex

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val fab1: FloatingActionButton = findViewById(R.id.fabReview)


        fab1.setOnClickListener {
            // Handle click for FAB 1
            val intent = Intent(this, AddReview::class.java)
            startActivity(intent)
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        // Initialize Firebase database referenceS
        database = FirebaseDatabase.getInstance().reference.child("reviews")

        // Listen for changes in the Firebase database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                displayReviews(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun displayReviews(dataSnapshot: DataSnapshot) {
        val reviewContainer = findViewById<LinearLayout>(R.id.review_container)
        reviewContainer.removeAllViews()

        for (reviewSnapshot in dataSnapshot.children) {
            val review = reviewSnapshot.getValue(Review::class.java)
            review?.let {
                val reviewView = layoutInflater.inflate(R.layout.reviewcard_layout, null)

                // Set review data to views in the review_card layout
                reviewView.findViewById<TextView>(R.id.text_review_place_name).text = it.placeName
                reviewView.findViewById<TextView>(R.id.text_review_description).text = it.description
                reviewView.findViewById<RatingBar>(R.id.rating_bar).rating = it.rating

                reviewContainer.addView(reviewView)
            }
        }
    }
}
