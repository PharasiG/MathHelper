package com.example.mathhelper.navigation

interface Destinations {
    val route: String
}

object Query : Destinations {
    override val route = "Query"
}