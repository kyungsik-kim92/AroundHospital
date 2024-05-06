package com.example.presenter.ui.bookmark

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.presenter.R
import com.example.presenter.adapter.BookmarkAdapter
import com.example.presenter.base.BaseFragment
import com.example.presenter.base.ViewEvent
import com.example.presenter.base.ViewState
import com.example.presenter.databinding.FragmentBookmarkBinding
import com.example.presenter.home.HomeViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {
    override val viewModel by viewModels<BookmarkViewModel>()

    private val parentViewModel by viewModels<HomeViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val bookmarkAdapter =
        BookmarkAdapter { item ->
            parentViewModel.moveItem(item)
        }


    override fun initUi() {

    }

    override fun onChangedViewState(state: ViewState) {
        binding.rvBookmark.adapter = bookmarkAdapter
        viewModel.viewEvent.map(::onChangeViewEvent).launchIn(lifecycleScope)
    }

    override fun onChangeViewEvent(event: ViewEvent) {
        when (event) {
            is BookmarkViewEvent.GetBookmarkList -> {
                bookmarkAdapter.submitList(event.bookmarkList)
            }
        }
    }
}