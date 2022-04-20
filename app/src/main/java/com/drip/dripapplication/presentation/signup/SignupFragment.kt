package com.drip.dripapplication.presentation.signup

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.SignupFragmentBinding
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.use_case.SignupUseCase
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

        viewModel = SignupViewModel(SignupUseCase(appContainer.authRepository))

        initObservers()

        binding.EmailHint.isVisible = false
        binding.PasswordHint.isVisible = false
        binding.RepeatPasswordHint.isVisible = false
        binding.SignUpError.isVisible = false
        binding.EmailCorrect.visibility = View.INVISIBLE
        binding.PasswordCorrect.visibility = View.INVISIBLE
        binding.RepeatPasswordCorrect.visibility = View.INVISIBLE

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
                binding.EmailHint.setTextColor(Color.RED)
                binding.PasswordHint1.setTextColor(Color.RED)
                binding.PasswordHint2.setTextColor(Color.RED)
                binding.RepeatPasswordHint.setTextColor(Color.RED)
                binding.EmailHint.isVisible = !isValidEmail
                binding.PasswordHint.isVisible = !isValidPassword
                binding.RepeatPasswordHint.isVisible = !isValidPassword
                return@setOnClickListener
            }

            viewModel.signup(
                Cridential(
                    binding.Email.getText().toString(),
                    binding.Password.getText().toString()
                )
            )
        }

        binding.LoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.Email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {
                binding.PasswordHint1.setTextColor(Color.RED)
                binding.PasswordHint2.setTextColor(Color.RED)
                binding.RepeatPasswordHint.setTextColor(Color.RED)
            }

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                binding.SignUpError.isVisible = false
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
                binding.RepeatPasswordHint.setTextColor(Color.RED)
            }

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                binding.SignUpError.isVisible = false
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

        binding.RepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {
                binding.EmailHint.setTextColor(Color.RED)
                binding.PasswordHint1.setTextColor(Color.RED)
                binding.PasswordHint2.setTextColor(Color.RED)
            }

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                binding.SignUpError.isVisible = false
                val isMatchPasswords: Boolean =
                    (binding.Password.getText().toString() == binding.RepeatPassword.getText()
                        .toString())
                binding.RepeatPasswordHint.setTextColor(Color.GRAY)
                binding.RepeatPasswordHint.isVisible = !isMatchPasswords
                if (isMatchPasswords) {
                    binding.RepeatPasswordCorrect.visibility = View.VISIBLE
                } else {
                    binding.RepeatPasswordCorrect.visibility = View.INVISIBLE
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
            binding.SignUpError.isVisible = (it == 1001)
            if (it in 200..299) {
                findNavController().navigate(R.id.action_signupFragment_to_profileEditFragment)
            }
        }
    }
}