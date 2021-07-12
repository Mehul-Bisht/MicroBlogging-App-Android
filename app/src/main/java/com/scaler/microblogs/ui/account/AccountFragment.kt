package com.scaler.microblogs.ui.account

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.scaler.libconduit.models.User
import com.scaler.microblogs.Constants
import com.scaler.microblogs.R
import com.scaler.microblogs.databinding.FragmentAccountBinding
import kotlinx.coroutines.flow.collect
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireActivity().getSharedPreferences(
            Constants.SIGN_IN_STATUS,
            MODE_PRIVATE
        )

        val token = sharedPrefs.getString(Constants.USER_TOKEN, "") ?: ""

        viewModel.getAccount(token)

        val radius: Float = requireContext().resources.getDimension(R.dimen.image_corner_radius)
        val shapeAppearanceModelObj: ShapeAppearanceModel =
            binding.profile.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius)
                .build()

        binding.profile.shapeAppearanceModel = shapeAppearanceModelObj

        lifecycleScope.launchWhenStarted {

            viewModel.accountStatus.collect { state ->

                when (state) {

                    is AccountViewModel.AccountState.Initial -> Unit

                    is AccountViewModel.AccountState.Loading -> {


                    }

                    is AccountViewModel.AccountState.Success -> {

                        val user: User? = state.data?.user

                        user?.let {

                            binding.username.text = it.username
                            binding.email.text = it.email
                            binding.bio.text = it.bio

                            it.image?.let {

                                val imageUrl = it.toString()
                                Glide.with(requireContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.default_user)
                                    .into(binding.profile)
                            }

                            binding.editProfile.setOnClickListener { view ->

                                val bundle = bundleOf(
                                    "username" to it.username,
                                    "bio" to it.bio,
                                    "imageUrl" to it.image
                                )

                                view.findNavController().navigate(
                                    R.id.action_nav_account_to_accountEditFragment,
                                    bundle
                                )
                            }
                        }
                    }

                    is AccountViewModel.AccountState.Error -> {


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