package com.example.locaweb.integrations

import android.content.Context
import com.example.locaweb.interfaces.IEmail
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body() body: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body() body: RegisterRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<LoginResponse>

    @GET("user/info")
    suspend fun userInfo(): Response<UserInfoResponse>

    @PUT("user/preferences")
    suspend fun updateInfo(@Body() body: UserPreferences): Response<LoginResponse>

    @GET("email")
    suspend fun getEmails(
        @Query("date") date: String? = null,
        @Query("markers") markers: String? = null,
        @Query("read") read: Boolean? = null,
        @Query("favorite") favorite: Boolean? = null,
        @Query("importants") importants: Boolean? = null,
        @Query("archived") archived: Boolean? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Number? = null,
        @Query("limit") limit: Number? = null,
    ): Response<GetEmailsResponse>

    @GET("email/{id}")
    suspend fun getEmailDetail(@Path("id") id: String): Response<EmailDetailResponse>

    @PATCH("email/favorite/{id}")
    suspend fun toggleFavorite(@Path("id") id: String): Response<LoginResponse>

    @POST("email")
    suspend fun sendEmail(@Body() body: SendEmailBody): Response<LoginResponse>
}

object Requester {
    lateinit var apiService: ApiService

    fun init(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(context))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }
}