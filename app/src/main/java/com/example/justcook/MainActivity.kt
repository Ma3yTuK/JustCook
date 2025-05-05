package com.example.justcook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.components.theme.JustCookTheme
import com.example.justcook.navigation.AppNavigation
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.justcook.navigation.JustBottomBar
import com.example.justcook.navigation.HomeSection
import com.example.justcook.navigation.findStartDestination

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JustCookTheme {
                val navController: NavHostController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                SharedTransitionLayout {
                    AnimatedContent(navBackStackEntry?.destination?.parent?.route) { currentRoute ->
                        CompositionLocalProvider(
                            LocalSharedTransitionScope provides this@SharedTransitionLayout,
                            LocalNavAnimatedVisibilityScope provides this@AnimatedContent
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
                                                currentRoute = currentRoute
                                                    ?: HomeSection.FEED.stringRoute,
                                                navigateToRoute = { route ->
                                                    if (route.stringRoute != navController.currentDestination?.parent?.route) {
                                                        navController.navigate(route.route) {
                                                            launchSingleTop = true
                                                            restoreState = true
                                                            // Pop up backstack to the first destination and save state. This makes going back
                                                            // to the start destination when pressing back in any other bottom tab.
                                                            popUpTo(
                                                                findStartDestination(
                                                                    navController.graph
                                                                ).id
                                                            ) {
                                                                saveState = true
                                                            }
                                                        }
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