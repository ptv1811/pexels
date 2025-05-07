package com.vanluong.search

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vanluong.common.BaseFragment
import com.vanluong.common.extensions.hideKeyboard
import com.vanluong.model.Resource
import com.vanluong.search.adapter.SearchPhotoAdapter
import com.vanluong.search.adapter.ShimmerAdapter
import com.vanluong.search.databinding.FragmentPhotoSearchBinding
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
        SearchPhotoAdapter { photo ->
            navigateToDetails()
        }
    }
    private val shimmerAdapter = ShimmerAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observePhotoSearchResult()
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

    private fun navigateToDetails() {
        // We will use the deep link to navigate to the details fragment
        val request = NavDeepLinkRequest.Builder
            .fromUri("android-app://com.vanluong.pexels/details_fragment".toUri())
            .build()

        // Navigation options to add animation
        val navOptions =
            NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build()

        navController.navigate(request, navOptions)
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