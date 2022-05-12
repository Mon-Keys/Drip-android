package com.drip.dripapplication.presentation.login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import java.util.regex.Pattern
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
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as App).appContainer

        viewModel = LoginViewModel(LoginUseCase(appContainer.authRepository))

        initObservers()

        binding.EmailHint.isVisible = false
        binding.PasswordHint.isVisible = false
        binding.AuthError.isVisible = false
        binding.EmailCorrect.visibility = View.INVISIBLE
        binding.PasswordCorrect.visibility = View.INVISIBLE

        //ViewPager buttons
        binding.LoginButton.setOnClickListener {
            val isValidEmail: Boolean = Pattern.matches(
                "^[a-zA-Z0-9.!#\$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$",
                binding.Email.getText().toString()
            )
            val isValidPassword: Boolean =
                Pattern.matches("^[a-zA-z\\d]{8,20}\$", binding.Password.getText().toString())

            if (!isValidEmail || !isValidPassword) {
                binding.EmailHint.setTextColor(Color.RED)
                binding.PasswordHint1.setTextColor(Color.RED)
                binding.PasswordHint2.setTextColor(Color.RED)
                binding.EmailHint.isVisible = !isValidEmail
                binding.PasswordHint.isVisible = !isValidPassword
                return@setOnClickListener
            }

            viewModel.login(
                Cridential(
                    binding.Email.getText().toString(),
                    binding.Password.getText().toString()
                )
            )
        }

        binding.SignupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.Email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {
                binding.PasswordHint1.setTextColor(Color.RED)
                binding.PasswordHint2.setTextColor(Color.RED)
            }

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                binding.AuthError.isVisible = false
                val isValidEmail: Boolean = Pattern.matches(
                    "^[a-zA-Z0-9.!#\$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$",
                    string
                )
                binding.EmailHint.setTextColor(Color.GRAY)
                binding.EmailHint.isVisible = !isValidEmail
                if (isValidEmail) {
                    binding.EmailCorrect.visibility = View.VISIBLE
                } else {
                    binding.EmailCorrect.visibility = View.INVISIBLE
                }
            }
        })

        binding.Password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {
                binding.EmailHint.setTextColor(Color.RED)
            }

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                binding.AuthError.isVisible = false
                val isValidPassword: Boolean =
                    Pattern.matches("^[a-zA-z\\d]{8,20}\$", string)
                binding.PasswordHint1.setTextColor(Color.GRAY)
                binding.PasswordHint2.setTextColor(Color.GRAY)
                binding.PasswordHint.isVisible = !isValidPassword
                if (isValidPassword) {
                    binding.PasswordCorrect.visibility = View.VISIBLE
                } else {
                    binding.PasswordCorrect.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObservers() {
        viewModel.loadingState.observe(viewLifecycleOwner) {
        }

        viewModel.status.observe(viewLifecycleOwner) {
            binding.AuthError.isVisible = it in 300..500
            if (it in 200..299) {
                findNavController().navigate(R.id.action_loginFragment_to_tabsFragment)
            }
        }
    }
}