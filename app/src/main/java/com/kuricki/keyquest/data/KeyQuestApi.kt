package com.kuricki.keyquest.data

import com.fasterxml.jackson.annotation.JsonProperty
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET

data class LoginSession (
    @JsonProperty("token") val token: String,
    @JsonProperty("id") val id: Int,
    @JsonProperty("usrName") val userName: String,
)

data class Level (
    @JsonProperty("id") val id: Int,
    @JsonProperty("displayName") val displayName: String,
    @JsonProperty("difficulty") val userName: Int,
    @JsonProperty("currentScore") val currentScore: Int,
    @JsonProperty("bestScore") val bestScore: String,
)

object KeyQuestApi {
    private const val BASE_URL = "keyquest.kuricki.com"
    private var token:String = "undefined"

    object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            //add auth token to all requests
            val request = chain.request().newBuilder()
                .addHeader("Authorization", token)
            return chain.proceed(request.build())
        }
    }

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(RequestInterceptor)
        .build()

    fun getClient():Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

    fun setToken(newToken:String){
        token = newToken
    }
}

interface LevelsApi {
    @GET("levels")
    fun getLevels(): Call<List<Level>>

    @GET("login")
    fun login(): Call<LoginSession>
}