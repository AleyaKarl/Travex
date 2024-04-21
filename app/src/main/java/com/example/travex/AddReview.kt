package com.example.travex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddReview : AppCompatActivity() {

    private lateinit var eTPlaceNameReview: EditText
    private lateinit var eTReview: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var btnSubmit: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        databaseReference = FirebaseDatabase.getInstance().reference.child("reviews")

        eTPlaceNameReview = findViewById(R.id.eTPlaceNameReview)
        eTReview = findViewById(R.id.eTReview)
        ratingBar = findViewById(R.id.ratingBar)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            addReview()
        }
    }

    private fun addReview() {
        try {
            val placeName = eTPlaceNameReview.text.toString().trim()
            val reviewText = eTReview.text.toString().trim()
            val rating = ratingBar.rating

            if (placeName.isNotEmpty() && reviewText.isNotEmpty()) {
                val reviewId = databaseReference.push().key
                val review = Review(placeName, reviewText, rating)

                if (reviewId != null) {
                    databaseReference.child(reviewId).setValue(review)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                finish()
                            } else {
                                // Handle error
                            }
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}