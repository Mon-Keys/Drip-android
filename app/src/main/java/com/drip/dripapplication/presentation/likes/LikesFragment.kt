package com.drip.dripapplication.presentation.likes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.LikesFragmentBinding
import com.drip.dripapplication.domain.use_case.GetLikesUseCase
import com.drip.dripapplication.domain.use_case.GetUserInfoUseCase

class LikesFragment : Fragment() {

    //ViewBinding
    private var _binding: LikesFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LikesFragment()
    }

    //ViewModel
    private lateinit var viewModel: LikesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.likes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (activity?.application as App).appContainer

        viewModel = LikesViewModel(GetLikesUseCase(appContainer.userRepository))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}