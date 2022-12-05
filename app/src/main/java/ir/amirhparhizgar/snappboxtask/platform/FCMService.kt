package ir.amirhparhizgar.snappboxtask.platform

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import ir.amirhparhizgar.snappboxtask.domain.CloudMessageHandler
import javax.inject.Inject


/**
 * Created by AmirHossein Parhizgar on 12/3/2022.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var cloudMessageHandler: CloudMessageHandler

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        cloudMessageHandler.handleMessage(remoteMessage.data)
    }
}