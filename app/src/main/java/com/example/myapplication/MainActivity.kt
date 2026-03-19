package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.theme.MyApplicationTheme

enum class AppScreen {
    SPLASH, ONBOARDING, WELCOME, LOGIN, SIGNUP, HOME, PROFILE, EDIT_PROFILE,
    PRODUCT_DETAIL, CHANGE_PASSWORD, ADD_PRODUCT, FAQ
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                val splashAlreadyShown = prefs.getBoolean("splash_shown", false)
                var splashDone by remember { mutableStateOf(splashAlreadyShown) }
                var currentScreen by remember { mutableStateOf(AppScreen.ONBOARDING) }
                var loggedInUser by remember { mutableStateOf("") }
                var userProfile by remember { mutableStateOf(UserProfile()) }
                var selectedPerfume by remember { mutableStateOf<PerfumeItem?>(null) }
                val db = remember { DatabaseHelper(context) }
                var products by remember { mutableStateOf(db.getAllProducts()) }

                Crossfade(
                    targetState = splashDone,
                    animationSpec = tween(600),
                    label = "splashCrossfade"
                ) { done ->
                if (!done) {
                    SplashScreen(onFinished = {
                        prefs.edit().putBoolean("splash_shown", true).apply()
                        splashDone = true
                    })
                } else {

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        when {
                            targetState == AppScreen.SIGNUP ->
                                (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
                            initialState == AppScreen.SIGNUP ->
                                (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
                            targetState in setOf(
                                AppScreen.PROFILE, AppScreen.EDIT_PROFILE,
                                AppScreen.PRODUCT_DETAIL, AppScreen.CHANGE_PASSWORD,
                                AppScreen.ADD_PRODUCT, AppScreen.FAQ
                            ) ->
                                (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
                            initialState in setOf(
                                AppScreen.PROFILE, AppScreen.EDIT_PROFILE,
                                AppScreen.PRODUCT_DETAIL, AppScreen.CHANGE_PASSWORD,
                                AppScreen.ADD_PRODUCT, AppScreen.FAQ
                            ) ->
                                (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
                            else ->
                                fadeIn() togetherWith fadeOut()
                        }
                    },
                    label = "screenTransition"
                ) { screen ->
                    when (screen) {
                        AppScreen.SPLASH -> {}
                        AppScreen.ONBOARDING -> OnboardingScreen(
                            onFinished = { currentScreen = AppScreen.WELCOME }
                        )
                        AppScreen.WELCOME -> WelcomeScreen(
                            onNavigateToLogin = { currentScreen = AppScreen.LOGIN },
                            onNavigateToSignUp = { currentScreen = AppScreen.SIGNUP }
                        )
                        AppScreen.LOGIN -> LoginScreen(
                            db = db,
                            onLoginSuccess = { user ->
                                loggedInUser = user.fullName.ifBlank { user.email }
                                userProfile = UserProfile(
                                    fullName = user.fullName,
                                    email = user.email,
                                    phone = user.phone,
                                    dateOfBirth = user.dateOfBirth
                                )
                                currentScreen = AppScreen.HOME
                            },
                            onNavigateToSignUp = { currentScreen = AppScreen.SIGNUP }
                        )
                        AppScreen.SIGNUP -> SignUpScreen(
                            db = db,
                            onSignUpSuccess = { currentScreen = AppScreen.LOGIN },
                            onNavigateToLogin = { currentScreen = AppScreen.LOGIN }
                        )
                        AppScreen.HOME -> HomeScreen(
                            username = loggedInUser,
                            products = products,
                            onNavigateToProfile = { currentScreen = AppScreen.PROFILE },
                            onNavigateToProduct = { perfume ->
                                selectedPerfume = perfume
                                currentScreen = AppScreen.PRODUCT_DETAIL
                            },
                            onNavigateToAddProduct = { currentScreen = AppScreen.ADD_PRODUCT },
                            onNavigateToFaq = { currentScreen = AppScreen.FAQ }
                        )
                        AppScreen.PROFILE -> ProfileScreen(
                            username = loggedInUser,
                            profile = userProfile,
                            onEditProfile = { currentScreen = AppScreen.EDIT_PROFILE },
                            onChangePassword = { currentScreen = AppScreen.CHANGE_PASSWORD },
                            onLogout = {
                                loggedInUser = ""
                                userProfile = UserProfile()
                                currentScreen = AppScreen.WELCOME
                            },
                            onBack = { currentScreen = AppScreen.HOME }
                        )
                        AppScreen.CHANGE_PASSWORD -> ChangePasswordScreen(
                            onBack = { currentScreen = AppScreen.PROFILE }
                        )
                        AppScreen.EDIT_PROFILE -> EditProfileScreen(
                            profile = userProfile,
                            onSave = { updated ->
                                userProfile = updated
                                loggedInUser = updated.fullName.ifBlank { loggedInUser }
                                currentScreen = AppScreen.PROFILE
                            },
                            onBack = { currentScreen = AppScreen.PROFILE }
                        )
                        AppScreen.PRODUCT_DETAIL -> selectedPerfume?.let { perfume ->
                            ProductDetailScreen(
                                perfume = perfume,
                                onDelete = {
                                    db.deleteProduct(perfume.id)
                                    products = db.getAllProducts()
                                    selectedPerfume = null
                                    currentScreen = AppScreen.HOME
                                },
                                onBack = { currentScreen = AppScreen.HOME }
                            )
                        }
                        AppScreen.ADD_PRODUCT -> AddProductScreen(
                            onAdd = { newProduct ->
                                db.addProduct(newProduct)
                                products = db.getAllProducts()
                                currentScreen = AppScreen.HOME
                            },
                            onBack = { currentScreen = AppScreen.HOME }
                        )
                        AppScreen.FAQ -> FaqScreen(
                            onBack = { currentScreen = AppScreen.HOME }
                        )
                    }
                }

                } // end if splashDone
                } // end Crossfade
            }
        }
    }
}
