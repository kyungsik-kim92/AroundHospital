package com.example.presenter.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.presenter.R
import com.example.presenter.databinding.FragmentMapBinding
import com.example.presenter.ext.hidePOIInfoContainer
import com.example.presenter.ext.showPOIInfoContainer
import com.example.presenter.home.HomeUiEvent
import com.example.presenter.home.HomeViewModel
import com.example.presenter.kakaomap.KakaoMapManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var kakaoMapManager: KakaoMapManager

    private val viewModel by viewModels<MapViewModel>()
    private val parentViewModel by viewModels<HomeViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiEvent()
        observeParentEvent()
    }

    private fun initUi() {
        kakaoMapManager.init(binding.mapView)

        viewLifecycleOwner.lifecycleScope.launch {
            kakaoMapManager.kakaoMapEventFlow.collect { event ->
                viewModel.kakaoMapEvent(event)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is MapUiEvent.MoveCamera -> {
                            kakaoMapManager.moveCamera(event.cameraUpdate)
                        }

                        is MapUiEvent.GetHospitals -> {
                            kakaoMapManager.addLabels(event.list)
                        }

                        is MapUiEvent.ShowPOIItem -> {
                            binding.viewMapInfo.item = event.item
                            binding.viewMapInfo.root.showPOIInfoContainer(requireContext())
                            viewModel.updateMapInfoState(true, event.item)
                        }

                        is MapUiEvent.HidePOIItem -> {
                            binding.viewMapInfo.root.hidePOIInfoContainer(requireContext())
                        }
                    }
                }
            }
        }
    }

    private fun observeParentEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                parentViewModel.uiEvent.collect { event ->
                    when (event) {
                        is HomeUiEvent.MoveToMap -> {
                            viewModel.moveItem(kakaoMapManager.getLabel(event.item))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
