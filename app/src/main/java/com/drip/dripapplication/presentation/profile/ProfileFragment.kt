package com.drip.dripapplication.presentation.profile

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.data.utils.SharedPrefs
import com.drip.dripapplication.databinding.ProfileFragmentBinding
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetUserInfoUseCase
import com.drip.dripapplication.presentation.findTopNavController
import com.drip.dripapplication.presentation.profile.viewModel.ProfileViewModel
import com.drip.dripapplication.utils.adapter.PhotoRecycleAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class ProfileFragment : Fragment() {

    //ViewBinding
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private lateinit var viewModel: ProfileViewModel

    //ViewPager
    private lateinit var viewPager: ViewPager2

    //Adapter
    private lateinit var adapterWithDiffUtil: PhotoRecycleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appContainer = (activity?.application as App).appContainer

        viewModel = ProfileViewModel(GetUserInfoUseCase(appContainer.userRepository))


        adapterWithDiffUtil = PhotoRecycleAdapter()

        viewPager = binding.photo

        Timber.d("viewPagerWidth = ${viewPager.width}")

        binding.viewPagerIndicator.setupWithViewPager(viewPager)

        initViewPager()

        initObservers()

        //ViewPager buttons
        binding.buttonNext.setOnClickListener{
            if (viewPager.currentItem < adapterWithDiffUtil.itemCount) viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }

        binding.buttonPrev.setOnClickListener{
            if (viewPager.currentItem > 0) viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

        binding.settings.setOnClickListener{
            findTopNavController().navigate(R.id.profileEditFragment)
        }


        //Refresh
        binding.refreshLayout.setColorSchemeResources(R.color.purple_700);
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getUserInfo()
            binding.tagsLayout.removeAllViewsInLayout()
            binding.refreshLayout.isRefreshing = false
        }

        binding.logOut.setOnClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle(getString(R.string.alert_dialog_logout_title))
                .setMessage(getString(R.string.alert_dialog_logout_text))
                .setPositiveButton(getString(R.string.alert_dialog_logout_positive_button)
                ) { dialog, which ->
                    SharedPrefs.authToken = ""
                    findTopNavController().navigate(R.id.loginFragment)
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

    //Calculate slider width
    private fun calculateSliderWidth(itemsCount: Int, parentWidth: Int, horizontalMargin: Int = 60, sliderGap: Float): Float{
        val marginInPx = convertDpToPixels(horizontalMargin)
        return (parentWidth - marginInPx - (sliderGap*(itemsCount-1)))/itemsCount

    }

    private fun initViewPager(){
        viewPager.adapter = adapterWithDiffUtil

        //ViewPager page listener
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when  {
                    adapterWithDiffUtil.itemCount == 1 -> {
                        binding.buttonPrev.visibility = View.INVISIBLE
                        binding.buttonNext.visibility = View.INVISIBLE
                    }
                    position == 0 -> {
                        binding.buttonPrev.visibility = View.INVISIBLE
                        binding.buttonNext.visibility = View.VISIBLE
                    }
                    position == adapterWithDiffUtil.itemCount - 1 ->{
                        binding.buttonPrev.visibility = View.VISIBLE
                        binding.buttonNext.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.buttonNext.visibility = View.VISIBLE
                        binding.buttonPrev.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

    private fun initObservers(){
        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding.loading.isVisible = it
            binding.profileCard.isVisible = !it
        }

        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (it != null) {
                Timber.d("user=$it")
                //adapterWithDiffUtil.userPhoto = it.images

                setupSlider(adapterWithDiffUtil.itemCount, viewPager.width)

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

    private fun setupSlider(numberOfItems: Int, viewPagerWidth: Int){
        Timber.d("numberOfItems = $numberOfItems, width = $viewPagerWidth")
        val sliderGap = convertDpToPixels(7)
        val sliderWidth = calculateSliderWidth(
            numberOfItems,
            viewPagerWidth,
            sliderGap = sliderGap
        )

        binding.viewPagerIndicator.apply {
            setSliderWidth(sliderWidth)
            setSliderGap(convertDpToPixels(7))
            setSliderHeight(convertDpToPixels(5))
            notifyDataChanged()
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