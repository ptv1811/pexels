package com.vanluong.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vanluong.common.BaseActivity
import com.vanluong.common.extensions.hideKeyboard
import com.vanluong.model.Resource
import com.vanluong.search.adapter.SearchPhotoAdapter
import com.vanluong.search.adapter.ShimmerAdapter
import com.vanluong.search.databinding.ActivityPhotoSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoSearchActivity :
    BaseActivity<ActivityPhotoSearchBinding>(R.layout.activity_photo_search) {
    private val photoSearchViewModel: PhotoSearchViewModel by viewModels()
    private val searchPhotoAdapter: SearchPhotoAdapter by lazy {
        SearchPhotoAdapter { photo ->
            // Handle photo click
        }
    }
    private val shimmerAdapter = ShimmerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
        lifecycleScope.launch {
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