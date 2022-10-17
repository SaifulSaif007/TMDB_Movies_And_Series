package com.saiful.person.view.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saiful.base.util.ItemDecorator
import com.saiful.base.util.navigation.MovieModuleNavigation
import com.saiful.base.util.navigation.TvShowModuleNavigation
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonDetailsBinding
import com.saiful.person.view.adapter.PersonImageAdapter
import com.saiful.person.view.adapter.PersonMovieAdapter
import com.saiful.person.view.adapter.PersonShowsAdapter
import com.saiful.shared.utils.formatDate
import com.saiful.shared.utils.loadPosterSizeImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentPersonDetailsBinding>() {

    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: PersonDetailsVM by viewModels()
    private val imageAdapter = PersonImageAdapter()
    private val movieAdapter = PersonMovieAdapter(::movieItemClick)
    private val showsAdapter = PersonShowsAdapter(::showsItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    @Inject
    lateinit var movieModuleNavigation: MovieModuleNavigation

    @Inject
    lateinit var tvShowModuleNavigation: TvShowModuleNavigation

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDetailsBinding {
        return FragmentPersonDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_details

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        viewModel.fetchPersonDetails(args.personId)

        bindingView.apply {
            personImageLayout.personImageRecycler.apply {
                adapter = imageAdapter
                addItemDecoration(itemDecorator)
            }
            personMovieLayout.personMovieRecycler.apply {
                adapter = movieAdapter
                addItemDecoration(itemDecorator)
            }
            personShowsLayout.personShowsRecycler.apply {
                adapter = showsAdapter
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.personDetails.collect { details ->
                (activity as AppCompatActivity).supportActionBar?.title = details?.name

                bindingView.apply {
                    personName.text = details?.name
                    personBiography.text = details?.biography
                    knownFor.text =
                        "Also known as: \n" + details?.knowAs?.take(2)?.joinToString(", ")
                    bornOn.text = "Born on: " + details?.birthday?.formatDate()
                    bornIn.text = "Birth place: \n" + details?.birthPlace

                    profileImage.loadPosterSizeImage(details?.profilePath)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.personImageList.collect { images ->
                if (images != null) imageAdapter.submitList(images.profiles)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.personMovieList.collect { movies ->
                if (movies != null) movieAdapter.submitList(movies.cast.sortedByDescending { it.releaseDate })
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.personShowsList.collect { shows ->
                if (shows != null) showsAdapter.submitList(shows.cast)
            }
        }
    }

    private fun movieItemClick(movieId: Int) {
        movieModuleNavigation.navigateMovieDetails(movieId, findNavController())
    }

    private fun showsItemClick(showsId: Int) {
        tvShowModuleNavigation.navigateToShowDetails(showsId, findNavController())
    }
}