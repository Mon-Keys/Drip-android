package com.drip.dripapplication.presentation.profile

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.R
import com.drip.dripapplication.data.utils.SharedPrefs
import com.drip.dripapplication.databinding.ProfileFragmentBinding
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.presentation.findTopNavController
import com.drip.dripapplication.presentation.profile.viewModel.ProfileViewModel
import com.drip.dripapplication.presentation.adapter.PhotoRecycleAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    //ViewBinding
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val viewModel: ProfileViewModel by viewModels()

    //ViewPager
    private lateinit var viewPager: ViewPager2

    //Adapter
    private val adapter: PhotoRecycleAdapter = PhotoRecycleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()

        initObservers()

        viewModel.getUserInfo()

        binding.settings.setOnClickListener{
            val bundle = bundleOf("userFromProfile" to viewModel.userInfo.value)
            findTopNavController().navigate(R.id.profileEditFragment, bundle)
        }

        //Refresh
        binding.refreshLayout.setColorSchemeResources(R.color.purple_700);
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getUserInfo()
            binding.refreshLayout.isRefreshing = false
        }

        binding.logOut.setOnClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle(getString(R.string.alert_dialog_logout_title))
                .setMessage(getString(R.string.alert_dialog_logout_text))
                .setPositiveButton(getString(R.string.alert_dialog_logout_positive_button)
                ) { _, _ ->
                    SharedPrefs.authToken = ""
                    findTopNavController().navigate(R.id.loginFragment, null, navOptions{
                        popUpTo(R.id.tabsFragment){
                            inclusive = true
                        }
                    })
                }
                .setNeutralButton(getString(R.string.alert_dialog_logout_neutral_button)
                ){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Converter dp to pixels
    private fun convertDpToPixels(dp: Int) = dp * resources.displayMetrics.density

    private fun setupViewPager(){
        viewPager = binding.photo

        viewPager.adapter = adapter

        //ViewPager page listener
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when  {
                    adapter.itemCount == 1 -> {
                        binding.tabLayout.visibility = View.INVISIBLE
                        binding.buttonPrev.visibility = View.INVISIBLE
                        binding.buttonNext.visibility = View.INVISIBLE
                    }
                    position == 0 -> {
                        binding.buttonPrev.visibility = View.INVISIBLE
                        binding.buttonNext.visibility = View.VISIBLE
                    }
                    position == adapter.itemCount - 1 ->{
                        binding.tabLayout.visibility = View.VISIBLE
                        binding.buttonPrev.visibility = View.VISIBLE
                        binding.buttonNext.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.tabLayout.visibility = View.VISIBLE
                        binding.buttonNext.visibility = View.VISIBLE
                        binding.buttonPrev.visibility = View.VISIBLE
                    }
                }
            }
        })

        //ViewPager buttons
        binding.buttonNext.setOnClickListener{
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }

        binding.buttonPrev.setOnClickListener{
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { _, _ ->}.attach()

    }

    private fun initObservers(){
        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding.loading.isVisible = it
            binding.profileCard.isVisible = !it
        }

        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                Timber.d("user=$it")

                binding.tagsLayout.removeAllViews()
                adapter.userPhoto = it.images

                insertDataIntoTextView(it)

                viewPager.setCurrentItem(0,false)

            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            Snackbar
                .make(binding.root, it, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottom_nav)
                .show()
        }
    }

    private fun insertDataIntoTextView(user: User){
        val nameWithComma = "${user.name},"
        binding.name.text = nameWithComma



        binding.age.text = user.age.toString()
        binding.description.text = user.description

        //Tags
        for (i in user.tags) {
            val view = generateTextView(binding.root.context, i)
            binding.tagsLayout.addView(view)
        }


    }

    private fun generateTextView(context: Context, tag: String): View {
        return TextView(context).apply {
            textSize = 14f
            text = tag
            setPadding(
                convertDpToPixels(7).toInt(),
                0,
                convertDpToPixels(7).toInt(),
                convertDpToPixels(3).toInt()
            )
            setTextColor(Color.WHITE)
            val typeFace = ResourcesCompat.getFont(context, R.font.futura_bold)
            typeface = typeFace
            setBackgroundResource(R.drawable.tags_form)
        }
    }
}