package com.saiful.shared.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.R
import com.saiful.shared.databinding.FragmentGalleryBinding
import com.saiful.shared.model.Image
import com.saiful.shared.utils.JsonConverter
import com.saiful.shared.view.adapter.GalleryPagerAdapter

class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    private val args: GalleryFragmentArgs by navArgs()

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGalleryBinding {
        return FragmentGalleryBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_gallery

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        val images = JsonConverter.fromJsonList<Image>(args.images)
        val startAt = args.startIndex

        bindingView.apply {
            viewPager.adapter = GalleryPagerAdapter(images!!)
            viewPager.setCurrentItem(startAt, true)
        }

    }
}