package com.scaler.microblogs.data

import android.util.Log
import com.scaler.libconduit.apis.ConduitApi
import com.scaler.libconduit.responses.MultipleArticleResponse
import com.scaler.libconduit.responses.UserResponse
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Exception

class AppRepositoryImpl(
    private val service: ConduitApi
): AppRepository {

    private val _profileStatus: MutableStateFlow<Resource<UserResponse>> = MutableStateFlow(Resource.Loading())
    val profileStatus: StateFlow<Resource<UserResponse>> get() = _profileStatus

    private val _tagStatus: MutableStateFlow<Resource<List<String>?>> = MutableStateFlow(Resource.Loading())
    val tagStatus: StateFlow<Resource<List<String>?>> get() = _tagStatus

    private val _tagDetails: MutableStateFlow<Resource<MultipleArticleResponse>> = MutableStateFlow(Resource.Loading())
    val tagDetails: StateFlow<Resource<MultipleArticleResponse>> get() = _tagDetails

    private val _feeds: MutableStateFlow<Resource<MultipleArticleResponse>> = MutableStateFlow(Resource.Loading())
    val feeds: StateFlow<Resource<MultipleArticleResponse>> get() = _feeds

    override fun viewProfile(token: String) {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = service.getCurrentUser("Token " + token)
                _profileStatus.value = Resource.Success(response)

            } catch (e: Exception) {

                _profileStatus.value = Resource.Error("An unknown error occurred!")
            }
        }
    }

    override fun getTags() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = service.getTags()
                _tagStatus.value = Resource.Success(response.tags)

            } catch (e: Exception) {

                _tagStatus.value = Resource.Error("An unknown error occurred!")
            }
        }
    }

    override fun getArticlesListByTag(tag: String) {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = service.getArticlesListByTag(tag)
                _tagDetails.value = Resource.Success(response)

            } catch (e: Exception) {

                _tagDetails.value = Resource.Error("An unknown error occurred!")
            }
        }
    }

    override fun getFeed() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = service.getFeedArticles()
                _feeds.value = Resource.Success(response)

            } catch (e: Exception) {

                _feeds.value = Resource.Error("An unknown error occurred!")
            }
        }
    }
}