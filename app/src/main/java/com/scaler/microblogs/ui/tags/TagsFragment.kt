package com.scaler.microblogs.ui.tags

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.scaler.microblogs.databinding.FragmentTagsBinding
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

class TagsFragment : Fragment() {

    val viewModel by viewModel<TagsViewModel>()
    private var _binding: FragmentTagsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = TagsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTags()

        val adapter = TagsAdapter()
        adapter.setOnItemClick {

            viewModel.searchByTag(it)
            val action = TagsFragmentDirections.actionNavTagsToTagsDetailsFragment(it)
            view.findNavController().navigate(action)
        }

        binding.recyclerview.adapter = adapter

        lifecycleScope.launchWhenStarted {

            viewModel.tags.collect { state ->

                when(state) {

                    is TagsViewModel.TagsState.Initial -> Unit

                    is TagsViewModel.TagsState.Loading -> {

                        binding.progress.visibility = View.VISIBLE
                    }

                    is TagsViewModel.TagsState.Success -> {

                        binding.progress.visibility = View.GONE
                        adapter.submitList(state.tags)
                    }

                    is TagsViewModel.TagsState.Error -> {

                        binding.progress.visibility = View.GONE
                    }
                }
            }

            viewModel.tagDetails.collect {

                Log.d("dataa ","""
                    $it
                """.trimIndent())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}