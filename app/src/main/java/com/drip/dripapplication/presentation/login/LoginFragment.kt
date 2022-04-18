package com.drip.dripapplication.presentation.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import android.content.Intent
import java.util.regex.Pattern
import com.drip.dripapplication.App
import com.drip.dripapplication.databinding.LoginFragmentBinding
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.use_case.LoginUseCase
import com.drip.dripapplication.presentation.MainActivity


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

        //ViewPager buttons
        binding.LoginButton.setOnClickListener {
            val isValidEmail: Boolean = Pattern.matches(
                "^[a-zA-Z0-9.!#\$%&'*+=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$",
                binding.Email.getText().toString()
            )
            val isValidPassword: Boolean =
                Pattern.matches("^[a-zA-z\\d]{8,20}\$", binding.Password.getText().toString())

            if (!isValidEmail || !isValidPassword) {
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
            // TODO: redirect to signup view
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
                val intent = Intent(
                    requireContext(),
                    MainActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("Username",true)
                startActivity(intent)
            }
        }

    }
}