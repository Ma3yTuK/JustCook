package com.example.data.services.recipe

import androidx.compose.runtime.compositionLocalOf
import androidx.paging.PagingSource
import com.example.data.models.IngredientUnitConversion
import com.example.data.models.Page
import com.example.data.models.Recipe
import com.example.data.models.RecipeConversion
import com.example.data.models.RecipeStep
import com.example.data.models.categories.Category
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.MyPagingSource
import com.example.data.services.auth.TokenService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

fun RecipeService.getPagingSource(filters: RecipeFilters, queryParam: String?): PagingSource<Int, RecipeShort> {
    val queryMap = queryParam?.let {
        mapOf("searchQuery" to queryParam)
    } ?: mapOf()
    return MyPagingSource({ pageParams ->
        getAllRecipes(pageParams + filters.toMap() + queryMap)
    })
}

fun RecipeService.getUnVerifiedPagingSource(): PagingSource<Int, RecipeShort> {
    return MyPagingSource({ pageParams ->
        getUnVerifiedRecipes(pageParams)
    })
}

fun RecipeService.getFavoritePagingSource(): PagingSource<Int, RecipeShort> {
    return MyPagingSource({ pageParams ->
        getFavoriteRecipes(pageParams)
    })
}

fun RecipeService.getMyRecipesPagingSource(): PagingSource<Int, RecipeShort> {
    return MyPagingSource({ pageParams ->
        getMyRecipes(pageParams)
    })
}

interface RecipeService {

    @GET("/recipes/categories")
    suspend fun getCategories(): List<RealCategory>

    @GET("/recipes/lifestyles")
    suspend fun getLifeStyles(): List<LifeStyleCategory>

    @GET("/recipes/sorting_variants")
    suspend fun getSortingVariants(): List<String>

    @GET("/recipes/unVerified")
    suspend fun getUnVerifiedRecipes(@QueryMap recipeRequestParams: Map<String, String>): Page<RecipeShort>

    @GET("/recipes/favorite")
    suspend fun getFavoriteRecipes(@QueryMap recipeRequestParams: Map<String, String>): Page<RecipeShort>

    @GET("/recipes/all")
    suspend fun getAllRecipes(@QueryMap recipeRequestParams: Map<String, String>): Page<RecipeShort>

    @GET("/recipes/mine")
    suspend fun getMyRecipes(@QueryMap recipeRequestParams: Map<String, String>): Page<RecipeShort>

    @POST("/recipes/makeFavorite/{recipeId}")
    suspend fun makeFavorite(@Path("recipeId") recipeId: Long): Boolean

    @GET("/recipes/{recipeId}")
    suspend fun getRecipe(@Path("recipeId") recipeId: Long): Recipe

    @POST("/recipes/add")
    suspend fun saveRecipe(@Body uploadRecipeRequest: UploadRecipeRequest): Recipe

    @POST("/recipes/update/{recipeId}")
    suspend fun updateRecipe(@Path("recipeId") recipeId: Long, @Body uploadRecipeRequest: UploadRecipeRequest): Recipe

    @POST("/recipes/saveStep")
    suspend fun saveStep(@Body uploadRecipeStepRequest: UploadRecipeStepRequest): RecipeStep

    @POST("/recipes/updateStep/{stepId}")
    suspend fun updateStep(@Path("stepId") stepId: Long, @Body uploadRecipeStepRequest: UploadRecipeStepRequest): RecipeStep

    @POST("/recipes/deleteStep/{stepId}")
    suspend fun deleteStep(@Path("stepId") stepId: Long)

    @GET("/recipes/conversion/{ingredientId}")
    suspend fun getConversions(@Path("ingredientId") ingredientId: Long): List<IngredientUnitConversion>

    @POST("/recipes/saveIngredient")
    suspend fun saveIngredient(@Body uploadRecipeConversionRequest: UploadRecipeConversionRequest): RecipeConversion

    @POST("/recipes/updateIngredient/{ingredientId}")
    suspend fun updateIngredient(@Path("ingredientId") ingredientId: Long, @Body uploadRecipeConversionRequest: UploadRecipeConversionRequest): RecipeConversion

    @POST("/recipes/deleteIngredient/{ingredientId}")
    suspend fun deleteIngredient(@Path("ingredientId") ingredientId: Long)

    @POST("/recipes/delete/{recipeId}")
    suspend fun deleteRecipe(@Path("recipeId") recipeId: Long)
}

val LocalRecipeService = compositionLocalOf<RecipeService?> { null }