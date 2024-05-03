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
    @JsonProperty("id") var id: Int,
    @JsonProperty("displayName") var displayName: String,
    @JsonProperty("difficulty") var difficulty: Int,
    @JsonProperty("currentScore") var currentScore: Int,
    @JsonProperty("bestScore") var bestScore: Int,
)

object KeyQuestApi {
    private const val BASE_URL = "keyquest.kuricki.com"
    private var token:String = "undefined" //api auth token

    object RequestInterceptor : Interceptor {
        //intercepts all requests, useful to add headers to all requests
        override fun intercept(chain: Interceptor.Chain): Response {
            //add auth token to all requests
            val request = chain.request().newBuilder()
                .addHeader("Authorization", token)
            return chain.proceed(request.build())
        }
    }

    private val okHttpClient = OkHttpClient()
        //Used to add interceptor
        .newBuilder()
        .addInterceptor(RequestInterceptor)
        .build()

    //retrofit client
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

interface levels {
    @GET("levels") //Get available levels from api
    fun getLevels(): Call<MutableList<Level>>

}

interface authentication {
    @GET("login") //Login user
    fun login(): Call<LoginSession>
}