package com.moov.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moov.app.R
import com.moov.app.data.remote.TmdbMovieDto
import com.moov.app.ui.detail.DetailScreen
import com.moov.app.ui.favorite.FavoriteScreen
import com.moov.app.ui.home.HomeScreen
import com.moov.app.ui.profile.EditProfileScreen
import com.moov.app.ui.profile.ProfileScreen
import com.moov.app.ui.search.DummySearchMovie
import com.moov.app.ui.search.SearchScreen

data class BottomNavItem(
    val label: String,
    val iconResId: Int,
    val screen: @Composable () -> Unit
)

@Composable
fun MainScreen(onLogout: () -> Unit = {}) {
    var selectedIndex by remember { mutableStateOf(0) }
    var showEditProfile by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<TmdbMovieDto?>(null) }
    var selectedSearchMovie by remember { mutableStateOf<DummySearchMovie?>(null) }

    // Navigasi ke Detail dari Search
    if (selectedSearchMovie != null) {
        selectedSearchMovie = null
    }

    // Navigasi ke Edit Profile
    if (showEditProfile) {
        EditProfileScreen(onNavigateBack = { showEditProfile = false })
        return
    }

    // Navigasi ke Detail dari Home
    if (selectedMovie != null) {
        selectedMovie = null
    }

    val navItems = listOf(
        BottomNavItem("Home", R.drawable.home, {
            HomeScreen(onNavigateToDetail = { selectedMovie = it })
        }),
        BottomNavItem("Search", R.drawable.search, {
            SearchScreen(onMovieClick = { movie -> selectedSearchMovie = movie })
        }),
        BottomNavItem("Favorite", R.drawable.favorite, { FavoriteScreen() }),
        BottomNavItem("Profile", R.drawable.profil, {
            ProfileScreen(
                onNavigateToEdit = { showEditProfile = true },
                onLogout = onLogout
            )
        })
    )

    Scaffold(
        containerColor = Color(0xFF0D0D0D),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1A1A1A)
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Image(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(item.label, fontSize = 12.sp) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Transparent,
                            selectedTextColor = Color(0xFFE50914),
                            unselectedIconColor = Color.Transparent,
                            unselectedTextColor = Color(0xFF757575),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            navItems[selectedIndex].screen()
        }
    }
}