package com.vanluong.search.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vanluong.common.BaseFragment
import com.vanluong.common.extensions.changeColorAtRange
import com.vanluong.search.R
import com.vanluong.search.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(R.layout.fragment_details) {
    private val args: DetailsFragmentArgs by navArgs()
    private val navController by lazy {
        findNavController()
    }
    private val detailsViewModel: DetailsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition();
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observePhotoDetails()
    }

    private fun observePhotoDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.selectedPhotoStateFlow.collect { photo ->
                    if (photo != null) {
                        binding {
                            // Load image with Glide
                            loadImageUsingGlide(photo.url)

                            val photographerString =
                                getString(R.string.photographer_name, photo.photographer)
                            tvPhotographer.text = photographerString.changeColorAtRange(
                                photographerString.indexOf("Photographer") + "Photographer".length..photographerString.length
                                        to resources.getColor(R.color.white, null)
                            )
                            tvAlt.text = photo.alt
                        }
                    }
                }
            }
        }
    }

    private fun loadImageUsingGlide(photoUrl: String) {
        val requestBuilder: RequestBuilder<Drawable> =
            Glide.with(requireContext())
                .asDrawable().sizeMultiplier(0.25f)
        Glide.with(requireContext())
            .load(photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .thumbnail(requestBuilder)
            .fitCenter()
            .into(binding.ivDetailsImage)
    }

    private fun initUI() {
        binding {
            val photoId = args.photoId
            val transitionName = "shared_image_$photoId"
            ivDetailsImage.transitionName = transitionName
            detailsViewModel.getRecentPhoto(photoId)

            ivBack.setOnClickListener {
                navController.navigateUp()
            }
        }
    }

    private fun prepareSharedElementTransition() {
        val transition: Transition? =
            TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image)

        sharedElementEnterTransition = transition
    }
}