package com.scaler.microblogs.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.scaler.microblogs.MainActivity
import com.scaler.microblogs.databinding.FragmentLoginBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    val viewModel by viewModel<AuthViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToSignin.setOnClickListener {

            if (view.findNavController().previousBackStackEntry != null) {
                view.findNavController().popBackStack()
            } else {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                view.findNavController().navigate(action)
            }
        }

        binding.login.setOnClickListener {

            val email = binding.email.editText?.text.toString()
            val pass = binding.password.editText?.text.toString()

            viewModel.login(email,pass)
        }

        lifecycleScope.launchWhenStarted {

            async {

                viewModel.loginProgress.collect { state ->

                    when(state) {

                        is AuthViewModel.LogInProgress.Initial -> Unit

                        is AuthViewModel.LogInProgress.Loading -> {

                            binding.loader.visibility = View.VISIBLE
                            binding.login.visibility = View.INVISIBLE
                        }
                        is AuthViewModel.LogInProgress.Error -> {

                            binding.loader.visibility = View.INVISIBLE
                            binding.login.visibility = View.VISIBLE
                        }
                        is AuthViewModel.LogInProgress.Completed -> {

                            binding.loader.visibility = View.GONE
                            binding.login.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            async {

                viewModel.loginStatus.collect { status ->

                    when(status) {

                        is AuthViewModel.LogInState.Success -> {

                            val data = status.response

                            Log.d("login data ","""
                                $data
                            """.trimIndent())

                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }
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