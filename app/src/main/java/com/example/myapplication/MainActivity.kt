package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.theme.MyApplicationTheme

enum class AppScreen {
    SPLASH, ONBOARDING, WELCOME, LOGIN, SIGNUP, HOME, PROFILE, EDIT_PROFILE,
    PRODUCT_DETAIL, CHANGE_PASSWORD, ADD_PRODUCT, FAQ
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
                var loggedInUser by remember { mutableStateOf("") }
                var userProfile by remember { mutableStateOf(UserProfile()) }
                var selectedPerfume by remember { mutableStateOf<PerfumeItem?>(null) }

                val context = LocalContext.current
                val db = remember { DatabaseHelper(context) }
                var products by remember { mutableStateOf(db.getAllProducts()) }

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
                        AppScreen.SPLASH -> SplashScreen(
                            onFinished = { currentScreen = AppScreen.ONBOARDING }
                        )
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
            }
        }
    }
}
