package com.vanluong.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vanluong.domain.SearchPhotoUseCase
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by van.luong
 * on 07,May,2025
 */
@HiltViewModel
class PhotoSearchViewModel @Inject constructor(
    private val searchPhotoUseCase: SearchPhotoUseCase
) : ViewModel() {
    private val _currentQuery = MutableStateFlow("")
    private var _currentSearchResult: MutableStateFlow<Resource<PagingData<Photo>>?> =
        MutableStateFlow(null)
    val currentSearchResultStateFlow = _currentSearchResult.asStateFlow()

    init {
        observeSearchQuery()
    }

    /*
        * Observe the search query and trigger the search when it changes.
        * This allows the app to display data on user typing but does not firing API request immediately.
     */
    private fun observeSearchQuery() = viewModelScope.launch {
        _currentQuery
            .debounce(500) // Wait 500ms after last input
            .filter { it.isNotBlank() } // Skip empty query
            .distinctUntilChanged() // Only trigger search if query is different
            .collectLatest { query ->
                // clear the previous search result
                searchPhotoUseCase(query).cachedIn(viewModelScope).collectLatest {
                    _currentSearchResult.value = Resource.Success(it)
                }
            }
    }

    fun onQueryChanged(query: String) = viewModelScope.launch {
        _currentSearchResult.emit(Resource.Loading)
        _currentQuery.value = query
    }
}