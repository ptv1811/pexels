package com.vanluong.search.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vanluong.domain.FetchCuratedPhotoUseCase
import com.vanluong.domain.SaveRecentPhotoUseCase
import com.vanluong.domain.SearchPhotoUseCase
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by van.luong
 * on 07,May,2025
 */
@HiltViewModel
class PhotoSearchViewModel @Inject constructor(
    private val searchPhotoUseCase: SearchPhotoUseCase,
    private val curatedPhotoUseCase: FetchCuratedPhotoUseCase,
    private val saveRecentPhotoUseCase: SaveRecentPhotoUseCase,
) : ViewModel() {
    private val _currentQuery = MutableStateFlow("")

    // Memory cache for the curated photo list, because next time user open the app
    // we don't want user to see old curated photo list
    private var _curatedPhotoList: PagingData<Photo>? = null

    private var _currentPhotoList: MutableStateFlow<Resource<PagingData<Photo>>?> =
        MutableStateFlow(null)
    val currentSearchResultStateFlow = _currentPhotoList.asStateFlow()


    init {
        observeSearchQuery()
    }

    fun onQueryChanged(query: String) = viewModelScope.launch {
        _currentQuery.value = query
    }

    fun saveRecentPhoto(photo: Photo) = viewModelScope.launch {
        saveRecentPhotoUseCase(photo)
    }

    /*
        * Observe the search query and trigger the search when it changes.
        * This allows the app to display data on user typing but does not firing API request immediately.
     */
    private fun observeSearchQuery() = viewModelScope.launch {
        _currentQuery
            .debounce(500) // Wait 500ms after last input
            .distinctUntilChanged() // Only trigger search if query is different
            .collectLatest { query ->
                if (query.isEmpty()) {
                    // If there is no query, we want to show the curated photo list
                    fetchCuratedPhotos()
                    return@collectLatest
                }
                _currentPhotoList.value = Resource.Loading

                searchPhotoUseCase(query).cachedIn(viewModelScope).collectLatest {
                    _currentPhotoList.value = Resource.Success(it)
                }
            }
    }

    private fun fetchCuratedPhotos() = viewModelScope.launch {
        _currentPhotoList.value = Resource.Loading
        curatedPhotoUseCase().cachedIn(viewModelScope).collectLatest {
            if (_curatedPhotoList == null) {
                _curatedPhotoList = it
            }
            _currentPhotoList.value = Resource.Success(_curatedPhotoList!!)
        }
    }
}