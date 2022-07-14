package i.herman.weatherapp.locationdetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import i.herman.weatherapp.base.BaseViewModel
import i.herman.weatherapp.base.StateReducer
import i.herman.weatherapp.data.Response
import i.herman.weatherapp.data.remote.model.toForecastItemList
import i.herman.weatherapp.data.repository.WeatherRepository
import i.herman.weatherapp.locationdetail.contract.LocationDetailEvent
import i.herman.weatherapp.locationdetail.contract.LocationDetailStateReducer
import i.herman.weatherapp.locationdetail.contract.LocationDetailViewIntent
import i.herman.weatherapp.locationdetail.contract.LocationDetailViewState
import i.herman.weatherapp.locationdetail.model.LocationDetailState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: WeatherRepository,
) : BaseViewModel<LocationDetailViewState, LocationDetailViewIntent, LocationDetailEvent>() {

    val locationName: String = checkNotNull(savedStateHandle["location"])
    private val lat: String = checkNotNull(savedStateHandle["lat"])
    private val lng: String = checkNotNull(savedStateHandle["lng"])

    init {
        processIntent(LocationDetailViewIntent.FetchSevenDaysWeatherForecast)
    }

    override fun createInitialState(): LocationDetailViewState =
        LocationDetailViewState(state = LocationDetailState.LoadingForecast)

    @OptIn(FlowPreview::class)
    override fun Flow<LocationDetailViewIntent>.handleIntent(): Flow<StateReducer<LocationDetailViewState>> {

        val fetchWeatherForecast =
            filterIsInstance<LocationDetailViewIntent.FetchSevenDaysWeatherForecast>()
                .flatMapConcat {
                    repository.getWeatherData(lat = lat.toDouble(), lng = lng.toDouble())
                        .map { response ->
                            when (response) {
                                is Response.Success -> {
                                    LocationDetailStateReducer.LoadedForecastList(response.data.daily.toForecastItemList())
                                }
                                is Response.Error -> {
                                    //TODO handle error
                                    LocationDetailStateReducer.EmptyList
                                }
                            }
                        }.onStart {
                            emit(LocationDetailStateReducer.LoadList)
                        }
                }

        return merge(
            fetchWeatherForecast
        )
    }

}
