package ir.amirhparhizgar.snappboxtask.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
@Parcelize
data class Destination(val address: String, val point: Point) : Parcelable