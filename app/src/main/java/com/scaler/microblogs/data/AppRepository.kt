package com.scaler.microblogs.data

interface AppRepository {

    fun viewProfile(token: String)

    fun getTags()

    fun getArticlesListByTag(tag: String)

    fun getFeed()
}