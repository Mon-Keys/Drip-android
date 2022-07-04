package com.drip.dripapplication.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.LoginFragmentBinding
import com.drip.dripapplication.domain.model.Credential
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    //ViewBinding
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        binding.LoginButton.setOnClickListener {
            viewModel.login(Credential(binding.inputEmail.text.toString(), binding.inputPassword.text.toString()))
        }

        binding.SignupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.inputPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.validatePassword(text)
        }

        binding.inputEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.validateEmail(text)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObservers() {
        viewModel.loginButton.observe(viewLifecycleOwner){
            binding.LoginButton.isEnabled = it
        }

        viewModel.validEmail.observe(viewLifecycleOwner){
            binding.layoutEmail.error = it.error?.let { error -> getText(error) }
        }

        viewModel.validPassword.observe(viewLifecycleOwner){
            binding.layoutPassword.error = it.error?.let { error -> getText(error) }
        }

        viewModel.isLogin.observe(viewLifecycleOwner){
            if (it) findNavController().navigate(R.id.action_loginFragment_to_tabsFragment)
            else Snackbar
                .make(binding.root, R.string.error_auth, Snackbar.LENGTH_LONG)
                .show()
        }

    }
}