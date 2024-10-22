package com.techlambda.camerainstallation.dev

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

val LocalNavigationProvider = staticCompositionLocalOf<NavHostController> {
    error("No NavHost")
}

@Composable
fun AppNavHost(modifier: Modifier) {
    val navHostController = LocalNavigationProvider.current
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = "Home",
    ) {
        composable(route = "home") {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting(name = "Hello Android")
            }
        }

    }
}

@Composable
fun AppLogo() {
    Image(
        painter = painterResource(id = com.techlambda.authlibrary.R.drawable.ic_flag),
        contentDescription = "App Logo"
    )
}
