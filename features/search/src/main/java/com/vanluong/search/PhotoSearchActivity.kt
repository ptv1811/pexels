package com.vanluong.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

            etSearch.apply {
                addTextChangedListener {
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
                                binding.rvPhotos.adapter = shimmerAdapter
                            }

                            is Resource.Success -> {
                                binding.rvPhotos.adapter = searchPhotoAdapter
                                searchPhotoAdapter.submitData(resource.body)
                            }

                            else -> {
                                // Handle error state
                            }
                        }
                    }
                }
            }
        }
    }
}