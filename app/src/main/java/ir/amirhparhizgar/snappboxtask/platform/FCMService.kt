package ir.amirhparhizgar.snappboxtask.platform

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh") // it's not a production app!
class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("SnappBoxTask", "From: " + remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("SnappBoxTask", "Message data payload: " + remoteMessage.data)
        }

    }
}