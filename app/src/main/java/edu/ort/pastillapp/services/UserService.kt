package edu.ort.pastillapp.services
import edu.ort.pastillapp.models.ApiResponse
import edu.ort.pastillapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

      // profe fun getUser(): Call<ApiResponse<User>>
      @GET("api/User/{userId}")
      fun getUserId(@Path("userId") userId: Int): Call<ApiResponse>
      @GET("api/User/email/user")
      fun getUserEmail(@Path("email") email: String): Call<ApiResponse>
      @GET("api/User")
      fun getUsers(): Call<List<ApiResponse>>
      @DELETE("api/User/{userId}")
      fun deleteUserId(@Path("userId") userId: Int): Call<Void>
      @POST("api/User")
      fun createUser(@Body user: User): Call<Void>
     // Call<ApiResponse> saveUser(@Body usuario: User)
    //   @PUT("api/User/{dailyStatusId}")
    //  fun updateDailyStatus(@Path("dailyStatusId") dailyStatusId: Int, @Body estadoDiario: DailyStatus): Call<Void>
}
