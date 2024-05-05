package com.example.aroundhospital.ui.splash

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.aroundhospital.R
import com.example.aroundhospital.base.BaseFragment
import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.aroundhospital.databinding.FragmentSplashBinding
import com.example.aroundhospital.routeHomeFragment
import com.example.aroundhospital.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {


    override val viewModel by viewModels<SplashViewModel>()

    private val permissionResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            onChangedViewState(SplashViewState.RouteMap)
        } else {
            onChangeViewEvent(ViewEvent.ShowToast("위치 권한을 허용해주세요."))
        }
    }

    override fun initUi() {

    }

    override fun onChangedViewState(state: ViewState) {
        when (state) {
            is SplashViewState.RouteMap -> routeHomeFragment()
            is SplashViewState.RequestPermission -> {
                permissionResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.ShowToast -> showToast(event.message)
        }
    }
}