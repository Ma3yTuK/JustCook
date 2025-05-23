package com.example.data.services

import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.Image
import com.example.data.models.Ingredient
import com.example.data.models.MeasurementUnit
import com.example.data.models.Recipe
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User
import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.query_params.UserFilters
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.data.models.search_suggestions.SearchSuggestion
import com.example.data.models.search_suggestions.SearchSuggestionGroup
import com.example.data.models.short_models.UserShort
import kotlinx.datetime.LocalDateTime

object RecipeServiceImpl {

    // Единицы измерения
    val gram = MeasurementUnit(id = 1, name = "грамм")
    val ml = MeasurementUnit(id = 2, name = "миллилитр")
    val piece = MeasurementUnit(id = 3, name = "шт.")

    // Полномочия и пользователи
    val authModerate = Authority(id = Authorities.Moderate.id, name = Authorities.Moderate.name)
    val userAlice = User(
        id = 1,
        name = "Алиса Алиса Алиса Алиса Алиса Алиса Алиса Алиса Алиса Алиса ",
        email = "alice@gmail.com",
        authorities = listOf(authModerate),
        isVerified = true,
        image = Image(0, "")
    )
    val userBob = User(
        id = 2,
        name = "Боб",
        email = "bob@gmail.com",
        authorities = emptyList(),
        isVerified = false,
        image = Image(0, "")
    )
    val userCarol = User(
        id = 3,
        name = "Кэрол",
        email = "hardtospell@gmail.com",
        authorities = listOf(authModerate),
        isVerified = true,
        image = Image(0, "")
    )

    val searchSuggestion1 = SearchSuggestion("Сыр")
    val searchSuggestion2 = SearchSuggestion("Яблочный сок")

    val searchSuggestionGroup1 = SearchSuggestionGroup("Недавние запросы", listOf(searchSuggestion1, searchSuggestion2))

    val realCategory1 = RealCategory(0, "Завтраки", null)
    val lifeStyleCategory1 = LifeStyleCategory(0, "Без глютена", null)

    val categoryCollection1 = CategoryCollection("Категории", listOf(realCategory1))
    val categoryCollection2 = CategoryCollection("По составу", listOf(lifeStyleCategory1))

    val allIngredients: List<Ingredient> = listOf()
    val allUsers = listOf(userAlice, userBob, userCarol)
    val allCategoryCollections = listOf(categoryCollection1, categoryCollection2)
    val allSuggestionGroups = listOf(searchSuggestionGroup1)
}