package com.mexator.petfoodinspector.ui.recycler.base

/**
 * @author Anton Brisilin a.brisilin@innopolis.university
 */

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Base class for all ViewHolders
 */
abstract class BaseViewHolder<in T : ViewTyped>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    /**
     * Changes views in this ViewHolder so that it displays [item]
     */
    abstract fun bind(item: T)
}

/**
 * Base class for all adapters for RecyclerView. Items are submitted to [items].
 * It has built-in AsyncListDiffer, so you shouldn't care about it. Though, you need
 * either override [equals] for type [T] or use `data class` as item to be displayed
 * to make diff'ing list work properly.
 *
 * ...or you can simply override [differ]
 */
open class BaseAdapter<T : ViewTyped>(private val holderFactory: BaseHolderFactory) :
    RecyclerView.Adapter<BaseViewHolder<ViewTyped>>() {

    private val diffCallback: DiffUtil.ItemCallback<ViewTyped> = object :
        DiffUtil.ItemCallback<ViewTyped>() {

        override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
            return oldItem.viewType == newItem.viewType && oldItem.uid == newItem.uid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
            return oldItem == newItem
        }

    }

    open val differ: AsyncListDiffer<ViewTyped> by lazy {
        AsyncListDiffer(this, diffCallback)
    }

    var items: List<ViewTyped>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> =
        holderFactory.create(parent, viewType)


    override fun onBindViewHolder(holder: BaseViewHolder<ViewTyped>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
    override fun getItemViewType(position: Int): Int = items[position].viewType
}