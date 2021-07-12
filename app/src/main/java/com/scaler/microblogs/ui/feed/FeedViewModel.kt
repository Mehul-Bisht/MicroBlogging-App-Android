package com.scaler.microblogs.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scaler.libconduit.responses.MultipleArticleResponse
import com.scaler.microblogs.data.AppRepositoryImpl
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedViewModel(
    private val service: AppRepositoryImpl
) : ViewModel() {

    private val _feedState: MutableStateFlow<FeedState> = MutableStateFlow(FeedState.Initial)
    val feedState: StateFlow<FeedState> get() = _feedState

    sealed class FeedState {

        object Initial: FeedState()
        object Loading: FeedState()
        data class Success(val data: MultipleArticleResponse?): FeedState()
        object Error: FeedState()
    }

    fun getFeed() {

        service.getFeed()

        viewModelScope.launch {

            service.feeds.collect { state ->

                when(state) {

                    is Resource.Loading -> {

                        _feedState.value = FeedState.Loading
                    }
                    is Resource.Success -> {

                        _feedState.value = FeedState.Success(state.data)
                    }
                    is Resource.Error -> {

                        _feedState.value = FeedState.Error
                    }
                }
            }
        }
    }
}