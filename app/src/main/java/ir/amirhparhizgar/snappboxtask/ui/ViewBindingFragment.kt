package ir.amirhparhizgar.snappboxtask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by AmirHossein Parhizgar on 12/5/2022.
 */

typealias ViewBindingInflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB

@Suppress("unused")
abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw  IllegalStateException("ViewBinding is null probably because improper lifecycle state.")

    abstract fun getViewBindingInflater(): ViewBindingInflater<VB>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return getViewBindingInflater().invoke(inflater, container, false).apply {
            _binding = this
        }.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}