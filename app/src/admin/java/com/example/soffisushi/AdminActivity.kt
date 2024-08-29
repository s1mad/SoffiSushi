package com.example.soffisushi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.soffisushi.navigation.graph.AdminRootNavGraph
import com.example.soffisushi.presentation.component.bottom_bar.AdminBottomBar
import com.example.soffisushi.presentation.component.fab.FloatingActionButton
import com.example.soffisushi.presentation.viewmodel.AdminViewModel
import com.example.soffisushi.ui.theme.SoffiSushiTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminActivity : ComponentActivity() {
    private val adminViewModel: AdminViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoffiSushiTheme(window) {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            viewModel = adminViewModel,
                            navController = navController
                        )
                    },
                    bottomBar = { AdminBottomBar(navController) }
                ) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)

                    AdminRootNavGraph(
                        modifier = modifier,
                        viewModel = adminViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}