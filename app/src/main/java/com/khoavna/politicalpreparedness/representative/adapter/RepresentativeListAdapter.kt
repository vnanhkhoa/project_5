package com.khoavna.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khoavna.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.khoavna.politicalpreparedness.network.models.Channel
import com.khoavna.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter :
    ListAdapter<Representative, RepresentativeListAdapter.RepresentativeViewHolder>(diffUtil) {

    inner class RepresentativeViewHolder(private val binding: ItemRepresentativeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Representative) {
            binding.representative = item
            item.official.channels?.let { showSocialLinks(it) }
            item.official.urls?.let { showWWWLinks(it) }
            binding.executePendingBindings()
        }

        private fun showSocialLinks(channels: List<Channel>) {
            val facebookUrl = getFacebookUrl(channels)
            if (!facebookUrl.isNullOrBlank()) {
                enableLink(binding.imgFacebook, facebookUrl)
            }

            val twitterUrl = getTwitterUrl(channels)
            if (!twitterUrl.isNullOrBlank()) {
                enableLink(binding.imgTwitter, twitterUrl)
            }
        }

        private fun showWWWLinks(urls: List<String>) {
            enableLink(binding.imgWeb, urls.first())
        }

        private fun getFacebookUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
        }

        private fun getTwitterUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
        }

        private fun enableLink(view: ImageView, url: String) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { setIntent(url) }
        }

        private fun setIntent(url: String) {
            val uri = Uri.parse(url)
            val intent = Intent(ACTION_VIEW, uri)
            itemView.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder(
            ItemRepresentativeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Representative>() {
            override fun areItemsTheSame(
                oldItem: Representative,
                newItem: Representative
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Representative,
                newItem: Representative
            ): Boolean {
                return oldItem.office.division.id == newItem.office.division.id
            }
        }
    }
}
