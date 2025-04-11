package com.ke.bilibili.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.ke.bilibili.tv.ui.LoginRoute
import com.ke.bilibili.tv.ui.MainRoute
import com.ke.bilibili.tv.ui.SplashRoute
import com.ke.bilibili.tv.ui.VideoDetailRoute
import com.ke.bilibili.tv.ui.theme.BilibiliTheme
import com.ke.biliblli.common.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BilibiliTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = Screen.Splash) {

                    composable<Screen.Splash> {
                        SplashRoute(toMain = {

                            navController.navigate(Screen.Main) {
                                popUpTo(Screen.Splash) {
                                    inclusive = true
                                }
                            }
                        }) {
                            navController.navigate(Screen.Login) {
                                popUpTo(Screen.Splash) {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    composable<Screen.Login> {
                        LoginRoute {
                            navController.navigate(Screen.Main) {
                                popUpTo(Screen.Login) {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    composable<Screen.Main> {
                        MainRoute({
                            navController.navigate(it)
                        })
                    }

                    composable<Screen.VideoDetail> {
                        VideoDetailRoute()
                    }
                }
            }
        }
    }
}
