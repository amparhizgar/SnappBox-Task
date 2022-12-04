package ir.amirhparhizgar.snappboxtask.domain

/**
 * Created by AmirHossein Parhizgar on 12/5/2022.
 */
interface CloudMessageHandler {
    fun handleMessage(data: MutableMap<String, String>)
}