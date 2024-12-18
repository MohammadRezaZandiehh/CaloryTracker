package com.example.search

import com.example.tracker_domain.model.TrackableFood

data class TrackableFoodUiState(
    val food: TrackableFood,
    val isExpandable: Boolean = false,
    val amount: String = ""
)