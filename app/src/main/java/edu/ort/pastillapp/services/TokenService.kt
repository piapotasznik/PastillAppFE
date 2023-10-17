package edu.ort.pastillapp.services

import edu.ort.pastillapp.models.ApiTokenResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import edu.ort.pastillapp.models.Token
import retrofit2.Call
import retrofit2.http.Body

interface TokenService {

    @POST("api/Token")
    fun createToken(@Body token: Token): Call<Void>

    @DELETE("api/Token")
    fun deleteToken(@Path("tokenId") tokenId: Int): Call<Void>

    @GET("api/Token")
    fun getTokens(): Call<List<Token>>

    @GET("api/Token/{email}")
    fun getUserEmail(@Path("email") email: String): Call<ApiTokenResponse>

}