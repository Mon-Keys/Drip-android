package com.drip.dripapplication.presentation.feed

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.FeedFragmentBinding
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase
import com.drip.dripapplication.utils.adapter.PhotoRecycleAdapter
import com.drip.dripapplication.presentation.findTopNavController
import com.drip.dripapplication.presentation.feed.viewModel.FeedViewModel
import com.drip.dripapplication.presentation.feed.viewModel.ViewModelsFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class FeedFragment : Fragment() {

    //ViewBinding
    private var _binding: FeedFragmentBinding? = null
    private val binding
        get() = _binding!!

    //ViewModel
    private lateinit var viewModel: FeedViewModel

    //ViewPager
    private lateinit var viewPager: ViewPager2

    private val adapter = PhotoRecycleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as App).appContainer

        viewModel = ViewModelProvider(this ,
            ViewModelsFactory(GetFeedUseCase(appContainer.userRepository), SetReactionUseCase(appContainer.likeRepository))
        )[FeedViewModel::class.java]

        binding.errorFeedLayout.buttonTry.setOnClickListener {
            viewModel.getCurrentUser()
        }

        setupViewPagerAndIndicator()

        setupReactions()

        setupExpandAnimations()

        initObserverUiState(view)

        viewModel.getCurrentUser()

    }

    //Animations
    private fun setupReactions(){

        //Transition listener
        binding.mainLayout.setTransitionListener(object : TransitionAdapter(){
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.toPass-> {
                        viewModel.swipe(viewModel.uiState.value.userCard?.id ?: -1, 0)
                    }
                    R.id.toLike -> {
                        viewModel.swipe(viewModel.uiState.value.userCard?.id ?: -1, 1)
                    }
                }
            }
        })

        //Buttons listeners
        binding.profileCard.like.setOnClickListener {
            binding.mainLayout.transitionToState(R.id.toLike)
        }

        binding.profileCard.dislike.setOnClickListener {
            binding.mainLayout.transitionToState(R.id.toPass)
        }
    }

    private fun setupExpandAnimations(){
        binding.profileCard.expand.setOnClickListener {

            val progress = binding.profileCard.root.progress

            if (progress == 0.0f) {
                binding.profileCard.root.transitionToState(R.id.end)
            }
            if (progress == 1.0f) {
                binding.profileCard.root.transitionToState(R.id.start)
            }

            animationDescription(binding.profileCard.descrAndTags, progress)

            animationButton(binding.profileCard.expand, progress)

        }
    }

    private fun animationButton(view: ImageButton, progress: Float){
        if (progress == 0f || progress == 1f){
            view.animate()
                .rotationBy(180f)
                .setDuration(1000)
                .setInterpolator(LinearInterpolator())
                .start()
        }
    }

    private fun animationDescription(view: View, progress: Float){
        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            duration = 500
            if (progress == 1f) reverse()
            else start()
        }

    }

    //Converter dp to pixels
    private fun convertDpToPixels(dp: Int) = dp * resources.displayMetrics.density

    //Calculate slider width
    private fun calculateSliderWidth(itemsCount: Int, parentWidth: Int, horizontalMargin: Int = 60, sliderGap: Float): Float{
        val marginInPx = convertDpToPixels(horizontalMargin)
        return (parentWidth - marginInPx - (sliderGap*(itemsCount-1)))/itemsCount

    }

    private fun setupViewPagerAndIndicator(){
        viewPager = binding.profileCard.photo

        viewPager.adapter = adapter

        //Disable swipes
        viewPager.isUserInputEnabled = false

        //ViewPager page listener
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeVPNavButtonsVisibility(position, adapter.itemCount)
            }
        })

        val tabLayout = binding.profileCard.tabLayout
        //tabLayout.setupWithViewPager(viewPager)
        TabLayoutMediator(tabLayout, viewPager) { _, _ ->}.attach()

        //ViewPager indicator (slider)
        //binding.profileCard.viewPagerIndicator.setupWithViewPager(viewPager)

        //ViewPager buttons
        binding.profileCard.buttonNext.setOnClickListener{
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }

        binding.profileCard.buttonPrev.setOnClickListener{
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

    }


    private fun initObserverUiState(view: View){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                Timber.d("uiState = $it")
                binding.loading.isInvisible = !it.isLoading

                if (it.userCard != null){
                    binding.profileCard.root.visibility = View.VISIBLE
                    val anim = ScaleAnimation(0f,1f,0f,1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
                    anim.duration = 500
                    binding.profileCard.root.animation = anim

                    val user = it.userCard
                    binding.profileCard.tagsLayout.removeAllViewsInLayout()

                    adapter.userPhoto = user.images

                    changeVPNavButtonsVisibility(viewPager.currentItem, adapter.itemCount)

                    insertDataIntoTextView(user)

                    viewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            setupSlider(adapter.itemCount, viewPager.width)
                            viewPager.setCurrentItem(0, false)
                            //binding.feedMotionLayout.root.visibility = View.VISIBLE
                            viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }

                    })
                }else {
                    binding.profileCard.root.visibility = View.INVISIBLE
                }

                if (it.match != null){
                    val bundle = bundleOf("matchUser" to it.match)
                    findTopNavController().navigate(R.id.action_tabsFragment_to_matchFragment, bundle)
                }else {
                    binding.profileCard.root.transitionToState(R.id.start)
                    binding.mainLayout.transitionToState(R.id.start, 1)
                }

                binding.errorFeedLayout.root.isInvisible = !it.isFeedLoadingError

                binding.endOfFeed.root.isInvisible = !it.isEndOfList

                if (it.error != null)
                    Snackbar
                        .make(view, it.error, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.bottom_nav)
                        .show()
            }
        }

    }

    private fun setupSlider(numberOfItems: Int, viewPagerWidth: Int){

        val sliderGap = convertDpToPixels(7)
        val sliderWidth = calculateSliderWidth(
            numberOfItems,
            viewPagerWidth,
            sliderGap = sliderGap
        )

        Timber.d("${viewPagerWidth}")
//        binding.profileCard.viewPagerIndicator.apply {
//            setSliderWidth(sliderWidth)
//            setSliderGap(convertDpToPixels(7))
//            setSliderHeight(convertDpToPixels(5))
//            notifyDataChanged()
//        }

    }

    private fun insertDataIntoTextView(user: User){
        val nameWithComma = "${user.name},"
        binding.profileCard.name.text = nameWithComma

        binding.profileCard.age.text = user.age.toString()
        binding.profileCard.description.text = user.description

        //Tags
        for (i in user.tags) {
            val view = generateTextView(i)
            binding.profileCard.tagsLayout.addView(view)
        }


    }

    private fun generateTextView(tag: String): View {
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
            val typeFace = ResourcesCompat.getFont(context.applicationContext, R.font.futura_bold)
            typeface = typeFace
            setBackgroundResource(R.drawable.tags_form)
        }
    }

    private fun changeVPNavButtonsVisibility(position: Int, viewPagerItemsCount: Int){
        when (position) {
            0 -> {
                binding.profileCard.buttonPrev.visibility = View.INVISIBLE
                if (viewPagerItemsCount == 1) {
                    binding.profileCard.buttonNext.visibility = View.INVISIBLE
                    binding.profileCard.tabLayout.visibility = View.INVISIBLE
                }
                else {
                    binding.profileCard.buttonNext.visibility = View.VISIBLE
                    binding.profileCard.tabLayout.visibility = View.VISIBLE
                }
            }
            else -> {
                binding.profileCard.buttonPrev.visibility = View.VISIBLE
                if (position == viewPagerItemsCount - 1) binding.profileCard.buttonNext.visibility = View.INVISIBLE
                else binding.profileCard.buttonNext.visibility = View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.d("onDestroyView")
    }


}