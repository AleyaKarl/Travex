package com.example.travex


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class placeAdapter(private val places: List<Place>, private val listener: OnItemClickListener) : RecyclerView.Adapter<placeAdapter.PlaceViewHolder>(), Filterable {

    private var filteredPlace: List<Place> = places
    // Interface for click listener


    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTextView: TextView = itemView.findViewById(R.id.tVplaceName)
        private val imageView: ImageView = itemView.findViewById(R.id.iVplaceImage)
        //private val detailsTextView: TextView = itemView.findViewById(R.id.tVplaceDetails)
        private lateinit var place: Place

        init {
            // Set click listener for the item view
            itemView.setOnClickListener(this)
        }

        fun bind(place: Place) {
            this.place = place
            nameTextView.text = place.placeName
            //detailsTextView.text = place.placeDetails
            // Load image into ImageView using Glide
            Glide.with(itemView.context).load(place.placeImage).into(imageView)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(filteredPlace[position])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(filteredPlace[position])
    }

    override fun getItemCount(): Int {
        return filteredPlace.size
    }

    interface OnItemClickListener {
        fun onItemClick(place: Place)
    }

    // Filter implementation
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Place>()
                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(places)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (place in places) {
                        if (place.placeName.lowercase().contains(filterPattern)) {
                            filteredList.add(place)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPlace = results?.values as List<Place>
                notifyDataSetChanged()
            }
        }
    }
}