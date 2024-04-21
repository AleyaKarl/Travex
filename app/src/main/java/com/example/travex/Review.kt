package com.example.travex

data class Review(val placeName: String, val description: String, val rating: Float) {
    constructor() : this("", "", 0.0f) // Empty constructor required for Firebase
}

