package com.scaler.microblogs.ui.feed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.scaler.microblogs.R
import com.scaler.microblogs.databinding.FragmentFeedBinding
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {

    val viewModel by viewModel<FeedViewModel>()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = FeedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FeedAdapter()
        binding.recyclerview.adapter = adapter
        adapter.setOnItemClick {

        }

        viewModel.getFeed()

        lifecycleScope.launchWhenStarted {

            viewModel.feedState.collect { state ->

                when(state) {

                    is FeedViewModel.FeedState.Initial -> Unit

                    is FeedViewModel.FeedState.Loading -> {}

                    is FeedViewModel.FeedState.Success -> {

                        adapter.submitList(state.data?.articles)
                    }

                    is FeedViewModel.FeedState.Error -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}