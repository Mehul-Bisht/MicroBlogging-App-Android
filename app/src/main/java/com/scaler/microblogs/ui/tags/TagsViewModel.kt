package com.scaler.microblogs.ui.tags

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scaler.libconduit.responses.MultipleArticleResponse
import com.scaler.microblogs.data.AppRepositoryImpl
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TagsViewModel(
    private val service: AppRepositoryImpl
) : ViewModel() {

    private val _tags: MutableStateFlow<TagsState> = MutableStateFlow(TagsState.Initial)
    val tags: StateFlow<TagsState> get() = _tags

    private val _searchedTag: MutableStateFlow<String> = MutableStateFlow("")

    private val _currentTagDetail: MutableStateFlow<TagsDetailState> = MutableStateFlow(TagsDetailState.Initial)
    val currentTagDetail: StateFlow<TagsDetailState> get() = _currentTagDetail

    sealed class TagsState {

        object Initial: TagsState()
        object Loading: TagsState()
        data class Success(val tags: List<String>?): TagsState()
        object Error: TagsState()
    }

    sealed class TagsDetailState {

        object Initial: TagsDetailState()
        object Loading: TagsDetailState()
        data class Success(val tagDetails: MultipleArticleResponse?): TagsDetailState()
        object Error: TagsDetailState()
    }

    @ExperimentalCoroutinesApi
    val tagDetails = _searchedTag.flatMapLatest {

        service.getArticlesListByTag(it)

        flow<MultipleArticleResponse?> {

            service.tagDetails.collectLatest{

                when(it) {

                    is Resource.Success -> {

                        emit(it.data)
                    }
                }
            }
        }

        //service.tagDetails
    }

    fun searchByTag(tag: String) {

        _searchedTag.value = tag
    }

    fun searchTag(tag: String) {

        service.getArticlesListByTag(tag)

        viewModelScope.launch {

            service.tagDetails.collectLatest{

                when(it) {

                    is Resource.Loading -> {

                        _currentTagDetail.value = TagsDetailState.Loading
                    }
                    is Resource.Success -> {

                        _currentTagDetail.value = TagsDetailState.Success(it.data)
                    }
                    is Resource.Error -> {

                        _currentTagDetail.value = TagsDetailState.Error
                    }
                }
            }
        }
    }

    fun getTags() {

        service.getTags()

        viewModelScope.launch {

            service.tagStatus.collect { state ->

                when(state) {

                    is Resource.Loading -> {

                        _tags.value = TagsState.Loading
                    }
                    is Resource.Error -> {

                        _tags.value = TagsState.Error
                    }
                    is Resource.Success -> {

                        val notBlankList = state.data?.filterNot {
                            it.startsWith("\u200c") || it.endsWith("\u200c")
                        }

                        _tags.value = TagsState.Success(notBlankList)
                    }
                }
            }
        }
    }

    fun getComments() {}
}