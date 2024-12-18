package com.example.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.use_case.FilterOutDigits
import com.example.core.util.UiEvent
import com.example.tracker_domain.use_case.TrackerUseCases
import com.example.core.R
import com.example.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                state = state.copy(
                    query = event.query
                )
            }

            //Todo it's different with Philipp code base.
            is SearchEvent.OnAmountForFoodChange -> {
                //1
                state.trackableFood.map {
                    if (it == event.food)
                        state = state.copy(amount = filterOutDigits(event.amount))
                }
                //2
                /*state = state.copy(
                    amount = filterOutDigits(event.amount)
                )*/

                //3 -> Philipp
                /*state = state.copy(
                    trackableFood = state.trackableFood.map {
                        if (it.food == event.food)
                            it.copy(amount = filterOutDigits(event.amount))
                          else it
                    }
                )*/
            }

            is SearchEvent.OnSearch -> {
                executeSearch()
            }

            //Todo it's different with Philipp code base.
            is SearchEvent.OnToggleTrackableFood -> {
                //1
                state = state.copy(
//                    trackableFood = state.trackableFood.map {
//                        if (it.food == event.food)
                    isExpandable = !state.isExpandable
//                        else it
//                    }
                )

                //2
                /*state.trackableFood.map {
                    if (it == event.food)
                        state = state.copy(isExpandable = !state.isSearching)
                }*/

            }

            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }

            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            //reset the list:
            state = state.copy(
                isSearching = true,
                trackableFood = emptyList()
            )

            trackerUseCases.searchFood(state.query)
                .onSuccess {
                    state = state.copy(
                        trackableFood = it,
                        isSearching = false,
                        query = ""
                    )
                }
                .onFailure {
                    state = state.copy(isSearching = false)
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it == event.food }
            trackerUseCases.trackFood(
                food = uiState ?: return@launch,
                amount = state.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )

            //popBackStack -> navigate to previous screen
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }


}










