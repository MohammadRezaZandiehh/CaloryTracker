package com.example.search

import com.example.tracker_domain.model.TrackableFood

//Todo it's different with Philipp code base.
data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,

    val isExpandable: Boolean = false,
    val amount: String = "",

    val trackableFood: List<TrackableFood> = emptyList()
)
