package com.kuricki.keyquest.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.UserSession
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL = "https://keyquest.kuricki.com/api/" //api base url

//Retrofit instance
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface KeyQuestApiService {
    @GET("levels") //Get available levels from api, requires token
    suspend fun getLevels(@Header("Authorization") authorization: String): MutableList<GameLevel>

    @POST("levels/score") //Submit level score to api, requires token")
    suspend fun submitLevelScore(@Header("Authorization") authorization: String, @Body b: JsonObject): JsonObject

    @POST("auth/login") //Get login session from api
    suspend fun getLoginSession(@Body b: JsonObject): UserSession

    @GET("auth/validate")
    suspend fun validateSession(@Header("Authorization") authorization: String): UserSession

    @POST("auth/register")
    suspend fun registerUser(@Body b: JsonObject): UserSession
}

object KeyQuestApi {
    //expose retrofit service
    val retrofitService : KeyQuestApiService by lazy {
        retrofit.create(KeyQuestApiService::class.java) }

    private var token: String = ""

    fun setToken(t: String) {
        token = t
    }

    fun getToken(): String {
        return token
    }

}