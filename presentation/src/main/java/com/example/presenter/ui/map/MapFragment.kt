package com.example.presenter.ui.map

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.presenter.R
import com.example.presenter.base.BaseFragment
import com.example.presenter.base.ViewEvent
import com.example.presenter.base.ViewState
import com.example.presenter.databinding.FragmentMapBinding
import com.example.presenter.ext.hidePOIInfoContainer
import com.example.presenter.ext.showPOIInfoContainer
import com.example.presenter.ext.showToast
import com.example.presenter.home.HomeViewEvent
import com.example.presenter.home.HomeViewModel
import com.example.presenter.kakaomap.KakaoMapManager
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
