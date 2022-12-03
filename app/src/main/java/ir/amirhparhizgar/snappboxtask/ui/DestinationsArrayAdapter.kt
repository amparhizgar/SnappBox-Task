package ir.amirhparhizgar.snappboxtask.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import ir.amirhparhizgar.snappboxtask.R
import ir.amirhparhizgar.snappboxtask.databinding.ItemDestinationBinding


/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
data class Destination(val index: Int, val address: String, val lat: Float, val lng: Float)
class DestinationsArrayAdapter(data: ArrayList<Destination>, context: Context) :
    ArrayAdapter<Destination>(context, R.layout.item_destination, data) {
    var mContext: Context

    init {
        mContext = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_destination, parent, false)
        val model = getItem(position)!!
        with(ItemDestinationBinding.bind(view)) {
            root.tag = position
            tvDestinationIndex.text =
                if (position == 0) ""
                else "%d".format(model.index)
            tvAddress.text = model.address
            tvDestinationIndex.background =
                AppCompatResources.getDrawable(
                    context,
                    if (position == 0) R.drawable.origin_icon else R.drawable.destination_icon
                )
        }
        return view
    }
}