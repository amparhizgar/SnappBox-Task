package ir.amirhparhizgar.snappboxtask.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import dagger.hilt.android.AndroidEntryPoint
import ir.amirhparhizgar.snappboxtask.R
import ir.amirhparhizgar.snappboxtask.common.collectWithinLifecycle
import ir.amirhparhizgar.snappboxtask.databinding.FragmentHomeBinding
import ir.amirhparhizgar.snappboxtask.presentation.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mapView get() = binding.mapView

    private val requestBothLocationPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            if (map[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                configureLocationPlugin(true)
                moveToUserLocation()
            }
        }

    private val requestFineLocationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            // no action is needed
        }

    private fun requestBothLocationPermissions() {
        requestBothLocationPermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().navigate(R.id.action_homeFragment_to_requestFragment)
    }

    private fun requestFineLocationPermission() {
        requestFineLocationPermission.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.fabCurrentLocation.setOnClickListener {
            viewModel.onZoomToLocationClicked(
                shouldShowRationale = {
                    return@onZoomToLocationClicked shouldShowRequestPermissionRationale(it)
                }, requestPermissions = {
                    requestBothLocationPermissions.launch(it)
                }
            )
        }
        binding.fabCurrentLocation.setOnLongClickListener {
            viewModel.onZoomToFineLocationClicked(
                shouldShowRationale = {
                    return@onZoomToFineLocationClicked shouldShowRequestPermissionRationale(it)
                }, requestPermission = {
                    requestFineLocationPermission.launch(it)
                }
            )
            true
        }


        viewModel.onShowRationaleForCoarseLocation.collectWithinLifecycle(viewLifecycleOwner) {
            showPermissionRationalDialog(R.string.why_location_permission_required) {
                requestBothLocationPermissions()
            }
        }
        viewModel.onShowRationaleForFineLocation.collectWithinLifecycle(viewLifecycleOwner) {
            showPermissionRationalDialog(R.string.why_fine_location_permission_required) {
                requestFineLocationPermission()
            }
        }
        viewModel.moveToUserLocationEvent.collectWithinLifecycle(viewLifecycleOwner) {
            moveToUserLocation()
        }

        return binding.root
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        onLocationChanged(it)
    }

    private fun moveToUserLocation() {
        mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    private fun onLocationChanged(point: Point) {
        mapView.getMapboxMap().flyTo(
            cameraOptions {
                center(point)
                zoom(16.0)
            },
            mapAnimationOptions {
                duration(2000)
            }
        )
        mapView.location.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapBoxStyle()
        customizeMapBoxTools()
    }

    private fun setMapBoxStyle() {
        binding.mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS,
            onStyleLoaded = {
                configureLocationPlugin(viewModel.haveCoarseLocationPermission)
                if (viewModel.haveCoarseLocationPermission)
                    moveToUserLocation()
            }
        )
    }

    private fun customizeMapBoxTools() {
        ViewCompat.setOnApplyWindowInsetsListener(mapView) { _, insets ->
            val systemBarInsets =
                insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
            mapView.compass.marginTop = systemBarInsets.top.toFloat()
            mapView.logo.marginBottom = systemBarInsets.bottom.toFloat()
            mapView.scalebar.enabled = false
            mapView.attribution.enabled = false
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun configureLocationPlugin(enabled: Boolean) {
        mapView.location.updateSettings {
            this.enabled = enabled
            pulsingEnabled = true
            locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireContext(),
                    R.mipmap.bike_indicator,
                )
            )
        }
        mapView.location2.updateSettings2 {
            puckBearingEnabled = true
            puckBearingSource = PuckBearingSource.HEADING
        }
    }

    private fun showPermissionRationalDialog(
        @StringRes message: Int,
        onRequestPermission: () -> Unit
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                onRequestPermission()
            }
            .setNeutralButton(resources.getString(R.string.not_now), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}