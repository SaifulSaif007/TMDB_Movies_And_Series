package com.saiful.person.view.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonDetailsBinding
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentPersonDetailsBinding>() {

    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: PersonDetailsVM by viewModels()

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

        lifecycleScope.launchWhenStarted {
            viewModel.personDetails.collect { details ->
                (activity as AppCompatActivity).supportActionBar?.title = details?.name

                bindingView.apply {
                    personName.text = details?.name
                    personBiography.text = details?.biography
                    knownFor.text = "Also known as: \n" +  details?.knowAs?.let { it.take(2).joinToString(", ") }
                    bornOn.text = "Born on: " +  details?.birthday?.formatDate()
                    bornIn.text = "Birth place: \n" + details?.birthPlace

                    Glide.with(bindingView.root.context)
                        .load(AppConstants.imageBaseUrl + AppConstants.posterSize + details?.profilePath)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        //.error(R.drawable.image1)
                        .into(bindingView.profileImage)
                }
            }
        }


    }

}