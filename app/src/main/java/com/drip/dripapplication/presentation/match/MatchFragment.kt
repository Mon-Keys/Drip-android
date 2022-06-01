package com.drip.dripapplication.presentation.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.FragmentMatchBinding
import com.drip.dripapplication.domain.model.MatchUserParcelable
import com.drip.dripapplication.presentation.findTopNavController
import timber.log.Timber

class MatchFragment : Fragment() {

    //ViewBinding
    private var _binding: FragmentMatchBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMatchBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userData = arguments?.getParcelable<MatchUserParcelable>("matchUser")

        binding.matchTextView.text = String.format(getString(R.string.match_with_name), userData?.name)

        Glide.with(view)
            .load("https://drip.monkeys.team/${userData?.photo}")
            //.load("${userData?.photo}")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .placeholder(R.drawable.icon_baseline_image)
            .error(R.drawable.icon_baseline_broken_image)
            .into(binding.matchPhoto)

        binding.buttonOk.setOnClickListener {
            findTopNavController().popBackStack()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.d("onDestroyView")
    }

}