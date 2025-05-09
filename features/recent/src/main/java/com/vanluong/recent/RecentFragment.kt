package com.vanluong.recent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vanluong.common.BaseFragment
import com.vanluong.model.Resource
import com.vanluong.recent.adapter.RecentPhotoAdapter
import com.vanluong.recent.databinding.FragmentRecentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecentFragment : BaseFragment<FragmentRecentBinding>(R.layout.fragment_recent) {
    private val recentViewModel: RecentViewModel by viewModels()
    private val recentPhotoAdapter: RecentPhotoAdapter by lazy {
        RecentPhotoAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeRecentPhotoResult()
    }

    private fun initUI() {
        binding {
            rvPhotos.apply {
                setHasFixedSize(true)
                adapter = recentPhotoAdapter
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        }
    }

    private fun observeRecentPhotoResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recentViewModel.recentPhotoListStateFlow.collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            recentPhotoAdapter.submitData(resource.body)
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }

    // Load new recent everytime user comes back to this screen
    override fun onResume() {
        super.onResume()
        recentViewModel.observeRecentSearchCollecting()
    }
}