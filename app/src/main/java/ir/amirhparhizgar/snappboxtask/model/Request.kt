package ir.amirhparhizgar.snappboxtask.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
@Parcelize
data class Request(
    @SerializedName("destinations")
    val destinations: ArrayList<Destination>
) : Parcelable
