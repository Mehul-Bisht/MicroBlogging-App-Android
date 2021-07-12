package com.scaler.microblogs.ui.tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.scaler.microblogs.databinding.FragmentTagsBinding
import com.scaler.microblogs.databinding.FragmentTagsDetailsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

class TagsDetailsFragment : Fragment() {

    val viewModel by viewModel<TagsViewModel>()
    private var _binding: FragmentTagsDetailsBinding? = null
    private val binding get() = _binding!!
    private val arg: TagsDetailsFragmentArgs by navArgs()

    companion object {
        fun newInstance() = TagsDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagsDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tag = arg.tag as String

        viewModel.searchTag(tag)

        val adapter = TagDetailsAdapter(requireContext())
        adapter.setOnItemClick {


        }

        binding.recyclerview.adapter = adapter

        lifecycleScope.launchWhenStarted {

            viewModel.currentTagDetail.collect { state ->

                when(state) {

                    is TagsViewModel.TagsDetailState.Initial -> Unit

                    is TagsViewModel.TagsDetailState.Loading -> {

                     binding.progress.visibility = View.VISIBLE
                    }

                    is TagsViewModel.TagsDetailState.Success -> {

                        binding.progress.visibility = View.GONE

                        state.tagDetails?.let {
                            adapter.submitList(it.articles)
                        }
                    }

                    is TagsViewModel.TagsDetailState.Error -> {

                        binding.progress.visibility = View.GONE
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