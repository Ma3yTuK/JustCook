package com.example.data.services

import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.CollectionType
import com.example.data.models.Ingredient
import com.example.data.models.MeasurementUnit
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection
import com.example.data.models.RecipeIngredient
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User
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
        authorities = listOf(authModerate)
    )
    val userBob = User(
        id = 2,
        name = "Боб",
        email = "bob@gmail.com",
        authorities = emptyList()
    )
    val userCarol = User(
        id = 3,
        name = "Кэрол",
        email = "hardtospell@gmail.com",
        authorities = listOf(authModerate)
    )

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
        name = "Классические блины",
        calories = 350,
        rating = 4.5f,
        description = "Мягкие и нежные блины на молоке.",
        isFavourite = true,
        steps = stepsPancakes,
        ingredients = ingredientsPancakes,
        reviews = listOf(review1, review2)
    )

    val omelet = Recipe(
        id = 2,
        name = "Омлет",
        calories = 200,
        rating = 4.8f,
        description = "Пушистый омлет с молоком.",
        isFavourite = false,
        steps = stepsOmelet,
        ingredients = ingredientsOmelet,
        reviews = listOf(review3)
    )

    // Коллекции рецептов
    val breakfastCollection = RecipeCollection(
        id = 1,
        name = "Завтраки",
        recipes = listOf(pancakes, omelet),
        type = CollectionType.Highlight
    )

    val normalCollection = RecipeCollection(
        id = 2,
        name = "Простые рецепты",
        recipes = listOf(pancakes),
        type = CollectionType.Normal
    )

    // Список всех созданных объектов
    val allUnits = listOf(gram, ml, piece)
    val allIngredients = listOf(flour, sugar, salt, milk, egg, butter)
    val allUsers = listOf(userAlice, userBob, userCarol)
    val allRecipes = listOf(pancakes, omelet)
    val allCollections = listOf(breakfastCollection, normalCollection)
}