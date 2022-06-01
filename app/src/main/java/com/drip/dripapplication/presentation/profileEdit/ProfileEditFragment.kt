package com.drip.dripapplication.presentation.profileEdit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.InputFilter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.bumptech.glide.Glide
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.ProfileEditFragmentBinding
import com.drip.dripapplication.domain.model.MatchUserParcelable
import com.drip.dripapplication.domain.use_case.EditProfileUseCase
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.UserRequest
import com.drip.dripapplication.presentation.findTopNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.regex.Pattern

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    //ViewBinding
    private var _binding: ProfileEditFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileEditFragment()
    }

    //ViewModel
    private val viewModel: ProfileEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileEditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //args
        val userData = arguments?.getParcelable<User>("userFromProfile")

        binding.nameHint.isVisible = false
        binding.dateHint.isVisible = false

        binding.day.filters += InputFilter.LengthFilter(2)
        binding.month.filters += InputFilter.LengthFilter(2)
        binding.year.filters += InputFilter.LengthFilter(4)

        //Hardcode
        binding.day.setText("01")
        binding.month.setText("12")
        binding.year.setText("2000")
        binding.name.setText(userData?.name)
        binding.description.setText(userData?.description)

        Glide.with(this)
            .load(userData?.images?.get(0))
            .into(binding.photo1)

        Glide.with(this)
            .load(userData?.images?.get(1))
            .into(binding.photo2)

        Glide.with(this)
            .load(userData?.images?.get(2))
            .into(binding.photo3)

        Glide.with(this)
            .load(userData?.images?.get(3))
            .into(binding.photo4)


        initObservers()

        binding.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
                val isValidName: Boolean = Pattern.matches(
                    "^[a-zA-Zа-яА-Я' ]{2,18}\$",
                    string
                )
                binding.nameHint.isVisible = !isValidName
            }
        })

        binding.day.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
//                if (string != "") {
//                    if (Integer.parseInt(string.toString()) > 31) {
//                        binding.dateHint.isVisible = true
//                    }
//                }
            }
        })
        binding.month.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
//                if (string != "") {
//                    if (Integer.parseInt(string.toString()) > 12) {
//                        binding.dateHint.isVisible = true
//                    }
//                }
            }
        })
        binding.year.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, s: Int, start: Int, count: Int) {}

            override fun onTextChanged(
                string: CharSequence, start: Int, before: Int, count: Int
            ) {
//                if (string != "") {
//                    if (Integer.parseInt(string.toString()) in 1950..2005) {
//                        binding.dateHint.isVisible = true
//                    }
//                }
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.saveButton.setOnClickListener {
            val isValidName: Boolean = Pattern.matches(
                "^[a-zA-Zа-яА-Я' ]{2,18}\$",
                binding.name.getText().toString()
            )

            val dayText: String = binding.day.getText().toString()
            val monthText: String = binding.month.getText().toString()
            val yearText: String = binding.year.getText().toString()
//            val isValidDay: Boolean = Integer.parseInt(dayText) > 31
//            val isValidMonth: Boolean = Integer.parseInt(monthText) > 12
//            val isValidYear: Boolean = Integer.parseInt(yearText) in 1950..2005

            if (!isValidName) {
                binding.nameHint.isVisible = !isValidName
                return@setOnClickListener
            }

            val date: String = "$yearText-$monthText-$dayText"
            if (date.length != 10) {
                binding.dateHint.isVisible = true
                return@setOnClickListener
            }

            val user = UserRequest(
                binding.name.getText().toString(),
                "2001-06-06",
                "male",
                binding.description.getText().toString(),
                "",
                userData?.images ?: emptyList(),
                listOf("Рок", "Спорт", "Футбол")
            )

            viewModel.update(user)
        }
    }

    private fun initObservers() {
        viewModel.loadingState.observe(viewLifecycleOwner) {
        }

        viewModel.status.observe(viewLifecycleOwner) {
            Timber.i("AAAAAAAAAa")
            if (it in 200..299) {
                Timber.i("EEEEEEEEEEEEEEEE")

                //Navigation
                val parent = findNavController().previousBackStackEntry?.destination?.id
                if (parent!=null) {
                    if (parent == R.id.tabsFragment) {
                        Timber.d("from profile")
                        findNavController().popBackStack()
                    }
                    else if (parent == R.id.signupFragment) {
                        Timber.d("from signup")
                        findNavController().navigate(R.id.tabsFragment,null, navOptions{
                            popUpTo(R.id.signupFragment){
                                inclusive = true
                            }
                        })
                    }
                }
            }
        }
    }
}