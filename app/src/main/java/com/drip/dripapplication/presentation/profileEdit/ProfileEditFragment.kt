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
import androidx.navigation.fragment.findNavController
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.ProfileEditFragmentBinding
import com.drip.dripapplication.domain.use_case.EditProfileUseCase
import com.drip.dripapplication.domain.use_case.GetProfileEditUseCase
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.UserRequest
import timber.log.Timber
import java.util.regex.Pattern

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

        viewModel = ProfileEditViewModel(
            GetProfileEditUseCase(appContainer.userRepository),
            EditProfileUseCase(appContainer.profileRepository)
        ) // TODO edit!!!

        binding.nameHint.isVisible = false
        binding.dateHint.isVisible = false

        binding.day.filters += InputFilter.LengthFilter(2)
        binding.month.filters += InputFilter.LengthFilter(2)
        binding.year.filters += InputFilter.LengthFilter(4)

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
            findNavController().navigate(R.id.action_profileEditFragment_to_profileFragment)
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
                listOf("https://sun9-68.userapi.com/s/v1/if2/4eB6CMU1pYvI8PU4zmzg1Qieiqt5YTNFDOrUnM7uyCJNfUo1GDJzwtDsQjWzIB1IV6bXHs0xOmdP7xVRAZC6FQSl.jpg?size=2160x2160&quality=96&type=album"),
                listOf("Fowler", "Beck", "Evans")
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
                findNavController().navigate(R.id.action_profileEditFragment_to_profileFragment)
            }
        }
    }
}