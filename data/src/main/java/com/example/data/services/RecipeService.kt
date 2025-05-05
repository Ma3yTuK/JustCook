package com.example.data.services

import com.example.data.models.CollectionType
import com.example.data.models.Ingredient
import com.example.data.models.MeasurementUnit
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection
import com.example.data.models.RecipeIngredient
import com.example.data.models.RecipeStep

object RecipeService {

    // Measurement units
    val units = listOf(
        MeasurementUnit(id = 1, name = "грамм"),
        MeasurementUnit(id = 2, name = "миллилитр"),
        MeasurementUnit(id = 3, name = "штука"),
        MeasurementUnit(id = 4, name = "чайная ложка"),
        MeasurementUnit(id = 5, name = "столовая ложка")
    )

    // Ingredients
    val ingredients = listOf(
        Ingredient(id = 1, name = "Мука", unit = units[0]),
        Ingredient(id = 2, name = "Молоко", unit = units[1]),
        Ingredient(id = 3, name = "Яйцо", unit = units[2]),
        Ingredient(id = 4, name = "Сахар", unit = units[4]),
        Ingredient(id = 5, name = "Соль", unit = units[3]),
        Ingredient(id = 6, name = "Масло сливочное", unit = units[0]),
        Ingredient(id = 7, name = "Помидор", unit = units[2]),
        Ingredient(id = 8, name = "Огурец", unit = units[2]),
        Ingredient(id = 9, name = "Оливковое масло", unit = units[4]),
        Ingredient(id = 10, name = "Картофель", unit = units[0]),
        Ingredient(id = 11, name = "Морковь", unit = units[0]),
        Ingredient(id = 12, name = "Лук", unit = units[2]),
        Ingredient(id = 13, name = "Чеснок", unit = units[3]),
        Ingredient(id = 14, name = "Куриное филе", unit = units[0]),
        Ingredient(id = 15, name = "Рис", unit = units[0]),
        Ingredient(id = 16, name = "Какао-порошок", unit = units[0]),
        Ingredient(id = 17, name = "Мука кукурузная", unit = units[0]),
        Ingredient(id = 18, name = "Сливки", unit = units[1])
    )

    // Recipes
    val pancake = Recipe(
        id = 1,
        name = "Блины на молоке",
        tagline = "Просто и вкусно",
        description = "Тонкие и нежные блины на молоке.",
        isFavourite = true,
        steps = listOf(
            RecipeStep(1, "Взбейте яйцо, сахар и соль."),
            RecipeStep(2, "Добавьте молоко и муку."),
            RecipeStep(3, "Жарьте на сковороде по 1-2 минуты со стороны.")
        ),
        ingredients = listOf(
            RecipeIngredient(1, 200f, ingredients[0]),
            RecipeIngredient(2, 300f, ingredients[1]),
            RecipeIngredient(3, 2f, ingredients[2]),
            RecipeIngredient(4, 2f, ingredients[3]),
            RecipeIngredient(5, 0.5f, ingredients[4]),
            RecipeIngredient(6, 20f, ingredients[5])
        )
    )

    val omelette = Recipe(
        id = 2,
        name = "Яичница-болтунья",
        tagline = "Быстрый завтрак",
        description = "Нежная болтунья с овощами.",
        isFavourite = false,
        steps = listOf(
            RecipeStep(1, "Взбейте яйца с солью."),
            RecipeStep(2, "Добавьте помидор и огурец."),
            RecipeStep(3, "Обжарьте на сковороде.")
        ),
        ingredients = listOf(
            RecipeIngredient(7, 3f, ingredients[2]),
            RecipeIngredient(8, 1f, ingredients[6]),
            RecipeIngredient(9, 1f, ingredients[7]),
            RecipeIngredient(10, 1f, ingredients[4]),
            RecipeIngredient(11, 1f, ingredients[8])
        )
    )

    val soup = Recipe(
        id = 3,
        name = "Овощной суп",
        tagline = "Полезно и легко",
        description = "Простой овощной суп на бульоне.",
        isFavourite = false,
        steps = listOf(
            RecipeStep(1, "Нарежьте картофель, морковь и лук."),
            RecipeStep(2, "Обжарьте лук и морковь."),
            RecipeStep(3, "Добавьте воду и картофель, варите 20 мин."),
            RecipeStep(4, "Приправьте солью и чесноком.")
        ),
        ingredients = listOf(
            RecipeIngredient(12, 300f, ingredients[9]),
            RecipeIngredient(13, 150f, ingredients[10]),
            RecipeIngredient(14, 1f, ingredients[11]),
            RecipeIngredient(15, 0.5f, ingredients[12])
        )
    )

    val chickenRice = Recipe(
        id = 4,
        name = "Курица с рисом",
        tagline = "Сытно и вкусно",
        description = "Куриное филе с рисом и овощами.",
        isFavourite = true,
        steps = listOf(
            RecipeStep(1, "Обжарьте курицу."),
            RecipeStep(2, "Добавьте рис и воду."),
            RecipeStep(3, "Тушите до готовности.")
        ),
        ingredients = listOf(
            RecipeIngredient(16, 300f, ingredients[13]),
            RecipeIngredient(17, 200f, ingredients[14]),
            RecipeIngredient(18, 100f, ingredients[8])
        )
    )

    val chocolateCake = Recipe(
        id = 5,
        name = "Шоколадный торт",
        tagline = "Для сладкоежек",
        description = "Пышный торт с какао и сливками.",
        isFavourite = false,
        steps = listOf(
            RecipeStep(1, "Смешайте муку, какао и сахар."),
            RecipeStep(2, "Добавьте яйца и сливки."),
            RecipeStep(3, "Выпекайте 40 мин при 180°C.")
        ),
        ingredients = listOf(
            RecipeIngredient(19, 150f, ingredients[0]),
            RecipeIngredient(20, 50f, ingredients[15]),
            RecipeIngredient(21, 3f, ingredients[2]),
            RecipeIngredient(22, 100f, ingredients[17])
        )
    )

    // Collections
    val breakfast = RecipeCollection(
        id = 1,
        name = "Завтраки",
        recipes = listOf(pancake, omelette),
        type = CollectionType.Highlight
    )

    val favourites = RecipeCollection(
        id = 2,
        name = "Любимые",
        recipes = listOf(pancake, chickenRice),
        type = CollectionType.Normal
    )

    val soups = RecipeCollection(
        id = 3,
        name = "Супы",
        recipes = listOf(soup),
        type = CollectionType.Normal
    )

    val mains = RecipeCollection(
        id = 4,
        name = "Основные блюда",
        recipes = listOf(chickenRice, soup),
        type = CollectionType.Highlight
    )

    val desserts = RecipeCollection(
        id = 5,
        name = "Десерты",
        recipes = listOf(chocolateCake),
        type = CollectionType.Normal
    )

    val veggie = RecipeCollection(
        id = 6,
        name = "Вегетарианские",
        recipes = listOf(pancake, omelette, soup),
        type = CollectionType.Normal
    )

    val festive = RecipeCollection(
        id = 7,
        name = "Праздничные",
        recipes = listOf(pancake, chocolateCake, chickenRice),
        type = CollectionType.Highlight
    )

    // Все коллекции
    val allCollections = listOf(
        breakfast,
        favourites,
        soups,
        mains,
        desserts,
        veggie,
        festive
    )

    val allRecipes = listOf(
        pancake,
        omelette,
        soup,
        chickenRice,
        chocolateCake
    )
}