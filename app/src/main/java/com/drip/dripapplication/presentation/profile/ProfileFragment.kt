package com.drip.dripapplication.presentation.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.drip.dripapplication.App
import com.drip.dripapplication.databinding.ProfileFragmentBinding
import com.drip.dripapplication.domain.use_case.GetUserInfoUseCase

class ProfileFragment : Fragment() {

    //ViewBinding
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val appContainer = (activity?.application as App).appContainer
        viewModel = ProfileViewModel(GetUserInfoUseCase(appContainer.userRepository))
        // TODO: Use the ViewModel
        viewModel.getUserInfo()
        viewModel.loadingState.observe(viewLifecycleOwner,{
            binding.loading.isVisible = it
            binding.profileCard.isVisible = !it
        })
        viewModel.userInfo.observe(viewLifecycleOwner,{
            binding.name.text = it?.name
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}