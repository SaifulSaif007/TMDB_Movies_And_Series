package com.saiful.movie.view.collection

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieCollectionBinding
import com.saiful.movie.view.adapter.CollectionAdapter
import com.saiful.shared.utils.loadBackDropSizeImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class CollectionFragment : BaseFragment<FragmentMovieCollectionBinding>() {

    private val args: CollectionFragmentArgs by navArgs()
    private val viewModel: CollectionVM by viewModels()
    private val collectionAdapter = CollectionAdapter(::movieItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieCollectionBinding {
        return FragmentMovieCollectionBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_collection

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        viewModel.fetchCollections(args.collectionId)

        bindingView.collectionRecycler.apply {
            adapter = collectionAdapter
            addItemDecoration(itemDecorator)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.collections.collect { collection ->

                bindingView.apply {
                    collectionName.text = collection?.name
                    collectionOverview.text = collection?.overview
                    toolbar.title = collection?.name
                    backdropImage.loadBackDropSizeImage(collection?.backdropPath)
                    posterImage.loadBackDropSizeImage(collection?.posterPath)
                }

                collection?.parts?.let { movie ->
                    collectionAdapter.submitList(movie)
                }
            }
        }
    }

    private fun movieItemClick(id: Int) {
        findNavController().navigate(
            R.id.movie_details_nav_graph, bundleOf("movie_id" to id)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingView.collapsingAppbar.setExpandedTitleColor(Color.TRANSPARENT)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        bindingView.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}