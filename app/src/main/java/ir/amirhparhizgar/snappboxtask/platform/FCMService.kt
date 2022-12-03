package ir.amirhparhizgar.snappboxtask.platform

import android.annotation.SuppressLint
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.amirhparhizgar.snappboxtask.R
import ir.amirhparhizgar.snappboxtask.ui.RequestFragmentArgs


/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("SnappBoxTask", "Message data payload: " + remoteMessage.data)
            val requestJson = remoteMessage.data["request"]
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

        NavDeepLinkBuilder(baseContext)
            .setGraph(R.navigation.home_nav_graph)
            .setDestination(R.id.requestFragment)
            .setArguments(RequestFragmentArgs(requestJson).toBundle())
            .createPendingIntent()
            .send()
    }
}