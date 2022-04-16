package com.drip.dripapplication.presentation.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.LoginFragmentBinding
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.use_case.LoginUseCase

class LoginFragment : Fragment() {

    //ViewBinding
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as App).appContainer

        viewModel = LoginViewModel(LoginUseCase(appContainer.authRepository))

        binding.EmailHint.visibility = View.INVISIBLE

        //ViewPager buttons
        binding.Login.setOnClickListener{
//            viewModel.login(Cridential(binding.Email.toString(), binding.Password.toString()))
        }

        binding.SignupButton.setOnClickListener{
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Converter dp to pixels
    private fun convertDpToPixels(dp: Int) = dp * resources.displayMetrics.density

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}