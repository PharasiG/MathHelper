package com.example.mathhelper.navigation

enum class Screen {
    QUERY,
    CAMERA,
    PREVIEW
}
sealed class NavigationItem(val route: String) {
    data object Query : NavigationItem(Screen.QUERY.name)
    data object Camera : NavigationItem(Screen.CAMERA.name)
    data object PreviewImage : NavigationItem(Screen.PREVIEW.name)
}