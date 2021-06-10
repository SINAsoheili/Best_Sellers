package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Question
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SurveyViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SurveyFragment constructor(val shop: Shop): Fragment(R.layout.fragment_survey), View.OnClickListener {

    @Inject
    lateinit var viewModel: SurveyViewModel

    private lateinit var questionContainer: LinearLayout
    private lateinit var etMessage: TextInputEditText
    private lateinit var tilMessage: TextInputLayout
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var questionList: List<Question>
    private var messageRegistered: Boolean = false;
    private var questionSubmitted: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()

        viewModel.getQuestion(shop.idCategory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
    }

    private fun initObj(view: View) {
        questionContainer = view.findViewById(R.id.ll_survey_questions)

        etMessage = view.findViewById(R.id.tie_survey_message)
        tilMessage = view.findViewById(R.id.til_survey_message)

        btnSubmit = view.findViewById(R.id.btn_survey_submit)
        btnSubmit.setOnClickListener(this)

        progressBar = view.findViewById(R.id.pb_survey)
    }

    private fun setObserver() {
        viewModel.questionData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    questionList = it.data
                    showQuestion(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(getString(R.string.connection_error))
                }
            }
        })

        viewModel.messageRegisterData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    messageRegistered = true
                    if(questionSubmitted) {
                        inVisibleProgressBar()
                        showMessage(getString(R.string.your_opinion_register_successfully))
                        closeFragment()
                    }
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(getString(R.string.connection_error))
                }
            }
        })

        viewModel.submitQuestionData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    questionSubmitted = true
                    if(messageRegistered) {
                        inVisibleProgressBar()
                        showMessage(getString(R.string.your_opinion_register_successfully))
                        closeFragment()
                    }
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(getString(R.string.connection_error))
                }
            }
        })
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String) {
        Snackbar.make(btnSubmit , text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun showQuestion(questions: List<Question>) {
        questionContainer.removeAllViews()
        for(q in questions) {
            val tv: TextView = TextView(requireContext())
            tv.id = q.id
            tv.text = q.text
            questionContainer.addView(tv)

            val sb: SeekBar = SeekBar(context)
            sb.tag = q.id
            sb.max = 5
            sb.min = -5
            sb.incrementProgressBy(1)
            sb.progress = 0
            questionContainer.addView(sb)
        }
    }

    private fun messageIsEmpty(): Boolean {
        if(etMessage.text.toString().trim().isEmpty()) {
            tilMessage.error = getString(R.string.please_fill_field)
            etMessage.requestFocus()
            return true
        } else {
            tilMessage.error = null
            return false
        }
    }

    private fun calcQuestion(): Map<String , Int> {
        val result: MutableMap<String, Int> = mutableMapOf()
        for(q in questionList) {
            val tv: TextView = requireView().findViewById(q.id)
            val pb: ProgressBar = requireView().findViewWithTag(q.id)
            result.set(q.id.toString(), pb.progress)
        }
        return result
    }

    private fun closeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onClick(v: View?) {
        when(v) {
            btnSubmit -> {
                if(! messageIsEmpty()) {
                    val message: String = etMessage.text.toString().trim()
                    val ans: Map<String , Int> = calcQuestion()
                    viewModel.registerMessage( shop.id , message)
                    viewModel.submitQuestion( shop.id , ans)
                }
            }
        }
    }

}