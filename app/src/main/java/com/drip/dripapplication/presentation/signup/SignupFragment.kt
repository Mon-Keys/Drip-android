package com.drip.dripapplication.presentation.signup

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.LoginFragmentBinding
import com.drip.dripapplication.databinding.SignupFragmentBinding
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.use_case.LoginUseCase
import com.drip.dripapplication.presentation.MainActivity
import com.drip.dripapplication.presentation.login.LoginViewModel
import java.util.regex.Pattern

class SignupFragment : Fragment() {
    //ViewBinding
    private var _binding: SignupFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as App).appContainer

        viewModel = SignupViewModel()

        initObservers()

        binding.EmailHint.isVisible = false
        binding.PasswordHint.isVisible = false
        binding.RepeatPasswordHint.isVisible = false
        binding.SignUpError.isVisible = false

        //ViewPager buttons
        binding.SignupButton.setOnClickListener {
            val isValidEmail: Boolean = Pattern.matches(
                "^[a-zA-Z0-9.!#\$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$",
                binding.Email.getText().toString()
            )
            val isValidPassword: Boolean =
                Pattern.matches("^[a-zA-z\\d]{8,20}\$", binding.Password.getText().toString())

            val isMatchPasswords: Boolean =
                (binding.Password.getText().toString() == binding.RepeatPassword.getText()
                    .toString())

            if (!isValidEmail || !isValidPassword || !isMatchPasswords) {
                binding.EmailHint.isVisible = !isValidEmail
                binding.PasswordHint.isVisible = !isValidPassword
                binding.RepeatPasswordHint.isVisible = !isValidPassword
                return@setOnClickListener
            }

            // TODO: signup
        }

        binding.LoginButton.setOnClickListener {
            // TODO: refirect to login view
        }

        binding.Email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                val isValidEmail: Boolean = Pattern.matches(
                    "^[a-zA-Z0-9.!#\$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$",
                    string
                )
                binding.EmailHint.isVisible = !isValidEmail
            }
        })

        binding.Password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                val isValidPassword: Boolean =
                    Pattern.matches("^[a-zA-z\\d]{8,20}\$", string)
                binding.PasswordHint.isVisible = !isValidPassword
            }
        })

        binding.RepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                val isMatchPasswords: Boolean =
                    (binding.Password.getText().toString() == binding.RepeatPassword.getText()
                        .toString())
                binding.RepeatPasswordHint.isVisible = !isMatchPasswords
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObservers() {
        // TODO: ovserve response from signup usecase
    }
}