package com.vanluong.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vanluong.search.R

/**
 * Created by van.luong
 * on 07,May,2025
 */
class ShimmerAdapter(private val count: Int = 10) : RecyclerView.Adapter<ShimmerAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val shimmerBox: View = view.findViewById(R.id.shimmerBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_shimmer_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val randomHeight = (300..600).random()
        holder.shimmerBox.layoutParams.height = randomHeight
        holder.shimmerBox.requestLayout()
    }

    override fun getItemCount() = count
}