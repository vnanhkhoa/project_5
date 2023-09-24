package com.khoavna.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khoavna.politicalpreparedness.databinding.ItemElectionBinding
import com.khoavna.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(diffUtil) {

    inner class ElectionViewHolder(private val binding: ItemElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ElectionListener, item: Election) {
            binding.election = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder(
            ItemElectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        getItem(position)?.also {
            holder.bind(clickListener, it)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Election>() {
            override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

interface ElectionListener {
    fun onItemClick(election: Election)
}
