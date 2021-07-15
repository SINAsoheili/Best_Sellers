package net.sinasoheili.best_sellers.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.children
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
    private lateinit var llQuestions: LinearLayout

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

        llQuestions = view.findViewById(R.id.ll_survey_question)

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

        viewModel.registerUserDiscountData.observe(this , Observer {
          when(it) {
              is DataState.Success -> { //mean shop has discount and register new discount for user
                  inVisibleProgressBar()
                  showMessage(getString(R.string.new_discount_set_for_you))
              }

              is DataState.Loading -> {
                  visibleProgressBar()
              }

              is DataState.Error -> { //mean shop dose not have any discount so user cannot has any discount
                  inVisibleProgressBar()
                  //do nothing
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

        val inflater: LayoutInflater = requireContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        llQuestions.removeAllViews()

        for(q in questions) {
            val questionCard: View = inflater.inflate(R.layout.question_card , null , false)

            val tvMessage: TextView = questionCard.findViewById(R.id.tv_questions_text)
            val sbScore: SeekBar = questionCard.findViewById(R.id.sb_questions_score)
            sbScore.incrementProgressBy(1)

            tvMessage.text = q.text
            tvMessage.tag = q.id.toString()
            sbScore.progress = 0

            llQuestions.addView(questionCard)
        }
    }

    private fun showAnsweredQuestion(questions: List<AnsweredQuestion>) {
        val inflater: LayoutInflater = requireContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        llQuestions.removeAllViews()

        for(q in questions) {
            val questionCard: View = inflater.inflate(R.layout.question_card , null , false)

            val tvMessage: TextView = questionCard.findViewById(R.id.tv_questions_text)
            val sbScore: SeekBar = questionCard.findViewById(R.id.sb_questions_score)
            sbScore.incrementProgressBy(1)

            tvMessage.text = q.content
            tvMessage.tag = q.questionId.toString()
            sbScore.progress = q.score

            llQuestions.addView(questionCard)
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

        for(item in llQuestions.children) {
            val tv: TextView = item.findViewById(R.id.tv_questions_text)
            val sb: SeekBar = item.findViewById(R.id.sb_questions_score)

            result.set( tv.tag.toString() , sb.progress)
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
                        viewModel.registerNewUserDiscount(shop.id) // shopId and discountId are same in data base architecture
                    }
                }
            }
        }
    }

}