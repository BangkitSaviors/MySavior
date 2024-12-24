package com.capstone.team.mysavior.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.team.mysavior.R
import com.capstone.team.mysavior.databinding.FragmentProfileBinding
import com.capstone.team.mysavior.ui.BmiActivity
import com.capstone.team.mysavior.ui.ViewModelFactory
import com.capstone.team.mysavior.ui.setting.SettingActivity
import com.capstone.team.mysavior.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingProfile.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        setupView()
        setupAction()
        observeUserSession()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun observeUserSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->

            user.profilePicture?.let { profileUri ->
                Glide.with(this)
                    .load(profileUri)
                    .into(binding.profileImage)
            } ?: run {
                binding.profileImage.setImageResource(R.drawable.ic_person_black_24dp)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}