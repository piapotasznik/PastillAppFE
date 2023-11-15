package edu.ort.pastillapp.Services

import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.Token
import edu.ort.pastillapp.Models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import edu.ort.pastillapp.Models.ApiContactEmergencyList
import edu.ort.pastillapp.Models.ApiContactEmergencyRequest
import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.Models.ApiEmergencyContactResponseDTO

interface UserService {
    @GET("api/User/{userId}")
    fun getUserId(@Path("userId") userId: Int): Call<ApiUserResponse>

    @GET("api/User/{email}/user")
    fun getUserEmail(@Path("email") email: String): Call<ApiUserResponse>

    @GET("api/User")
    fun getUsers(): Call<List<ApiUserResponse>>

    @DELETE("api/User/{userId}")
    fun deleteUserId(@Path("userId") userId: Int): Call<Void>

    @POST("api/User")
    fun createUser(@Body user: User): Call<Void>

    @PUT("api/User/{userId}")
    fun updateUser(@Path("userId") userId: Int, @Body user: User): Call<Void>

    @POST("api/Token")
    fun sendTokenToServer(@Body token: Token): Call<Void>

    @POST("api/User/contact-emergency")
    fun contactEmergency(@Body contactEmergencyRequest: ApiContactEmergencyRequest): Call<Void>

    @GET("api/User/contact-emergency/request")
    fun getEmergencyRequests(@Query("userMail") userMail: String): Call<ApiContactEmergencyList>

    @POST("api/User/contact-emergency/request")
    fun sendEmergencyRequestResponse(@Body request: ApiEmergencyContactResponseDTO): Call<ApiContactEmergencyServerResponse>

    @POST("api/User/SendEmergencyMessage")
    fun sendEmergencyMessage(@Query("userMail") userMail: String): Call<ApiContactEmergencyServerResponse>
    @DELETE("api/User/DeleteEmergencyContact")
    fun deleteEmergencyContact(@Query("userMail") userMail: String): Call<Void>
}
