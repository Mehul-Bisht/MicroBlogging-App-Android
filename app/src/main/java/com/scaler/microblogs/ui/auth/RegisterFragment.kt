package com.scaler.microblogs.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.scaler.microblogs.databinding.FragmentRegisterBinding
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    val viewModel by viewModel<AuthViewModel>()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToLogin.setOnClickListener {

            if (view.findNavController().previousBackStackEntry != null) {
                view.findNavController().popBackStack()
            } else {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                view.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}