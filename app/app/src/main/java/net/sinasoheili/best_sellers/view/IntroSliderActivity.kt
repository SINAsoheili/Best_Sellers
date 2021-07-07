package net.sinasoheili.best_sellers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.util.CacheToPreference

class IntroSliderActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var lottieView: LottieAnimationView;
    private lateinit var tvDescription: TextView
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var llDots: LinearLayout


    private val handler: Handler = Handler()
    private lateinit var fade: Animation
    private lateinit var visibleFade: Animation
    private lateinit var dots: Array<ImageView>
    private var currentPage: Int = 0
    private val lottieViews: Array<Int> = arrayOf(R.raw.location , R.raw.online_survey , R.raw.statistic)
    private val descriptions: Array<Int> = arrayOf(R.string.introSlider_location_description ,
        R.string.introSlider_survey_description ,
        R.string.introSlider_statistic_description)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)

        window.statusBarColor = getColor(R.color.intro_slider_background)

        initObj()
        preparePage()
    }

    private fun initObj() {
        lottieView = findViewById(R.id.introSlider_lottie_view)
        tvDescription = findViewById(R.id.tv_introSlider_description)
        llDots = findViewById(R.id.ll_introSlider_dots)

        btnNext = findViewById(R.id.btn_introSlider_next)
        btnNext.setOnClickListener(this)

        btnPrevious = findViewById(R.id.btn_introSlider_previous)
        btnPrevious.setOnClickListener(this)

        fade = AnimationUtils.loadAnimation(this , R.anim.fade)
        visibleFade = AnimationUtils.loadAnimation(this , R.anim.visible_fade)
    }

    private fun updateDots() {
        llDots.removeAllViews()

        for(i in 0 until lottieViews.size) {
            val dotImage: ImageView = ImageView(this)
            dotImage.setPadding(5 , 5, 5 ,5)
            if(i == currentPage){
                dotImage.setImageResource(R.drawable.active_dot)
            } else {
                dotImage.setImageResource(R.drawable.passive_dot)
            }
            llDots.addView(dotImage)
        }
    }

    private fun visibleStartButton() {
        btnNext.text = getString(R.string.start_app)
    }

    private fun visibleNextButton() {
        btnNext.text = getString(R.string.next)
    }

    private fun visiblePrevButton() {
        btnPrevious.visibility = View.VISIBLE
    }

    private fun invisiblePrevButton() {
        btnPrevious.visibility = View.GONE
    }

    private fun preparePage() {
        lottieView.startAnimation(fade)
        tvDescription.startAnimation(fade)

        updateDots()

        handler.postDelayed(object:Runnable{
            override fun run() {
                lottieView.setAnimation(lottieViews.get(currentPage))
                lottieView.playAnimation()

                tvDescription.text = getString(descriptions.get(currentPage))

                lottieView.startAnimation(visibleFade)
                tvDescription.startAnimation(visibleFade)
            }
        } , resources.getInteger(R.integer.animation_duration).toLong())
    }

    private fun startApplication() {
        CacheToPreference.SetIntroSliderVisited(this)
        startActivity(Intent(this , ChooseRoleActivity::class.java))
        finish()
    }

    override fun onClick(v: View?) {
        when(v) {
            btnNext -> {
                currentPage++

                if(currentPage == lottieViews.size) { // in last page and click on start application
                    startApplication()
                } else if(currentPage == lottieViews.size-1) { // in last page and does not click on start application
                    visibleStartButton()
                    preparePage()
                } else {
                    visibleNextButton()
                    visiblePrevButton()
                    preparePage()
                }
            }

            btnPrevious -> {
                currentPage--

                if(currentPage == 0){
                    invisiblePrevButton()
                } else {
                    visibleNextButton()
                }

                preparePage()
            }
        }
    }
}