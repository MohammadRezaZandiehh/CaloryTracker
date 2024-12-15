package com.example.search

import com.example.tracker_domain.model.TrackableFood

//Todo it's different with Philipp code base.  --> I don't use this class. I think it's additional data class.
data class TrackableFoodUiState(
    val food: TrackableFood,
    val isExpandable: Boolean = false,
    val amount: String = ""
)