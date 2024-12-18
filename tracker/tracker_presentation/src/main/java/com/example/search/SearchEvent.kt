package com.example.search

import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackableFood
import java.time.LocalDate

sealed class SearchEvent {
    data class OnQueryChange(val query: String): SearchEvent()
    object OnSearch: SearchEvent()
    data class OnToggleTrackableFood(val food: TrackableFood): SearchEvent()
    data class OnAmountForFoodChange(
        val amount: String,
        val food: TrackableFood
    ): SearchEvent()
    data class OnTrackFoodClick(
        val food: TrackableFood,
        val mealType: MealType,
        val date: LocalDate
    ):SearchEvent()
    data class OnSearchFocusChange(
        val isFocused: Boolean
    ): SearchEvent()
}










