package com.vanluong.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vanluong.domain.GetAllRecentPhotosUseCase
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by van.luong
 * on 09,May,2025
 */
@HiltViewModel
class RecentViewModel @Inject constructor(
    private val getAllRecentPhotosUseCase: GetAllRecentPhotosUseCase
) : ViewModel() {
    private var _recentPhotoList: MutableStateFlow<Resource<PagingData<Photo>>?> =
        MutableStateFlow(null)
    val recentPhotoListStateFlow = _recentPhotoList.asStateFlow()

    fun observeRecentSearchCollecting() = viewModelScope.launch {
        getAllRecentPhotosUseCase().cachedIn(viewModelScope).collectLatest {
            _recentPhotoList.value = Resource.Success(it)
        }
    }
}