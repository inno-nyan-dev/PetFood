package com.mexator.petfoodinspector.ui.recycler.base

/**
 * @author Anton Brisilin a.brisilin@innopolis.university
 */

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * This class is used by adapters to create ViewHolders for Recycler View
 */
abstract class BaseHolderFactory {
    /**
     * This function can should be overridden by children to
     * give factory an ability to create some custom ViewHolders
     *
     * @param parent ViewGroup at which created ViewHolder will be attached
     * @param viewType type of ViewHolder to be created
     *
     * @return null, if the factory cannot create ViewHolder of given [viewType],
     * or created ViewHolder
     */
    protected abstract fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*>?

    /**
     * Use it to create ViewHolders
     * @param parent ViewGroup at which created ViewHolder will be attached
     * @param viewType type of created ViewHolder
     *
     * @throws
     */
    fun create(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return checkNotNull(createViewHolder(view as ViewGroup, viewType)) {
            val type = try {
                parent.resources.getResourceName(viewType)
            } catch (ex: Resources.NotFoundException) {
                viewType.toString()
            }
            "unknown viewType $type"
        } as BaseViewHolder<ViewTyped>
    }
}