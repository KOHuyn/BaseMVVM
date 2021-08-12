package com.kohuyn.basemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.core.BaseViewHolder
import com.core.OnItemClick
import com.core.adapter.BaseAdapter
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.databinding.ItemUserBinding
import com.kohuyn.basemvvm.ui.utils.show
import com.utils.ext.clickWithDebounce
import com.utils.ext.setTextNotNull

/**
 * Created by KOHuyn on 2/1/2021
 */
class UserAdapter : BaseAdapter<ItemUserBinding>() {

    var onItemClick: OnItemClick<User>? = null
    override fun getItemCount(): Int = items.size
    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindChildViewHolder(holder: BaseViewHolder<ItemUserBinding>, position: Int) {
        val item = items[position] as User
        with(holder) {
            binding.imgAvatarUser.show(item.avatarUrl)
            binding.txtNameUser.setTextNotNull(item.login)
            binding.txtUrlGitUser.setTextNotNull(item.htmlUrl)
        }
        holder.itemView.clickWithDebounce {
            onItemClick?.onItemClickListener(item, position)
        }
    }

}