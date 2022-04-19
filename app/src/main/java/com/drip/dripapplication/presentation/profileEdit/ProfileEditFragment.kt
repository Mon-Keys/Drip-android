package com.drip.dripapplication.presentation.profileEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.ProfileEditFragmentBinding
import com.drip.dripapplication.domain.use_case.GetProfileEditUseCase

class ProfileEditFragment : Fragment() {

    //ViewBinding
    private var _binding: ProfileEditFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileEditFragment()
    }

    //ViewModel
    private lateinit var viewModel: ProfileEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileEditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (activity?.application as App).appContainer

        viewModel = ProfileEditViewModel(GetProfileEditUseCase(appContainer.userRepository)) // TODO edit!!!

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_profileEditFragment_to_profileFragment)
        }
        binding.saveButton.setOnClickListener{
            findNavController().navigate(R.id.action_profileEditFragment_to_profileFragment)
        }
    }

}