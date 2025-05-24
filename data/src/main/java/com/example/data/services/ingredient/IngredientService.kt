package com.example.data.services.ingredient

import androidx.compose.runtime.compositionLocalOf
import androidx.paging.PagingSource
import com.example.data.models.Ingredient
import com.example.data.models.Page
import com.example.data.models.query_params.PageParams
import com.example.data.services.MyPagingSource
import com.example.data.services.review.ReviewService
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

fun IngredientService.getPagingSource(queryParam: String?): PagingSource<Int, Ingredient> {
    val queryMap = queryParam?.let {
        mapOf("searchQuery" to queryParam)
    } ?: mapOf()
    return MyPagingSource({ pageParams ->
        getAllIngredients(pageParams + queryMap)
    })
}

interface IngredientService {
    @GET("/ingredients/allWithoutPagination")
    suspend fun getAllIngredientsWithoutPagination(@Query(value = "searchQuery") searchQuery: String? = null): List<Ingredient>

    @GET("/ingredients/all")
    suspend fun getAllIngredients(@QueryMap ingredientRecipeParams: Map<String, String>): Page<Ingredient>
}

val LocalIngredientService = compositionLocalOf<IngredientService?> { null }