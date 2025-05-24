package com.example.justcook

import android.icu.text.DateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.components.theme.JustCookTheme
import com.example.justcook.navigation.AppNavigation
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.data.databases.SuggestionDatabase
import com.example.data.repositories.ImageRepository
import com.example.data.repositories.LocalImageRepository
import com.example.data.repositories.LocalSuggestionRepository
import com.example.data.services.auth.AuthService
import com.example.data.services.auth.sign_in.BlankSignInService
import com.example.data.services.auth.sign_in.LocalSignInService
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.auth.TokenService
import com.example.data.services.ingredient.IngredientService
import com.example.data.services.ingredient.LocalIngredientService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService
import com.example.data.services.review.LocalReviewService
import com.example.data.services.review.ReviewService
import com.example.data.services.user.LocalUserService
import com.example.data.services.user.UserService
import com.example.justcook.navigation.JustBottomBar
import com.example.justcook.navigation.HomeSection
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenService = TokenService(this)

        val httpClient = OkHttpClient().newBuilder()
            .addInterceptor(
                Interceptor { chain ->
                    val request: Request = chain.request()
                        .newBuilder()
                        .header("Authorization", tokenService.getToken().let { if (it.isNotBlank()) "Bearer ${it}" else "" })
                        .build()
                    chain.proceed(request)
                }
            )

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(this.resources.getString(R.string.backend_url))
            .client(httpClient.build())
            .build()

        val authService: AuthService = retrofit.create(AuthService::class.java)
        val signInService = BlankSignInService(authService, tokenService)
        val imageRepository = ImageRepository(retrofit)
        val userService = retrofit.create(UserService::class.java)
        val recipeService = retrofit.create(RecipeService::class.java)
        val reviewService = retrofit.create(ReviewService::class.java)
        val ingredientService = retrofit.create(IngredientService::class.java)

        val localDatabase = Room.databaseBuilder(this, SuggestionDatabase::class.java, resources.getString(R.string.database_name)).build()
        val localSuggestionRepository = localDatabase.localSearchEntityRepository()

        enableEdgeToEdge()
        setContent {
            JustCookTheme {
                val navController: NavHostController = rememberNavController()
                val currentRoute = remember { mutableStateOf(HomeSection.FEED.stringRoute) }

                SharedTransitionLayout {
                    AnimatedVisibility(visible = true) {
                        CompositionLocalProvider(
                            LocalSuggestionRepository provides localSuggestionRepository,
                            LocalIngredientService provides ingredientService,
                            LocalReviewService provides reviewService,
                            LocalRecipeService provides recipeService,
                            LocalUserService provides userService,
                            LocalImageRepository provides imageRepository,
                            LocalTokenService provides tokenService,
                            LocalSignInService provides signInService,
                            LocalSharedTransitionScope provides this@SharedTransitionLayout,
                            LocalNavAnimatedVisibilityScope provides this@AnimatedVisibility
                        ) {
                            val sharedTransitionScope = LocalSharedTransitionScope.current
                                ?: throw IllegalStateException("No SharedElementScope found")
                            val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
                                ?: throw IllegalStateException("No SharedElementScope found")

                            Scaffold(
                                bottomBar = {
                                    with(animatedVisibilityScope) {
                                        with(sharedTransitionScope) {
                                            JustBottomBar(
                                                tabs = HomeSection.entries.toTypedArray(),
                                                currentRoute = currentRoute.value,
                                                navigateToRoute = { route ->
                                                    if (route.stringRoute != currentRoute.value) {
                                                        navController.navigate(route.route) {
                                                            launchSingleTop = true
                                                            restoreState = true
                                                            // Pop up backstack to the first destination and save state. This makes going back
                                                            // to the start destination when pressing back in any other bottom tab.
                                                            popUpTo(
                                                                currentRoute.value
                                                            ) {
                                                                saveState = true
                                                            }
                                                        }
                                                        currentRoute.value = route.stringRoute
                                                    }
                                                },
                                                modifier = Modifier
                                                    .renderInSharedTransitionScopeOverlay(
                                                        zIndexInOverlay = 1f,
                                                    )
                                                    .animateEnterExit(
                                                        enter = fadeIn(
                                                            nonSpatialExpressiveSpring()
                                                        ) + slideInVertically(
                                                            spatialExpressiveSpring()
                                                        ) {
                                                            it
                                                        },
                                                        exit = fadeOut(
                                                            nonSpatialExpressiveSpring()
                                                        ) + slideOutVertically(
                                                            spatialExpressiveSpring()
                                                        ) {
                                                            it
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            ) { innerPadding ->
                                AppNavigation(
                                    navController,
                                    Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}