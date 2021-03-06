package com.kohuyn.basemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.core.OnItemClick
import com.kohuyn.basemvvm.R
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.databinding.ItemUserBinding
import com.kohuyn.basemvvm.ui.utils.setTextNotNull
import com.kohuyn.basemvvm.ui.utils.show
import com.utils.ext.clickWithDebounce

/**
 * Created by KOHuyn on 2/1/2021
 */
class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var items = mutableListOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: OnItemClick? = null

    inner class UserViewHolder(view: ItemUserBinding) : RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = items[position]
        with(holder) {
            binding.imgAvatarUser.show(item.avatarUrl)
            binding.txtNameUser.setTextNotNull(item.login)
            binding.txtUrlGitUser.setTextNotNull(item.htmlUrl)
        }
        holder.itemView.clickWithDebounce {
            onItemClick?.onItemClickListener(it, position)
        }
    }

    override fun getItemCount(): Int = items.size

}