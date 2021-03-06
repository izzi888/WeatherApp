package i.herman.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import i.herman.weatherapp.feature.forecastdetail.contract.ForecastDetailEvent
import i.herman.weatherapp.feature.forecastdetail.view.ForecastDetailRoute
import i.herman.weatherapp.feature.locationdetail.contract.LocationDetailEvent
import i.herman.weatherapp.feature.locationdetail.view.LocationDetailsRoute
import i.herman.weatherapp.feature.locationlist.contract.LocationListEvent
import i.herman.weatherapp.feature.locationlist.view.LocationListRoute

@Composable
fun WeatherNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "add_location",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = "add_location") {
            LocationListRoute { locationListEvent ->
                when (locationListEvent) {
                    is LocationListEvent.OnLocationDetailsClick -> {
                        navController.navigate("location_details" +
                                "/${locationListEvent.location}" +
                                "/${locationListEvent.lat}" +
                                "/${locationListEvent.lng}")
                    }
                }
            }
        }
        composable(
            route = "location_details/{location}/{lat}/{lng}",
            arguments = listOf(
                navArgument("location") { type = NavType.StringType },
                navArgument("lat") { type = NavType.StringType },
                navArgument("lng") { type = NavType.StringType },
            )
        ) {
            LocationDetailsRoute { locationDetailEvent ->
                when (locationDetailEvent) {
                    is LocationDetailEvent.OnWeatherDetailsClick -> {
                        navController.navigate("forecast_details" +
                                "/${locationDetailEvent.date}" +
                                "/${locationDetailEvent.lat}" +
                                "/${locationDetailEvent.lng}")
                    }
                    is LocationDetailEvent.OnBackClick -> navController.popBackStack()
                }
            }
        }
        composable(
            route = "forecast_details/{date}/{lat}/{lng}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType },
                navArgument("lat") { type = NavType.StringType },
                navArgument("lng") { type = NavType.StringType },
            )
        ) {
            ForecastDetailRoute { forecastDetailEvent ->
                when (forecastDetailEvent) {
                    is ForecastDetailEvent.OnBackClick -> navController.popBackStack()
                }
            }
        }
    }
}

