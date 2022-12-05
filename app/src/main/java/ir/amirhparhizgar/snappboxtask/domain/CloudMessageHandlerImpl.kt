package ir.amirhparhizgar.snappboxtask.domain

import android.content.Context
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.amirhparhizgar.snappboxtask.R
import ir.amirhparhizgar.snappboxtask.ui.RequestFragmentArgs
import javax.inject.Inject

class CloudMessageHandlerImpl @Inject constructor(
    @ApplicationContext val context: Context
) : CloudMessageHandler {
    override fun handleMessage(data: MutableMap<String, String>) {
        if (data.isNotEmpty()) {
            Log.d("SnappBoxTask", "Message data payload: $data")
            val requestJson = data["request"]
            if (requestJson != null) {
                handleRequest(requestJson)
            }
        }
    }

    private fun handleRequest(requestJson: String) {
        /*
        * As request data is small, it is ok to pass it via intent.
        * The UI is unable to handle multiple request, so it's ok to launch the activity every time
        * a request comes.
        * */

        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.home_nav_graph)
            .setDestination(R.id.requestFragment)
            .setArguments(RequestFragmentArgs(requestJson).toBundle())
            .createPendingIntent()
            .send()
    }
}