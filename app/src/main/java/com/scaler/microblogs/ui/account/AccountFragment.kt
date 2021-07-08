package com.scaler.microblogs.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scaler.microblogs.databinding.FragmentAccountBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    val viewModel by viewModel<AccountViewModel>()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}