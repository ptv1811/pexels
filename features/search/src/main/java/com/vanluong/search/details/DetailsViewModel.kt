package com.vanluong.search.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanluong.domain.GetRecentPhotoUseCase
import com.vanluong.model.Photo
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
class DetailsViewModel @Inject constructor(
    private val getRecentPhotoUseCase: GetRecentPhotoUseCase
) : ViewModel() {

    private val _selectedPhotoStateFlow: MutableStateFlow<Photo?> = MutableStateFlow(null)
    val selectedPhotoStateFlow = _selectedPhotoStateFlow.asStateFlow()

    fun getRecentPhoto(id: Long) = viewModelScope.launch {
        getRecentPhotoUseCase(id).collectLatest { photo ->
            _selectedPhotoStateFlow.value = photo
        }
    }
}