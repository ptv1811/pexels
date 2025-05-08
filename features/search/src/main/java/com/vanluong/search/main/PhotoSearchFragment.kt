package com.vanluong.search.main

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vanluong.common.BaseFragment
import com.vanluong.common.extensions.hideKeyboard
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import com.vanluong.search.R
import com.vanluong.search.databinding.FragmentPhotoSearchBinding
import com.vanluong.search.main.adapter.SearchPhotoAdapter
import com.vanluong.search.main.adapter.ShimmerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoSearchFragment :
    BaseFragment<FragmentPhotoSearchBinding>(R.layout.fragment_photo_search) {
    private val photoSearchViewModel: PhotoSearchViewModel by viewModels()
    private val navController by lazy {
        findNavController()
    }

    private val searchPhotoAdapter: SearchPhotoAdapter by lazy {
        SearchPhotoAdapter { photo, photoView ->
            navigateToDetails(photo, photoView)
        }
    }
    private val shimmerAdapter = ShimmerAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observePhotoSearchResult()

        // This is to postpone the enter transition until the layout is drawn.
        // Used by hero animation.
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    /**
     * Initialize the UI components.
     */
    private fun initUI() {
        binding {
            rvPhotos.apply {
                adapter = searchPhotoAdapter
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }

            rvShimmer.apply {
                adapter = shimmerAdapter
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }

            etSearch.apply {
                addTextChangedListener {
                    ivClearSearch.isVisible = it.toString().isNotEmpty()
                    photoSearchViewModel.onQueryChanged(it.toString())
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) root.hideKeyboard()
                }
            }

            ivClearSearch.setOnClickListener {
                etSearch.text?.clear()
                etSearch.requestFocus()
            }
        }

        searchPhotoAdapter.addLoadStateListener { loadState ->
            // This will handle the loading more progress bar at the bottom
            val isLoadingMore =
                loadState.source.append is Loading

            // This will handle the shimmer loading state
            val isInitialLoading =
                loadState.source.refresh is Loading && loadState.source.prepend is NotLoading

            binding {
                rvPhotos.isVisible = !isInitialLoading
                rvShimmer.isVisible = isInitialLoading
                pbLoadMore.isVisible = isLoadingMore
            }

            val errorState = loadState.source.append as? Error
                ?: loadState.source.prepend as? Error
                ?: loadState.append as? Error
                ?: loadState.prepend as? Error

            errorState?.let {
                showError(it)
            }
        }
    }

    /**
     *  Observe the search result from the ViewModel and submit it to the adapter.
     */
    private fun observePhotoSearchResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoSearchViewModel.currentSearchResultStateFlow.collectLatest { resource ->
                    if (resource != null) {
                        when (resource) {
                            is Resource.Loading -> {
                                showLoading()
                            }

                            is Resource.Success -> {
                                hideLoading()
                                binding.rvPhotos.adapter = searchPhotoAdapter
                                searchPhotoAdapter.submitData(resource.body)
                            }

                            else -> {
                                // Error will be handled in the [addLoadStateListener]
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetails(photo: Photo, photoView: View) {
        // Save recent photo to retrieve in [DetailsFragment]
        // So that we don't have to fetch data from network again.
        photoSearchViewModel.saveRecentPhoto(photo)

        val action =
            PhotoSearchFragmentDirections.actionSearchFragmentToDetailsFragment(photo.id.toString())

        // We use shared element to animate the hero effect.
        val extras = FragmentNavigatorExtras(
            photoView to "shared_image_${photo.id}",
        )

        navController.navigate(action, extras)
    }

    private fun showLoading() {
        binding {
            rvPhotos.isVisible = false
            rvShimmer.isVisible = true
        }
    }

    private fun hideLoading() {
        binding {
            rvPhotos.isVisible = true
            rvShimmer.isVisible = false
        }
    }

    private fun showError(error: Error) {
        Snackbar
            .make(binding.root, error.error.message ?: "Unknown Error", Snackbar.LENGTH_LONG)
            .show()
    }
}