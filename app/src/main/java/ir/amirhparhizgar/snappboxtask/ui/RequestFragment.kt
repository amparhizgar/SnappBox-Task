package ir.amirhparhizgar.snappboxtask.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import dagger.hilt.android.AndroidEntryPoint
import ir.amirhparhizgar.snappboxtask.R
import ir.amirhparhizgar.snappboxtask.databinding.DestinationLocatorBinding
import ir.amirhparhizgar.snappboxtask.databinding.FragmentRequestBinding
import ir.amirhparhizgar.snappboxtask.presentation.RequestViewModel

@AndroidEntryPoint
class RequestFragment : Fragment() {

    private val viewModel: RequestViewModel by viewModels()
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!
    private val mapView get() = binding.mapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        with(binding) {
            tvPrice.doOnLayout {
                tvPrice.translationY = -tvPrice.measuredHeight.toFloat() / 2
            }
            list.adapter = DestinationsArrayAdapter(
                arrayListOf(
                    Destination(0, "Hello", 0f, 0f),
                    Destination(1, "Yellow", 0f, 0f),
                    Destination(2, "Blue", 0f, 0f)
                ),
                requireContext()
            )
            list.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position: Int, _ ->
                    val destination = list.getItemAtPosition(position) as Destination
                    // todo fly to destination
                }
            ViewCompat.setOnApplyWindowInsetsListener(spacer) { _, insets ->
                val systemBarInsets =
                    insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
                spacer.minimumHeight = systemBarInsets.bottom
                spacer.requestLayout()
                WindowInsetsCompat.CONSUMED
            }
        }

        return binding.root
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
                binding.mapView.location.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                    locationPuck = LocationPuck2D(
                        bearingImage = AppCompatResources.getDrawable(
                            requireContext(),
                            R.mipmap.bike_indicator,
                        )
                    )
                }
                binding.mapView.location2.updateSettings2 {
                    puckBearingEnabled = true
                    puckBearingSource = PuckBearingSource.HEADING
                }
                addDestinationToMap(
                    bitmapFromDrawableRes(
                        requireContext(),
                        R.drawable.origin_locator
                    )!!, Point.fromLngLat(18.16, 59.31)
                )
                addDestinationToMap(getBitmapForDestination(1), Point.fromLngLat(19.16, 59.31))
                addDestinationToMap(getBitmapForDestination(2), Point.fromLngLat(20.16, 59.31))
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

    private fun getBitmapForDestination(number: Int): Bitmap {
        val destinationViewBinding =
            DestinationLocatorBinding.inflate(LayoutInflater.from(requireContext())).apply {
                tvDestinationIndex.text = "%d".format(number)
            }
        return with(destinationViewBinding.root) {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(left, top, right, bottom)
            val bitmap = Bitmap.createBitmap(
                measuredWidth,
                measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            draw(canvas)
            bitmap
        }
    }

    private fun addDestinationToMap(bitmap: Bitmap, point: Point): PointAnnotation {
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val pointAnnotationOptions2: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconAnchor(IconAnchor.BOTTOM)
        return pointAnnotationManager.create(pointAnnotationOptions2)
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}