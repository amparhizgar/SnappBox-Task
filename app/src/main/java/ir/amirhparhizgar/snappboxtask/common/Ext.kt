package ir.amirhparhizgar.snappboxtask.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ir.amirhparhizgar.snappboxtask.model.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Created by AmirHossein Parhizgar on 12/2/2022.
 */

typealias  MapBoxPoint = com.mapbox.geojson.Point

fun <T> Flow<T>.collectWithinLifecycle(
    viewLifecycleOwner: LifecycleOwner,
    flowCollector: FlowCollector<T>
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect(flowCollector)
        }
    }
}

fun Point.toMapBoxPoint(): MapBoxPoint {
    return MapBoxPoint.fromLngLat(lng, lat)
}