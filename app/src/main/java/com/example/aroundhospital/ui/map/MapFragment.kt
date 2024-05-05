package com.example.aroundhospital.ui.map

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aroundhospital.R
import com.example.aroundhospital.base.BaseFragment
import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.aroundhospital.databinding.FragmentMapBinding
import com.example.aroundhospital.domain.manager.KakaoMapManager
import com.example.aroundhospital.hidePOIInfoContainer
import com.example.aroundhospital.home.HomeViewEvent
import com.example.aroundhospital.home.HomeViewModel
import com.example.aroundhospital.showPOIInfoContainer
import com.example.aroundhospital.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {

    @Inject
    lateinit var kakaoMapManager: KakaoMapManager

    override val viewModel by viewModels<MapViewModel>()
    private val parentViewModel by viewModels<HomeViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    override fun initUi() {
        kakaoMapManager.init(binding.mapView)
        kakaoMapManager.kakaoMapEventFlow.map(viewModel::kakaoMapEvent).launchIn(lifecycleScope)
        parentViewModel.viewEvent.map(::onChangeViewEvent).launchIn(lifecycleScope)
    }

    override fun onChangedViewState(state: ViewState) {}

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is MapViewEvent.ShowProgress -> {
                binding.progress.isVisible = true
            }

            is MapViewEvent.HideProgress -> {
                binding.progress.isVisible = false
            }

            is MapViewEvent.ShowMapPOIItemInfo -> {
                with(binding) {
                    fabCurrentLocation.isVisible = false
                    viewMapInfo.item = event.item
                    viewMapInfo.root.showPOIInfoContainer(requireContext())
                }
            }

            is MapViewEvent.HideMapPOIItemInfo -> {
                with(binding) {
                    fabCurrentLocation.isVisible = true
                    viewMapInfo.root.hidePOIInfoContainer(requireContext())
                }
            }

            is MapViewEvent.MoveCamera -> {
                kakaoMapManager.moveCamera(event.cameraUpdate)
            }

            is MapViewEvent.GetHospitals -> {
                kakaoMapManager.addLabels(event.list)
            }

            is HomeViewEvent.MoveItem -> {
                viewModel.moveItem(kakaoMapManager.getLabel(event.item))
            }

            is ViewEvent.ShowToast -> showToast(event.message)
        }
    }
}
