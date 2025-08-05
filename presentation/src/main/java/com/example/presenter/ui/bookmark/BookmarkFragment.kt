package com.example.presenter.ui.bookmark

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
import com.example.presenter.adapter.BookmarkAdapter
import com.example.presenter.databinding.FragmentBookmarkBinding
import com.example.presenter.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BookmarkViewModel>()

    private val parentViewModel by viewModels<HomeViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private val bookmarkAdapter =
        BookmarkAdapter { item ->
            parentViewModel.moveItem(item)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeUiState()
    }

    private fun initUi() {
        binding.rvBookmark.adapter = bookmarkAdapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is BookmarkUiState.Loading -> {}
                        is BookmarkUiState.Success -> {
                            bookmarkAdapter.submitList(state.bookmarkList)
                        }

                        is BookmarkUiState.Error -> {}
                    }
                }
            }
        }
    }
}