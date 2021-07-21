package com.kohuyn.basemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.core.BaseViewHolder
import com.core.OnItemClick
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.databinding.ItemUserBinding
import com.kohuyn.basemvvm.ui.utils.show
import com.utils.ext.clickWithDebounce
import com.utils.ext.setTextNotNull

/**
 * Created by KOHuyn on 2/1/2021
 */
class UserAdapter : RecyclerView.Adapter<BaseViewHolder<ItemUserBinding>>() {

    var items = mutableListOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: OnItemClick<User>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemUserBinding> {
        return BaseViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemUserBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.imgAvatarUser.show(item.avatarUrl)
            binding.txtNameUser.setTextNotNull(item.login)
            binding.txtUrlGitUser.setTextNotNull(item.htmlUrl)
        }
        holder.itemView.clickWithDebounce {
            onItemClick?.onItemClickListener(item, position)
        }
    }

    override fun getItemCount(): Int = items.size

}