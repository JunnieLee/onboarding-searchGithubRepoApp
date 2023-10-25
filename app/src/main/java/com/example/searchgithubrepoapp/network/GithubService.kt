package com.example.searchgithubrepoapp.network

import com.example.searchgithubrepoapp.model.Repo
import com.example.searchgithubrepoapp.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    // @Headers("Authorization: Bearer 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")
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String, @Query("page") page:Int): Call<List<Repo>>

    // @Headers("Authorization: Bearer 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")
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<UserDto>
}