package com.example.aroundhospital.home

import androidx.fragment.app.viewModels
import com.example.aroundhospital.R
import com.example.aroundhospital.adapter.FragmentPagerAdapter
import com.example.aroundhospital.base.BaseFragment
import com.example.aroundhospital.base.ViewEvent
import com.example.aroundhospital.base.ViewState
import com.example.aroundhospital.bookmark.BookmarkFragment
import com.example.aroundhospital.databinding.FragmentHomeBinding
import com.example.aroundhospital.map.MapFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override val viewModel by viewModels<HomeViewModel>()

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.icon = resources.obtainTypedArray(R.array.home_tab_icon).getDrawable(position)
        }

    override fun initUi() {
        val list = listOf(MapFragment(), BookmarkFragment())

        val pagerAdapter =
            FragmentPagerAdapter(list, childFragmentManager, viewLifecycleOwner.lifecycle)

        with(binding) {
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = list.size
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()
        }
    }

    override fun onChangedViewState(state: ViewState) {

    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is HomeViewEvent.MoveItem -> {
                binding.viewPager.setCurrentItem(INDEX_MAP_FRAGMENT, false)
            }
        }
    }


    companion object {
        private const val INDEX_MAP_FRAGMENT = 0
    }

}