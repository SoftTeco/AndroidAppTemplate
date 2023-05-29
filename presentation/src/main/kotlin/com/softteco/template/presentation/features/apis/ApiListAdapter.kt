package com.softteco.template.presentation.features.apis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.presentation.R
import com.softteco.template.presentation.databinding.ItemApiEntryBinding
import com.softteco.template.presentation.extensions.setOnSafeClickListener

class ApiListAdapter(
    private val items: List<ApiEntry>,
    private val onClick: (position: Int) -> Unit,
    private val onClickFavorite: (position: Int) -> Unit,
) : RecyclerView.Adapter<ApiEntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiEntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemApiEntryBinding.inflate(layoutInflater, parent, false)
        val holder = ApiEntryViewHolder(binding)
        binding.run {
            cvApiItem.setOnSafeClickListener {
                onClick(holder.bindingAdapterPosition)
            }
            ivFavorite.setOnSafeClickListener {
                onClickFavorite(holder.bindingAdapterPosition)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ApiEntryViewHolder, position: Int) {
        val item = items[position]
        holder.binding.run {
            tvTitle.text = item.name
            ivApiLogo.load(item.logo)
            val favoriteRes = if (item.favorite) {
                R.drawable.ic_favorite_filled_white
            } else {
                R.drawable.ic_favorite_outline_white
            }
            ivFavorite.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.context, favoriteRes))
        }
    }

    override fun getItemCount(): Int = items.size
}

class ApiEntryViewHolder(val binding: ItemApiEntryBinding) : RecyclerView.ViewHolder(binding.root)
