package com.example.data.services

import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.Ingredient
import com.example.data.models.MeasurementUnit
import com.example.data.models.Recipe
import com.example.data.models.RecipeIngredient
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User
import com.example.data.models.RecipeFilters
import com.example.data.models.RecipeSortingOption
import com.example.data.models.UserFilters
import com.example.data.models.UserSortingOption
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.data.models.search_suggestions.SearchSuggestion
import com.example.data.models.search_suggestions.SearchSuggestionGroup
import kotlinx.datetime.LocalDateTime

object RecipeService {

    // Единицы измерения
    val gram = MeasurementUnit(id = 1, name = "грамм")
    val ml = MeasurementUnit(id = 2, name = "миллилитр")
    val piece = MeasurementUnit(id = 3, name = "шт.")

    // Ингредиенты
    val flour = Ingredient(id = 1, name = "Мука", unit = gram)
    val sugar = Ingredient(id = 2, name = "Сахар", unit = gram)
    val salt = Ingredient(id = 3, name = "Соль", unit = gram)
    val milk = Ingredient(id = 4, name = "Молоко", unit = ml)
    val egg = Ingredient(id = 5, name = "Яйцо", unit = piece)
    val butter = Ingredient(id = 6, name = "Масло сливочное", unit = gram)

    // Полномочия и пользователи
    val authModerate = Authority(id = Authorities.Moderate.id, name = Authorities.Moderate.name)
    val userAlice = User(
        id = 1,
        name = "Алиса Алиса Алиса Алиса Алиса Алиса Алиса Алиса Алиса Алиса ",
        email = "alice@gmail.com",
        authorities = listOf(authModerate),
        isVerified = true,
        hasPremium = true
    )
    val userBob = User(
        id = 2,
        name = "Боб",
        email = "bob@gmail.com",
        authorities = emptyList(),
        isVerified = false,
        hasPremium = false
    )
    val userCarol = User(
        id = 3,
        name = "Кэрол",
        email = "hardtospell@gmail.com",
        authorities = listOf(authModerate),
        isVerified = true,
        hasPremium = true
    )

    val searchSuggestion1 = SearchSuggestion("Сыр")
    val searchSuggestion2 = SearchSuggestion("Яблочный сок")

    val searchSuggestionGroup1 = SearchSuggestionGroup("Недавние запросы", listOf(searchSuggestion1, searchSuggestion2))

    val realCategory1 = RealCategory(0, "Завтраки")
    val lifeStyleCategory1 = LifeStyleCategory(0, "Без глютена")

    val categoryCollection1 = CategoryCollection("Категории", listOf(realCategory1))
    val categoryCollection2 = CategoryCollection("По составу", listOf(lifeStyleCategory1))

    // Шаги приготовления
    val stepsPancakes = listOf(
        RecipeStep(id = 1, description = "В миске смешать муку, сахар и соль."),
        RecipeStep(id = 2, description = "Добавить молоко и яйца, взбить до однородности."),
        RecipeStep(id = 3, description = "Разогреть сковороду и растопить масло."),
        RecipeStep(id = 4, description = "Выпекать блины с двух сторон до золотистого цвета.")
    )

    val stepsOmelet = listOf(
        RecipeStep(id = 5, description = "Взбить яйца с молоком и щепоткой соли."),
        RecipeStep(id = 6, description = "Нагреть сковороду, добавить масло."),
        RecipeStep(id = 7, description = "Вылить яичную смесь и готовить под крышкой 3–4 минуты.")
    )

    // Ингредиенты для рецептов
    val ingredientsPancakes = listOf(
        RecipeIngredient(id = 1, amount = 200f, ingredient = flour),
        RecipeIngredient(id = 2, amount = 50f, ingredient = sugar),
        RecipeIngredient(id = 3, amount = 5f, ingredient = salt),
        RecipeIngredient(id = 4, amount = 300f, ingredient = milk),
        RecipeIngredient(id = 5, amount = 2f, ingredient = egg),
        RecipeIngredient(id = 6, amount = 20f, ingredient = butter)
    )

    val ingredientsOmelet = listOf(
        RecipeIngredient(id = 7, amount = 3f, ingredient = egg),
        RecipeIngredient(id = 8, amount = 50f, ingredient = milk),
        RecipeIngredient(id = 9, amount = 10f, ingredient = butter),
        RecipeIngredient(id = 10, amount = 2f, ingredient = salt)
    )

    // Отзывы
    val review1 = Review(
        id = 1,
        rating = 5,
        comment = "Отличные блины, вышли очень мягкими!Отличные блины, вышли очень мягкими!Отличные блины, вышли очень мягкими!Отличные блины, вышли очень мягкими!",
        date = LocalDateTime(2025, 3, 15, 14, 30),
        user = userAlice
    )
    val review2 = Review(
        id = 2,
        rating = 4,
        comment = "Вкусно, но я бы добавил ещё ванили.",
        date = LocalDateTime(2025, 4, 1, 9, 0),
        user = userBob
    )
    val review3 = Review(
        id = 3,
        rating = 5,
        comment = "Омлет получился воздушным и нежным.",
        date = LocalDateTime(2025, 2, 20, 18, 45),
        user = userCarol
    )

    // Рецепты
    val pancakes = Recipe(
        id = 1,
        name = "Классические блины Классические блины Классические блины",
        calories = 350,
        rating = 4.5f,
        description = "Мягкие и нежные блины на молоке.",
        isFavorite = true,
        steps = stepsPancakes,
        ingredients = ingredientsPancakes,
        reviews = listOf(review1, review2),
        user = userAlice,
        isPremium = true
    )

    val omelet = Recipe(
        id = 2,
        name = "Омлет",
        calories = 200,
        rating = 4.8f,
        description = "Пушистый омлет с молоком.",
        isFavorite = false,
        steps = stepsOmelet,
        ingredients = ingredientsOmelet,
        reviews = listOf(review3),
        user = userBob,
        isPremium = false
    )

    val userSortingOption1 = UserSortingOption(1, "Алфавит")
    val recipeSortingOption1 = RecipeSortingOption(1, "Название")

    fun recipesFiltered(filters: RecipeFilters? = null, searchQuery: String? = null): List<Recipe> {
        if (filters?.sortingOptionId?.toInt() == 1)
            return listOf(pancakes, omelet)
        if (filters?.sortingOptionId?.toInt() == 2)
            return listOf(pancakes)
        if (filters?.sortingOptionId?.toInt() == 3)
            return allRecipes.filter { it.user == currentUser }
        return listOf()
    }

    fun usersFiltered(filters: UserFilters? = null, searchQuery: String? = null): List<User> {
        if (filters?.sortingOptionId?.toInt() == 1)
            return listOf(userAlice, userCarol)
        return listOf()
    }

    // Список всех созданных объектов
    val userSortingOptions = listOf(userSortingOption1)
    val recipeSortingOptions = listOf(recipeSortingOption1)
    val allUnits = listOf(gram, ml, piece)
    val allIngredients = listOf(flour, sugar, salt, milk, egg, butter)
    val allUsers = listOf(userAlice, userBob, userCarol)
    val allRecipes = listOf(pancakes, omelet)
    val currentUser = userAlice
    val allCategoryCollections = listOf(categoryCollection1, categoryCollection2)
    val allSuggestionGroups = listOf(searchSuggestionGroup1)
}