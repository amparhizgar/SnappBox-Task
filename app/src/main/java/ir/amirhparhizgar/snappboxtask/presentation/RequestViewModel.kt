package ir.amirhparhizgar.snappboxtask.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.amirhparhizgar.snappboxtask.data.Request
import ir.amirhparhizgar.snappboxtask.ui.RequestFragmentArgs
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    val request: Request

    init {
        val requestJson =
            RequestFragmentArgs.fromSavedStateHandle(savedStateHandle).extraRequestJson
        request = GsonBuilder().create().fromJson(requestJson, Request::class.java)
    }
}