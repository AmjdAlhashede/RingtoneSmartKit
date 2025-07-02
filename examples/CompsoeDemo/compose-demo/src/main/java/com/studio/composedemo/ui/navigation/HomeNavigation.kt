/*
 * Copyright 2025 Amjd Alhashede
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.studio.composedemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.studio.composedemo.ui.HomeScreen
import com.studio.composedemo.ui.dialogs.CustomizeDialog
import com.studio.composedemo.ui.screens.AssetsContactScreen
import com.studio.composedemo.ui.screens.AssetsSystemScreen
import com.studio.composedemo.ui.screens.StorageContactScreen
import com.studio.composedemo.ui.screens.StorageSystemScreen

@Composable
fun HomeNavigation(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: Screens = Screens.Home,
) {

    val onNavigation = { screen: Screens ->
        navController.navigate(screen)
    }

    val arrowBack = {
        navController.navigateUp()
        Unit
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screens.Home> {
            HomeScreen(
                onNavigation = onNavigation,
            )
        }

        composable<Screens.AssetSystem> {
            AssetsSystemScreen(onNavigation = onNavigation)
        }

        composable<Screens.AssetsContact> {
            AssetsContactScreen()
        }

        composable<Screens.StorageSystem> {
            StorageSystemScreen()
        }


        composable<Screens.StorageContact> {
            StorageContactScreen()
        }


        dialog<Screens.CustomizeDialog> {
            val item = it.toRoute<Screens.CustomizeDialog>()
            CustomizeDialog(
                asset = item.toAssetAudioItem(),
                onDismiss = arrowBack
            )
        }
    }

}