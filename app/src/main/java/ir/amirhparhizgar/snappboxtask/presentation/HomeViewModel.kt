package ir.amirhparhizgar.snappboxtask.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val haveCoarseLocationPermission
        get() = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    val haveFineLocationPermission
        get() = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private val _moveToUserLocationEvent = MutableSharedFlow<Unit>()
    val moveToUserLocationEvent: SharedFlow<Unit> = _moveToUserLocationEvent

    private val _onShowRationaleForCoarseLocation = MutableSharedFlow<Unit>()
    val onShowRationaleForCoarseLocation: SharedFlow<Unit> = _onShowRationaleForCoarseLocation

    private val _onShowRationaleForFineLocation = MutableSharedFlow<Unit>()
    val onShowRationaleForFineLocation: SharedFlow<Unit> = _onShowRationaleForFineLocation

    fun onZoomToLocationClicked(
        shouldShowRationale: (String) -> Boolean,
        requestPermissions: (Array<String>) -> Unit
    ) {
        when {
            haveCoarseLocationPermission -> {
                viewModelScope.launch {
                    _moveToUserLocationEvent.emit(Unit)
                }
            }
            shouldShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                viewModelScope.launch {
                    _onShowRationaleForCoarseLocation.emit(Unit)
                }
            }
            else -> {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
    }

    fun onZoomToFineLocationClicked(
        shouldShowRationale: (String) -> Boolean,
        requestPermission: (String) -> Unit
    ) {
        when {
            haveFineLocationPermission -> {
                viewModelScope.launch {
                    _moveToUserLocationEvent.emit(Unit)
                }
            }
            shouldShowRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModelScope.launch {
                    _onShowRationaleForFineLocation.emit(Unit)
                }
            }
            else -> {
                requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }
}