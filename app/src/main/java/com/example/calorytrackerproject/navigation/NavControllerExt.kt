package com.example.calorytrackerproject.navigation

import androidx.navigation.NavController
import com.example.core.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}