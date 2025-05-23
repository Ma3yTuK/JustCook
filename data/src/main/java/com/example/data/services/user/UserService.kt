package com.example.data.services.user

import androidx.compose.runtime.compositionLocalOf
import androidx.paging.PagingSource
import com.example.data.models.Page
import com.example.data.models.Recipe
import com.example.data.models.User
import com.example.data.models.query_params.UserFilters
import com.example.data.models.short_models.UserShort
import com.example.data.services.MyPagingSource
import com.example.data.services.auth.TokenService
import com.example.data.services.recipe.UploadRecipeRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

fun UserService.getPagingSource(filters: UserFilters, queryParam: String?): PagingSource<Int, UserShort> {
    val queryMap = queryParam?.let {
        mapOf("queryParam" to queryParam)
    } ?: mapOf()
    return MyPagingSource({ pageParams ->
        getAllUsers(pageParams + filters.toMap() + queryMap)
    })
}

interface UserService {

    @GET("/users/sorting_variants")
    suspend fun getSortingVariants(): List<String>

    @GET("/users/all")
    suspend fun getAllUsers(@QueryMap userRequestParams: Map<String, String>): Page<UserShort>

    @GET("/users/meShort")
    suspend fun getCurrentUserShort(): UserShort

    @GET("/users/me")
    suspend fun getCurrentUser(): User

    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId") userId: Long): User

    @POST("/users/update")
    suspend fun updateCurrent(@Body uploadUserRequest: UploadUserRequest): User

    @POST("/users/update/{userId}")
    suspend fun updateUser(@Path("userId") userId: Long, @Body uploadUserRequest: UploadUserRequest): User

    @POST("/recipes/delete/{recipeId}")
    suspend fun deleteRecipe(@Path("recipeId") recipeId: Long)
}

val LocalUserService = compositionLocalOf<UserService?> { null }