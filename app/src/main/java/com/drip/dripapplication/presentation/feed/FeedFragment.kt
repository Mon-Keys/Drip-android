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
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.FeedFragmentBinding
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase
import com.drip.dripapplication.presentation.findTopNavController
import com.drip.dripapplication.presentation.profile.PhotoRecycleAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        Timber.d("onCreateView")
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as App).appContainer

        Timber.d("_binding = $_binding")

        viewModel = ViewModelProvider(this ,ViewModelsFactory(GetFeedUseCase(appContainer.userRepository), SetReactionUseCase(appContainer.likeRepository)))[FeedViewModel::class.java]
        //viewModel = FeedViewModel by viewModels{ViewModelsFactory(GetFeedUseCase(appContainer.userRepository), SetReactionUseCase(appContainer.likeRepository))}

        setupViewPager()

        setupReactions()

        setupExpandAnimations()


        //viewModel.getCurrentUser()

        initObserverUiState()



    }

    //Animations
    private fun setupReactions(){

        //Transition listener
        binding.feedMotionLayout.mainLayout.setTransitionListener(object : TransitionAdapter(){
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
        binding.feedMotionLayout.profileCard.like.setOnClickListener {
            binding.feedMotionLayout.mainLayout.transitionToState(R.id.toLike)
        }

        binding.feedMotionLayout.profileCard.dislike.setOnClickListener {
            binding.feedMotionLayout.mainLayout.transitionToState(R.id.toPass)
        }
    }

    private fun setupExpandAnimations(){
        binding.feedMotionLayout.profileCard.expand.setOnClickListener {

            val progress = binding.feedMotionLayout.profileCard.root.progress

            if (progress == 0.0f) {
                binding.feedMotionLayout.profileCard.root.transitionToState(R.id.end)
            }
            if (progress == 1.0f) {
                binding.feedMotionLayout.profileCard.root.transitionToState(R.id.start)
            }

            animationDescription(binding.feedMotionLayout.profileCard.descrAndTags, progress)

            animationButton(binding.feedMotionLayout.profileCard.expand, progress)

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

    private fun setupViewPager(){
        viewPager = binding.feedMotionLayout.profileCard.photo

        viewPager.adapter = adapter

        //Disable swipes
        viewPager.isUserInputEnabled = false

        //ViewPager page listener
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeVPNavButtonsVisibility(position, adapter.itemCount)
            }
        })

        //ViewPager indicator (slider)
        binding.feedMotionLayout.profileCard.viewPagerIndicator.setupWithViewPager(viewPager)

        //ViewPager buttons
        binding.feedMotionLayout.profileCard.buttonNext.setOnClickListener{
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }

        binding.feedMotionLayout.profileCard.buttonPrev.setOnClickListener{
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }


    }


    private fun initObserverUiState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                Timber.d("uiState = $it")
                binding.loading.isVisible = it.isLoading

                if (it.match != null){
                    val bundle = bundleOf("matchUser" to it.match)
                    findTopNavController().navigate(R.id.action_tabsFragment_to_matchFragment, bundle)
                }else{
                    binding.feedMotionLayout.root.progress = 0f
                    binding.feedMotionLayout.root.transitionToStart()
                }

                if (it.userCard != null){
                    val user = it.userCard
                    binding.feedMotionLayout.profileCard.tagsLayout.removeAllViewsInLayout()

                    adapter.userPhoto = user.images

//                    viewPager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                        override fun onGlobalLayout() {
//                            setupSlider(adapter.itemCount, viewPager.width)
//                            viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                        }
//
//                    })

                    insertDataIntoTextView(user)

                    binding.feedMotionLayout.root.isVisible = true
                }else {
                    binding.feedMotionLayout.root.isVisible = false
                }

                binding.endOfFeed.root.isVisible = it.isEndOfList

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

        binding.feedMotionLayout.profileCard.viewPagerIndicator.apply {
            setSliderWidth(sliderWidth)
            setSliderGap(convertDpToPixels(7))
            setSliderHeight(convertDpToPixels(5))
            notifyDataChanged()

        }

    }

    private fun insertDataIntoTextView(user: User){
        val nameWithComma = "${user.name},"
        binding.feedMotionLayout.profileCard.name.text = nameWithComma

        binding.feedMotionLayout.profileCard.age.text = user.age.toString()
        binding.feedMotionLayout.profileCard.description.text = user.description

        //Tags
        for (i in user.tags) {
            val view = generateTextView(i)
            binding.feedMotionLayout.profileCard.tagsLayout.addView(view)
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
                binding.feedMotionLayout.profileCard.buttonPrev.visibility = View.INVISIBLE
                if (viewPagerItemsCount == 1) binding.feedMotionLayout.profileCard.buttonNext.visibility = View.INVISIBLE
                else binding.feedMotionLayout.profileCard.buttonNext.visibility = View.VISIBLE
            }
            else -> {
                binding.feedMotionLayout.profileCard.buttonPrev.visibility = View.VISIBLE
                if (position == viewPagerItemsCount - 1) binding.feedMotionLayout.profileCard.buttonNext.visibility = View.INVISIBLE
                else binding.feedMotionLayout.profileCard.buttonNext.visibility = View.VISIBLE
            }
        }
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