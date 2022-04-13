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
import androidx.viewpager2.widget.ViewPager2
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.FeedFragmentBinding
import com.drip.dripapplication.databinding.PhotoItemBinding
import com.drip.dripapplication.databinding.ProfileFragmentBinding
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.presentation.profile.PhotoRecycleAdapter
import kotlinx.coroutines.NonDisposableHandle.parent
import timber.log.Timber

class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
        val user = User(
        "Алиса",
        24,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "+
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
        listOf(
        "https://t3.ftcdn.net/jpg/04/64/39/50/240_F_464395072_dodQ5vK3ynIPDbD9nG82clMWJL4zUfMa.jpg",
        "https://t4.ftcdn.net/jpg/04/72/09/53/240_F_472095328_nNAedzTl57kxTyzs0wGrkvu3R2MQlvSq.jpg",
        "https://t3.ftcdn.net/jpg/04/64/85/42/240_F_464854295_GCNWjI5hPGj8hkWk7Ix8lO0xCSeo2jMc.jpg",
        "https://t3.ftcdn.net/jpg/04/64/62/34/240_F_464623498_GteYUp2YtOWCBBoIdUuEBe44Cu4HHZSN.jpg",
        "https://t3.ftcdn.net/jpg/04/64/52/06/240_F_464520694_HsFdx2BWUrKRFOdxyM1ATk5wvbqeHttR.jpg",
        "https://t4.ftcdn.net/jpg/04/65/40/27/240_F_465402775_ltrRik2AqHgmHz4JgIAHTFFJqWnlRN5F.jpg",
        "https://t4.ftcdn.net/jpg/04/69/33/37/240_F_469333729_ohdQnuuAehaGlhWQD1zh4i3MNQb9QMFz.jpg"
        ),
        listOf("Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги","Сериалы","Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги","Сериалы","Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги","Сериалы","Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги","Сериалы")
        )
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

        viewModel = FeedViewModel()

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
                    R.id.toPass,
                    R.id.toLike -> {
                        motionLayout.progress = 0f
                        motionLayout.transitionToStart()
                        viewPager.setCurrentItem(0, false)
                        viewModel.swipe()

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
                adapter.userPhoto = it.images

                insertDataIntoTextView(it)
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