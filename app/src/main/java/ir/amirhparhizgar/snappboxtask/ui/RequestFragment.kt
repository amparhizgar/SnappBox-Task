package ir.amirhparhizgar.snappboxtask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.amirhparhizgar.snappboxtask.databinding.FragmentRequestBinding
import ir.amirhparhizgar.snappboxtask.presentation.RequestViewModel

@AndroidEntryPoint
class RequestFragment : Fragment() {

    private val viewModel: RequestViewModel by viewModels()
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        with(binding) {
            tvPrice.doOnLayout {
                tvPrice.translationY = -tvPrice.measuredHeight.toFloat() / 2
            }
            list.adapter = DestinationsArrayAdapter(
                arrayListOf(
                    Destination(0, "Hello", 0f, 0f),
                    Destination(1, "Yellow", 0f, 0f),
                    Destination(2, "Blue", 0f, 0f)
                ),
                requireContext()
            )
            list.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position: Int, _ ->
                    val destination = list.getItemAtPosition(position) as Destination
                    // todo fly to destination
                }
            ViewCompat.setOnApplyWindowInsetsListener(spacer) { _, insets ->
                val systemBarInsets =
                    insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
                spacer.minimumHeight = systemBarInsets.bottom
                spacer.requestLayout()
                WindowInsetsCompat.CONSUMED
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}