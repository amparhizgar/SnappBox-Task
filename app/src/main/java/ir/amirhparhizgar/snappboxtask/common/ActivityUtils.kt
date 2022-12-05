package ir.amirhparhizgar.snappboxtask.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import java.util.*


fun Activity.showOnKeyGuard() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    }
    window.addFlags(
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
    )
}

fun Context.setAppLocale(): Context {
    val locale = Locale("fa", "IR")
    Locale.setDefault(locale)
    with(resources.configuration) {
        setLocale(locale)
        setLayoutDirection(locale)
        return createConfigurationContext(this)
    }
}