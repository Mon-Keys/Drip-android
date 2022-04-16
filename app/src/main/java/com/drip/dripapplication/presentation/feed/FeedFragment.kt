package com.drip.dripapplication.presentation.feed

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.App
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.FeedFragmentBinding
import com.drip.dripapplication.databinding.PhotoItemBinding
import com.drip.dripapplication.databinding.ProfileFragmentBinding
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.GetUserInfoUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase
import com.drip.dripapplication.presentation.profile.PhotoRecycleAdapter
import com.drip.dripapplication.presentation.profile.ProfileViewModel
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    //ViewBinding
    private var _binding: FeedFragmentBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private lateinit var viewModel: FeedViewModel

    //ViewPager
    private lateinit var viewPager: ViewPager2

    //Adapter
    private lateinit var adapter: PhotoRecycleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PhotoRecycleAdapter()

        viewPager = binding.photo

        val appContainer = (activity?.application as App).appContainer

        viewModel = FeedViewModel(GetFeedUseCase(appContainer.userRepository), SetReactionUseCase(appContainer.likeRepository))

        binding.viewPagerIndicator.setupWithViewPager(viewPager)

        initViewPager()

        initObservers()

        //ViewPager buttons
        binding.buttonNext.setOnClickListener{
            if (viewPager.currentItem < adapter.itemCount) viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }

        binding.buttonPrev.setOnClickListener{
            if (viewPager.currentItem > 0) viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }

        //Animations
        binding.mainLayout.setTransitionListener(object : TransitionAdapter(){
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.toPass-> {
                        motionLayout.progress = 0f
                        motionLayout.transitionToStart()
                        viewModel.userInfo.value?.let { viewModel.swipe(it.id, 0) }
                    }
                    R.id.toLike -> {
                        motionLayout.progress = 0f
                        motionLayout.transitionToStart()
                        viewModel.userInfo.value?.let { viewModel.swipe(it.id, 1) }
                    }
                }
            }
        })

        binding.like.setOnClickListener {
            binding.mainLayout.transitionToState(R.id.toLike)
        }

        binding.dislike.setOnClickListener {
            binding.mainLayout.transitionToState(R.id.toPass)
        }

        binding.expand.setOnClickListener {

            val progress = binding.profileCard.progress

            if (progress == 0.0f) {
                binding.profileCard.transitionToState(R.id.end)
                animationDescription(binding.descrAndTags, progress)
            }
            if (progress == 1.0f) {
                binding.profileCard.transitionToState(R.id.start)
                animationDescription(binding.descrAndTags, progress)
            }

            animationButton(binding.expand, progress)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Animations
    private fun animationButton(view: ImageButton, progress: Float){
        if (progress==0f || progress == 1f){
            view.animate()
                .rotationBy(180f)
                .setDuration(1000)
                .setInterpolator(LinearInterpolator())
                .start()
        }
    }

    private fun animationDescription(view: View, progress: Float){
        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            duration = 1000
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

    private fun initViewPager(){
        viewPager.adapter = adapter

        //ViewPager page listener
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Timber.d("onPageSelected = $position")
                when (position) {
                    0 -> binding.buttonPrev.visibility = View.INVISIBLE
                    adapter.itemCount - 1 -> binding.buttonNext.visibility = View.INVISIBLE
                    else -> {
                        binding.buttonNext.visibility = View.VISIBLE
                        binding.buttonPrev.visibility = View.VISIBLE
                    }
                }
            }


        })

    }


    private fun initObservers(){
        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (it!=null) {
                Timber.d("user=$it")
                binding.tagsLayout.removeAllViewsInLayout()

                adapter.userPhoto = it.images

                setupSlider(adapter.itemCount, viewPager.width)

                insertDataIntoTextView(it)

                viewPager.setCurrentItem(0, false)

                Timber.d("initobservers = ${viewPager.currentItem}")


                //viewPager.postDelayed({ viewPager.setCurrentItem(0, false) }, 100)
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