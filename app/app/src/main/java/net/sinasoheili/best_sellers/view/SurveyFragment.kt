package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.AnsweredQuestion
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.model.Question
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SurveyViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SurveyFragment constructor(val shop: Shop): Fragment(R.layout.fragment_survey), View.OnClickListener {

    @Inject
    lateinit var viewModel: SurveyViewModel

    private lateinit var etMessage: TextInputEditText
    private lateinit var tilMessage: TextInputLayout
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var questionContainer: List<CardView>

    private lateinit var questionList: List<Question>
    private var messageRegistered: Boolean = false
    private var questionSubmitted: Boolean = false
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()
        viewModel.checkUserAnsQuestion(shop.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
    }

    private fun initObj(view: View) {

        questionContainer = listOf (
            view.findViewById(R.id.cv_survey_questions_1),
            view.findViewById(R.id.cv_survey_questions_2),
            view.findViewById(R.id.cv_survey_questions_3),
            view.findViewById(R.id.cv_survey_questions_4),
            view.findViewById(R.id.cv_survey_questions_5)
        )

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

        viewModel.userAnsQuestionData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    if(it.data) { // user answered question before
                        isUpdate = true
                        prepareButton()
                        viewModel.getUserAnsweredQuestion(shop.id)
                        viewModel.getUserMessage(shop.id)

                    } else { // user dose not answered question before
                        isUpdate = false
                        prepareButton()
                        viewModel.getQuestion(shop.idCategory)
                    }
                    inVisibleProgressBar()
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

        viewModel.userShopMessageData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showUserMessage(it.data)
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

        viewModel.answeredQuestionData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    initialQuestionList(it.data)
                    showAnsweredQuestion(it.data)
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

        viewModel.removeSurveyData.observe(this, Observer {
            when(it) {
                is DataState.Success -> {
                    registerSurvey()
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
        var i: Int = 0
        for(q in questions) {
            val cv: CardView = questionContainer.get(i)
            cv.removeAllViews()

            val ll: LinearLayout = LinearLayout(requireContext())
            ll.orientation = LinearLayout.VERTICAL

            val tv: TextView = TextView(requireContext())
            tv.id = q.id
            tv.text = q.text
            ll.addView(tv)

            val sb: SeekBar = SeekBar(context)
            sb.tag = q.id
            sb.max = 5
            sb.min = -5
            sb.incrementProgressBy(1)
            sb.progress = 0
            ll.addView(sb)

            cv.addView(ll)
            i++
        }
    }

    private fun showAnsweredQuestion(questions: List<AnsweredQuestion>) {
        var i: Int = 0
        for(q in questions) {
            val cv: CardView = questionContainer.get(i)
            cv.removeAllViews()

            val ll: LinearLayout = LinearLayout(requireContext())
            ll.orientation = LinearLayout.VERTICAL
            val padding: Int = requireContext().resources.getDimension(R.dimen.padding_4).toInt()
            ll.setPadding(padding, padding, padding, padding)

            val tv: TextView = TextView(requireContext())
            tv.id = q.questionId
            tv.text = q.content
            ll.addView(tv)

            val sb: SeekBar = SeekBar(context)
            sb.tag = q.questionId
            sb.max = 5
            sb.min = -5
            sb.incrementProgressBy(1)
            sb.progress = q.score
            ll.addView(sb)

            cv.addView(ll)
            i++
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

    private fun showUserMessage(msg: Message) {
        etMessage.setText(msg.text)
    }

    private fun prepareButton()  {
        if(isUpdate) {
            btnSubmit.text = getString(R.string.update)
        } else {
            btnSubmit.text = getString(R.string.register)
        }
    }

    private fun registerSurvey() {
        val message: String = etMessage.text.toString().trim()
        val ans: Map<String , Int> = calcQuestion()

        viewModel.registerMessage( shop.id , message)
        viewModel.submitQuestion( shop.id , ans)
    }

    private fun initialQuestionList(questions: List<AnsweredQuestion>) {
        val ql: MutableList<Question> = mutableListOf()
        for(q in questions) {
            ql.add(Question(q.questionId , q.content))
        }
        questionList = ql
    }

    override fun onClick(v: View?) {
        when(v) {
            btnSubmit -> {
                if(! messageIsEmpty()) {
                    if(isUpdate) {
                        viewModel.removeSurvey(shop.id)
                    }
                    else {
                        registerSurvey()
                    }
                }
            }
        }
    }

}