package com.kuricki.keyquest.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Data class for login session
 */
@Serializable
data class LoginSession (
    @JsonProperty("token") val token: String,
    @JsonProperty("id") val id: Int,
    @JsonProperty("username") val username: String,
    @JsonProperty("tokenExpire") val tokenExpire: Long,
)

/**
 * Data class for game level
 */
@Serializable
data class GameLevel (
    @JsonProperty("id") var id: Int,
    @JsonProperty("displayName") var displayName: String,
    @JsonProperty("difficulty") var difficulty: Int,
    @JsonProperty("currentScore") var currentScore: Int,
    @JsonProperty("bestScore") var bestScore: Int,
)

private const val BASE_URL = "https://keyquest.kuricki.com/api/" //api base url

//Retrofit instance
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface KeyQuestApiService {
    @GET("levels") //Get available levels from api, requires token
    suspend fun getLevels(@Header("Authorization") authorization: String): MutableList<GameLevel>

    @POST("auth/login") //Get login session from api
    suspend fun getLoginSession(@Body b: JsonObject): LoginSession
}

object KeyQuestApi {
    //expose retrofit service
    val retrofitService : KeyQuestApiService by lazy {
        retrofit.create(KeyQuestApiService::class.java) }
}