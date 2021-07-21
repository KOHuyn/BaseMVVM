package com.kohuyn.basemvvm.ui.repo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kohuyn.basemvvm.data.model.Repository
import com.kohuyn.basemvvm.databinding.ItemRepositoryBinding
import com.utils.ext.setTextNotNull

/**
 * Created by KOHuyn on 2/1/2021
 */
class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    var items = mutableListOf<Repository>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class RepositoryViewHolder(view: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            txtNameRepository.setTextNotNull(item.name)
            txtDescription.setTextNotNull(item.description)
            txtStar.setTextNotNull(item.stargazersCount?.toString())
            txtForked.setTextNotNull(item.forksCount?.toString())
            txtUpdateAt.setTextNotNull(item.getTime())
        }
    }

    override fun getItemCount(): Int = items.size
}