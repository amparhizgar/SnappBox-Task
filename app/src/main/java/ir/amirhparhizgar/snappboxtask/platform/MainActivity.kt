package ir.amirhparhizgar.snappboxtask.platform

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ir.amirhparhizgar.snappboxtask.R
import ir.amirhparhizgar.snappboxtask.common.setAppLocale
import ir.amirhparhizgar.snappboxtask.common.showOnKeyGuard

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.showOnKeyGuard()
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.setAppLocale())
    }
}