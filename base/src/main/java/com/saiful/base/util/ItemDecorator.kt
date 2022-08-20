package com.saiful.base.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import javax.inject.Singleton

//TODO -> Make runtime injection by using interface (@AssistedFactory)
@Singleton
class ItemDecorator
@Inject constructor() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(15, 8, 15, 8)
    }
}