package com.ke.bilibili.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.ke.bilibili.tv.ui.CommentsRoute
import com.ke.bilibili.tv.ui.ConvertRoute
import com.ke.bilibili.tv.ui.LoginRoute
import com.ke.bilibili.tv.ui.MainRoute
import com.ke.bilibili.tv.ui.SplashRoute
import com.ke.bilibili.tv.ui.UserDetailRoute
import com.ke.bilibili.tv.ui.VideoDetailRoute
import com.ke.bilibili.tv.ui.theme.BilibiliTheme
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var bilibiliRepository: BilibiliRepository

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            BilibiliTheme {
                val navController = rememberNavController()

                NavHost(
                    navController, startDestination = Screen.Splash, modifier = Modifier.background(
                        MaterialTheme.colorScheme.background
                    )
                ) {

                    composable<Screen.Splash> {
                        SplashRoute(toMain = {

                            navController.navigate(Screen.Main(it)) {
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


                    composable<Screen.Convert> {
                        val convert = it.toRoute<Screen.Convert>()
                        ConvertRoute {
                            val id = bilibiliRepository.videoView(convert.bvid).data!!.aid
                            navController.navigate(Screen.Comment(id, 1)) {
                                popUpTo(convert) {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    composable<Screen.Comment> {
                        CommentsRoute {
                            navController.navigate(it)
                        }
                    }

                    composable<Screen.UserDetail> {
                        UserDetailRoute {
                            navController.navigate(it)
                        }
                    }
                }
            }
        }
    }
}
