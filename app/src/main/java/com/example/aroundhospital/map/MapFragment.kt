package com.example.aroundhospital.map

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aroundhospital.MapManager
import com.example.aroundhospital.R
import com.example.aroundhospital.base.BaseFragment
import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.aroundhospital.databinding.FragmentMapBinding
import com.example.aroundhospital.hidePOIInfoContainer
import com.example.aroundhospital.home.HomeViewEvent
import com.example.aroundhospital.home.HomeViewModel
import com.example.aroundhospital.showPOIInfoContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import net.daum.mf.map.api.MapView


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {

    private var mapView: MapView? = null
    private var mapManager: MapManager? = null

    override val viewModel by viewModels<MapViewModel>()
    private val parentViewModel by viewModels<HomeViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    override fun initUi() {
        mapView = MapView(requireActivity())
        mapManager = MapManager(
            onMapEventType = viewModel::processMapViewEvent,
            onMapPOIItemEventType = viewModel::processPOIItemEvent
        )
        binding.containerMap.addView(mapView)
        mapView?.setMapViewEventListener(mapManager)
        mapView?.setPOIItemEventListener(mapManager)
        parentViewModel.viewEvent.map(::onChangeViewEvent).launchIn(lifecycleScope)
    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is MapViewState.MoveMapCenter -> {
                mapView?.setMapCenterPoint(state.mapPoint, true)
            }

            is MapViewState.GetPOIItems -> {
                mapView?.addPOIItems(state.poiItems)
            }
        }
    }

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

            is MapViewEvent.MoveMap -> {
                mapView?.setMapCenterPoint(event.mapPoint, true)
            }

            is HomeViewEvent.MoveItem -> {
                viewModel.moveItem(event.item)
            }
        }
    }
}
