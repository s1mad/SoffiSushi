package com.example.soffisushi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.soffisushi.navigation.graph.RootNavGraph
import com.example.soffisushi.presentation.component.bottom_bar.BottomBar
import com.example.soffisushi.presentation.component.top_bar.TopBar
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.ui.theme.SoffiSushiTheme
import com.example.soffisushi.util.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SoffiSushiViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            val point = viewModel.selectedPoint.value
            point !is Status.Success && point !is Status.Error
        }

        enableEdgeToEdge()
        setContent {
            SoffiSushiTheme(darkTheme = true) {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(
                            navController = navController,
                            viewModel = viewModel
                        )
                    },
                    bottomBar = { BottomBar(navController = navController, viewModel = viewModel) }
                ) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)
                    RootNavGraph(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = modifier
                    )
                }
            }
        }
    }
}
