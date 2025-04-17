package com.ke.biliblli.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ke.biliblli.common.Screen
import com.ke.biliblli.mobile.ui.home.recommend.HomeRecommendVideoListRoute
import com.ke.biliblli.mobile.ui.login.LoginRoute
import com.ke.biliblli.mobile.ui.splash.SplashRoute
import com.ke.biliblli.mobile.ui.theme.BilibiliTheme
import com.ke.biliblli.mobile.ui.video.VideoDetailRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


//    @Inject
//    lateinit var bilibiliApi: BilibiliApi

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        lifecycleScope.launch {
//            try {
//                bilibiliApi.comments(114301176124616L, 1, 1, 0)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }


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
//                            navController.navigate(Screen.VideoDetail(29013182448, "BV1EZXxYyEeZ"))
                        }) {
                            navController.navigate(Screen.Login) {
                                popUpTo(Screen.Splash) {
                                    inclusive = true
                                }
                            }
                        }
                    }


                    composable<Screen.Main> {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text("推荐")
                                    }
                                )
                            }
                        ) { padding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                            ) {
                                HomeRecommendVideoListRoute {
                                    navController.navigate(
                                        Screen.VideoDetail(
                                            it.bvid
                                        )
                                    )
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

                    composable<Screen.VideoDetail> {

                        VideoDetailRoute {
                            navController.navigate(it)
                        }

                    }
                }
            }
        }
    }
}

