package com.scaler.microblogs.ui.account

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.scaler.libconduit.models.User
import com.scaler.libconduit.requests.UserUpdateData
import com.scaler.microblogs.Constants
import com.scaler.microblogs.R
import com.scaler.microblogs.databinding.FragmentAccountEditBinding
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

class AccountEditFragment : Fragment() {

    val viewModel by viewModel<AccountViewModel>()
    private var _binding: FragmentAccountEditBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AccountEditFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radius: Float = requireContext().resources.getDimension(R.dimen.image_corner_radius)
        val shapeAppearanceModelObj: ShapeAppearanceModel =
            binding.profile.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius)
                .build()

        binding.profile.shapeAppearanceModel = shapeAppearanceModelObj

        arguments?.let {

            val username = it.getString("username")
            val bio = it.getString("bio")
            val imageUrl = it.getString("imageUrl")

            username?.let {
                binding.usernameTil.editText?.setText(it)
            }
            bio?.let {
                binding.bioTil.editText?.setText(it)
            }
            imageUrl?.let {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.default_user)
                    .into(binding.profile)
            }
        }

        binding.save.setOnClickListener {

            val username = binding.usernameTil.editText?.text.toString()
            val bio = binding.bioTil.editText?.text.toString()
            viewModel.updateAccount(
                UserUpdateData(
                    bio = bio,
                    username = username
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}