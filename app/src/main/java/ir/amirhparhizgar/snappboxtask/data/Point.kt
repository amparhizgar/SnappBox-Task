package ir.amirhparhizgar.snappboxtask.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
@Parcelize
data class Point(
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("lat")
    val lat: Double
) : Parcelable
