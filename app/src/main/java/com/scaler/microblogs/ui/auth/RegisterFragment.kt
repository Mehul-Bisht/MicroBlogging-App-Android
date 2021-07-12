package com.scaler.microblogs.ui.auth

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.scaler.microblogs.Constants
import com.scaler.microblogs.MainActivity
import com.scaler.microblogs.databinding.FragmentRegisterBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
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

        binding.signup.setOnClickListener {

            val email = binding.email.editText?.text.toString()
            val pass = binding.password.editText?.text.toString()
            val username = binding.username.editText?.text.toString()

            viewModel.signUp(
                username,
                email,
                pass
            )
        }

        lifecycleScope.launchWhenStarted {

            async {

                viewModel.signUpProgress.collect { state ->

                    when(state) {

                        is AuthViewModel.SignUpProgress.Initial -> Unit

                        is AuthViewModel.SignUpProgress.Loading -> {

                            binding.loader.visibility = View.VISIBLE
                            binding.signup.visibility = View.INVISIBLE
                        }
                        is AuthViewModel.SignUpProgress.Error -> {

                            binding.loader.visibility = View.INVISIBLE
                            binding.signup.visibility = View.VISIBLE
                        }
                        is AuthViewModel.SignUpProgress.Completed -> {

                            binding.loader.visibility = View.GONE
                            binding.signup.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            async {

                viewModel.signUpStatus.collect { status ->

                    when(status) {

                        is AuthViewModel.SignUpState.Success -> {

                            val data = status.response

                            val sharedPrefs = requireActivity().getSharedPreferences(
                                Constants.SIGN_IN_STATUS,
                                MODE_PRIVATE
                            )

                            val editor = sharedPrefs.edit()
                            editor.putBoolean(Constants.SIGNED_IN,true)
                            editor.putString(Constants.USER_TOKEN,data?.user?.token.toString())

                            editor.apply()

                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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