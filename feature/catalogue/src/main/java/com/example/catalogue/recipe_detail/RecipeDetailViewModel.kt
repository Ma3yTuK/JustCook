package com.example.catalogue.recipe_detail

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.data.models.Authorities
import com.example.data.models.Image
import com.example.data.models.Ingredient
import com.example.data.models.IngredientUnitConversion
import com.example.data.models.Recipe
import com.example.data.models.RecipeConversion
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User
import com.example.data.models.short_models.UserShort
import com.example.data.repositories.ImageRepository
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.auth.TokenService
import com.example.data.services.ingredient.IngredientService
import com.example.data.services.ingredient.getPagingSource
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.UploadRecipeConversionRequest
import com.example.data.services.recipe.UploadRecipeRequest
import com.example.data.services.recipe.UploadRecipeStepRequest
import com.example.data.services.review.ReviewService
import com.example.data.services.review.UploadReviewRequest
import com.example.data.services.review.getPagingSource
import com.example.data.services.user.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Thread.sleep

data class RecipeUiState(
    val isInEditMode: Boolean = false,
    val recipe: Recipe = Recipe(
        id = -1,
        name = "Новый рецепт",
        weight = 0,
        calories = 0,
        rating = 0f,
        description = "",
        isFavorite = false,
        isPremium = false,
        user = UserShort(
            id = -1,
            name = "",
            email = "",
            isVerified = false,
            image = null
        ),
        steps = listOf(),
        ingredients = listOf(),
        isVerified = false,
        image = null,
    ),
    val ingredientQuery: String = "",
    val reviewFlow: Flow<PagingData<Review>> = emptyFlow(),
    val ingredientFlow: Flow<PagingData<Ingredient>> = emptyFlow(),
    val collectionIndex: Int? = null,
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val allIngredients: List<Ingredient> = listOf(),
    val conversions: List<IngredientUnitConversion>? = null
) {
    val isValid: Boolean
        get() = recipe.name.isNotBlank()
                && recipe.description.isNotBlank()
                && recipe.user.id >= 0
                && recipe.ingredients.isNotEmpty()
                && recipe.steps.isNotEmpty()
    val canEdit: Boolean
        get() = currentUser?.let {
            recipe.user.id == it.id || it.authorities.any {
                authority -> authority.id == Authorities.Moderate.id
            }
        } ?: false
    val isLoggedIn: Boolean
        get() = currentUser != null
    val isNewRecipe: Boolean
        get() = recipe.id == -1L
    val isModerating: Boolean
        get() = !recipe.isVerified
}

const val AMOUNT_OF_GRAM_FOR_CALORIES = 100
const val QUERY_UPDATE_INTERVAL = 700

class RecipeDetailViewModel(
    private val recipeService: RecipeService,
    private val userService: UserService,
    private val reviewService: ReviewService,
    private val imageRepository: ImageRepository,
    private val tokenService: TokenService,
    private val ingredientService: IngredientService
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    var onError: (e: Throwable) -> Unit = {}
    var onSeriousError: (e: Throwable) -> Unit = {}
    var onDelete: () -> Unit = {}
    var onPremium: () -> Unit = {}

    var cancelQuery: () -> Unit = {}

    private var fallbackDescription: String? = null
    private var fallbackName: String? = null
    private var fallbackImage: Image? = null
    private var fallbackCalories: Long? = null
    private var fallbackWeight: Long? = null
    private var fallbackIngredients: List<RecipeConversion> = listOf()
    private var fallbackSteps: List<RecipeStep> = listOf()

    private var reviewPagingSource: PagingSource<Int, Review>? by mutableStateOf(null)
    private var ingredientPagingSource: PagingSource<Int, Ingredient>? by mutableStateOf(null)

    private var ingredientQueryJob: Job? = null

    private val reviewPager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            reviewPagingSource = reviewService.getPagingSource(uiState.value.recipe.id)
            reviewPagingSource!!
        }
    )

    private val ingredientPager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            ingredientPagingSource = ingredientService.getPagingSource()
            ingredientPagingSource!!
        }
    )

    fun refresh(recipeId: Long?) = viewModelScope.launch {
        val newState = RecipeUiState()
        try {
            _uiState.update {
                newState.copy(
                    isLoading = true,
                    allIngredients = ingredientService.getAllIngredientsWithoutPagination(),
                )
            }
            if (recipeId == null) {
                refreshForCreation()
            } else {
                loadRecipe(recipeId)
            }
        } catch (e: Exception) {
            if (e is HttpException && e.code() == 403) {
                onPremium()
            } else {
                onSeriousErrorWrapper(e)
            }
        }
        reviewPagingSource?.invalidate()
        ingredientPagingSource?.invalidate()
        _uiState.update {
            it.copy(
                isLoading = false,
                reviewFlow = reviewPager.flow.cachedIn(viewModelScope),
                ingredientFlow = ingredientPager.flow.cachedIn(viewModelScope)
            )
        }
    }

    private suspend fun refreshForCreation() {
        getCurrentUser()
        _uiState.update {
            it.copy(
                isInEditMode = true,
                recipe = it.recipe.copy(user = userService.getCurrentUserShort()),
            )
        }
    }

    private suspend fun loadRecipe(recipeId: Long) {
        getCurrentUser()
        val newRecipe = recipeService.getRecipe(recipeId)
        _uiState.update {
            it.copy(
                recipe = newRecipe,
                isInEditMode = !newRecipe.isVerified
            )
        }
        if (!uiState.value.recipe.isVerified) {
            onEdit()
        }
    }

    private suspend fun getCurrentUser() {
        if (tokenService.isSighedIn()) {
            try {
                _uiState.update {
                    it.copy(
                        currentUser = userService.getCurrentUser()
                    )
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun updateConversionsForIngredient(ingredientId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                _uiState.update {
                    it.copy(
                        conversions = recipeService.getConversions(ingredientId),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun updateIngredient(ingredientIndex: Int, conversion: IngredientUnitConversion?, amount: Long? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                var newCalories: Long = 0
                var oldCalories: Long = 0
                _uiState.update { state ->
                    state.copy(
                        recipe = state.recipe.copy(
                            ingredients = state.recipe.ingredients.mapIndexed { index, recipeConversion ->
                                if (ingredientIndex == index) {
                                    oldCalories = getCaloriesForConversion(recipeConversion)
                                    val newConversion = recipeService.saveIngredient(
                                        uploadRecipeConversionRequest = UploadRecipeConversionRequest(
                                            amount = amount ?: recipeConversion.amount,
                                            conversionId = conversion?.id ?: recipeConversion.ingredientUnitConversion.id
                                        )
                                    )
                                    newCalories = getCaloriesForConversion(newConversion)
                                    newConversion
                                } else {
                                    recipeConversion
                                }
                            },
                            calories = state.recipe.calories + newCalories - oldCalories
                        ),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun deleteIngredient(ingredientIndex: Int) {
        _uiState.update {
            it.copy(
                recipe = it.recipe.copy(
                    ingredients = it.recipe.ingredients.filterIndexed { index, _ -> ingredientIndex != index },
                    calories = it.recipe.calories - getCaloriesForConversion(it.recipe.ingredients[ingredientIndex])
                ),
            )
        }
    }

    private fun getCaloriesForConversion(conversion: RecipeConversion): Long {
        val amount = conversion.amount
        val coefficient = conversion.ingredientUnitConversion.coefficient
        val caloriesPerUnits = conversion.ingredientUnitConversion.ingredient.calories
        return (amount * coefficient * caloriesPerUnits / AMOUNT_OF_GRAM_FOR_CALORIES).toLong()
    }

    fun updateStep(stepIndex: Int, description: String? = null, image: Uri? = null) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        try {
            _uiState.update {
                it.copy(
                    recipe = it.recipe.copy(
                        steps = it.recipe.steps.mapIndexed { index, recipeStep ->
                            if (stepIndex == index) {
                                recipeService.saveStep(
                                    uploadRecipeStepRequest = UploadRecipeStepRequest(
                                        index = index.toLong(),
                                        description = description ?: recipeStep.description,
                                        imageId = image?.let { imageRepository.uploadImage(image).id } ?: recipeStep.image?.id
                                    )
                                )
                            } else {
                                recipeStep
                            }
                        }
                    ),
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            onErrorWrapper(e)
        }
    }

    fun deleteStep(stepIndex: Int) {
        _uiState.update {
            it.copy(
                recipe = it.recipe.copy(
                    steps = it.recipe.steps.subList(0, stepIndex).map {
                            step -> step.copy(index = step.index + 1)
                    } + it.recipe.steps.subList(stepIndex + 1, it.recipe.steps.size),
                )
            )
        }
    }

    fun addIngredient(conversion: IngredientUnitConversion, amount: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                val newIngredient = recipeService.saveIngredient(
                    UploadRecipeConversionRequest(
                        amount = amount,
                        conversionId = conversion.id
                    )
                )
                _uiState.update {
                    it.copy(
                        recipe = it.recipe.copy(
                            ingredients = it.recipe.ingredients + listOf(newIngredient),
                            calories = getCaloriesForConversion(newIngredient)
                        ),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun addStep(description: String, image: Uri? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                _uiState.update {
                    it.copy(
                        recipe = it.recipe.copy(
                            steps = it.recipe.steps + listOf(recipeService.saveStep(
                                UploadRecipeStepRequest(
                                    description = description,
                                    index = it.recipe.steps.size.toLong(),
                                    imageId = image?.let { imageRepository.uploadImage(image).id }
                                )
                            ))
                        ),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun canDeleteReview(review: Review): Boolean {
        return uiState.value.currentUser?.let {
            review.user.id == it.id || it.authorities.any {
                authority -> authority.id == Authorities.Moderate.id
            }
        } ?: false
    }

    fun onRemoveRecipe() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        try {
            recipeService.deleteRecipe(uiState.value.recipe.id)
            onDelete()
        } catch (e: Exception) {
            onErrorWrapper(e)
        }
    }

    fun onDeleteReview(reviewId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            try {
                reviewService.deleteReview(reviewId)
                _uiState.update {
                    it.copy(isLoading = false)
                }
                reviewPagingSource?.invalidate()
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun onSubmitReview(rating: Long, comment: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            try {
                reviewService.addReview(UploadReviewRequest(
                    rating = rating,
                    recipeId = uiState.value.recipe.id,
                    comment = comment
                ))
                _uiState.update {
                    it.copy(isLoading = false)
                }
                reviewPagingSource?.invalidate()
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun onEditToggle() {
        if (uiState.value.isInEditMode)
            onSaveEdit()
        else
            onEdit()
    }

    fun onEdit() {
        _uiState.update { state ->
            fallbackDescription = state.recipe.description
            fallbackName = state.recipe.name
            fallbackImage = state.recipe.image
            fallbackSteps = state.recipe.steps
            fallbackIngredients = state.recipe.ingredients
            fallbackCalories = state.recipe.calories
            fallbackWeight = state.recipe.weight
            state.copy(
                isInEditMode = true
            )
        }
    }

    private fun ingredientQueryChangeJob(ingredientQuery: String): () -> Unit {
        var isCancelled = false

        viewModelScope.launch {
            delay(QUERY_UPDATE_INTERVAL.toLong())
            if (isCancelled)
                return@launch
            val allIngredients = ingredientService.getAllIngredientsWithoutPagination(ingredientQuery)
            if (isCancelled)
                return@launch
            _uiState.update {
                it.copy(
                    allIngredients = allIngredients
                )
            }
        }

        return { isCancelled = true }
    }

    fun onIngredientQueryChange(ingredientQuery: String) {
        cancelQuery()
        cancelQuery = ingredientQueryChangeJob(ingredientQuery)

        _uiState.update {
            it.copy(
                ingredientQuery = ingredientQuery
            )
        }
    }

    fun onCancelEdit() {
        _uiState.update {
            it.copy(
                isInEditMode = false,
                recipe = it.recipe.copy(
                    calories = fallbackCalories!!,
                    ingredients = fallbackIngredients,
                    steps = fallbackSteps,
                    description = fallbackDescription!!,
                    name = fallbackName!!,
                    image = fallbackImage,
                    weight = fallbackWeight!!
                ),
            )
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update {
            it.copy(
                recipe = it.recipe.copy(
                    description = newDescription
                ),
            )
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                recipe = it.recipe.copy(
                    name = newName
                ),
            )
        }
    }

    fun onWeightChange(newWeight: Long) {
        _uiState.update {
            it.copy(
                recipe = it.recipe.copy(
                    weight = newWeight
                ),
            )
        }
    }

    fun onToggleFavoriteClick() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                _uiState.update {
                    it.copy(
                        recipe = it.recipe.copy(
                            isFavorite = recipeService.makeFavorite(uiState.value.recipe.id)
                        ),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun onImageChange(uri: Uri) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        recipe = it.recipe.copy(image = imageRepository.uploadImage(uri)),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError(e)
            }
        }
    }

    fun onSaveEdit() {
        val state = _uiState.value
        if (!state.isValid) return
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                _uiState.update {
                    val uploadRequest = UploadRecipeRequest(it.recipe)
                    it.copy(
                        recipe = if(it.recipe.id.toInt() == -1) recipeService.saveRecipe(uploadRequest) else recipeService.updateRecipe(it.recipe.id, uploadRequest),
                        isLoading = false,
                        isInEditMode = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError(e)
            }
        }
    }

    private fun onErrorWrapper(e: Throwable) {
        _uiState.update {
            it.copy(isLoading = false)
        }
        onError(e)
    }

    private fun onSeriousErrorWrapper(e: Throwable) {
        _uiState.update {
            it.copy(isLoading = false)
        }
        onSeriousError(e)
    }

    private fun onPremiumWrapper() {
        _uiState.update {
            it.copy(isLoading = false)
        }
        onPremium()
    }

    companion object {
        fun provideFactory(
            recipeService: RecipeService,
            userService: UserService,
            reviewService: ReviewService,
            imageRepository: ImageRepository,
            tokenService: TokenService,
            ingredientService: IngredientService,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RecipeDetailViewModel(
                    imageRepository = imageRepository,
                    tokenService = tokenService,
                    userService = userService,
                    recipeService = recipeService,
                    reviewService = reviewService,
                    ingredientService = ingredientService
                ) as T
            }
        }
    }
}