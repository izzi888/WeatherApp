package i.herman.weatherapp.locationlist.contract

import i.herman.weatherapp.base.BaseViewEvent

sealed class LocationListEvent : BaseViewEvent {

    class OnLocationDetailsClick(
        val location: String,
        val lat: Double,
        val lng: Double,
    ) : LocationListEvent()
}
