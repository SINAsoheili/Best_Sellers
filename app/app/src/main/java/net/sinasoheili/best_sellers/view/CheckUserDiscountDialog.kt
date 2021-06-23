package net.sinasoheili.best_sellers.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SellerDashboardFragmentViewModel

class CheckUserDiscountDialog constructor(val dialogContext: Context,
                                          val viewModel: SellerDashboardFragmentViewModel,
                                          val lifeCycle: LifecycleOwner)
    : Dialog(dialogContext), View.OnClickListener , TextWatcher{

    private lateinit var tilUserId: TextInputLayout
    private lateinit var etUserId: TextInputEditText
    private lateinit var tvResult: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnCheck: Button

    private var submitVisible: Boolean = true //use this flag to determine status of button. Button can be Consume OR submit.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_check_user_discount)

        initObj()
        setObserver()
    }

    override fun onStart() {
        super.onStart()
        this.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT)

        tvResult.text = ""
        consume2submitButton()
    }

    private fun initObj() {
        tilUserId = findViewById(R.id.til_checkUserDiscount_userId)
        etUserId = findViewById(R.id.et_checkUserDiscount_userId)
        etUserId.addTextChangedListener(this)

        btnCheck = findViewById(R.id.btn_checkUserDiscount_check)
        btnCheck.setOnClickListener(this)

        tvResult = findViewById(R.id.tv_checkUserDiscount_result)

        progressBar = findViewById(R.id.pb_checkUserDiscount)
    }

    private fun setObserver() {
        viewModel.checkUserHasDiscountData.observe(lifeCycle , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    enableCancelable()
                    if(it.data) {
                        tvResult.setTextColor(dialogContext.getColor(R.color.success))
                        tvResult.text = context.getString(R.string.user_has_discount)
                        submitButton2Consume()
                    } else {
                        tvResult.setTextColor(dialogContext.getColor(R.color.fail))
                        tvResult.text = context.getString(R.string.user_does_not_have_discount)
                    }
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                    enableCancelable()
                }

                is DataState.ConnectionError -> {
                    invisibleProgressBar()
                    enableCancelable()
                    showToast(context.getString(R.string.connection_error))
                }
            }
        })

        viewModel.deleteUserDiscountData.observe(lifeCycle , Observer {
            when (it) {
                is DataState.Success -> {
                    enableCancelable()
                    invisibleProgressBar()
                    showToast(context.getString(R.string.change_confirm_successfully))
                    closeDialog();
                }

                is DataState.Loading -> {
                    disableCancelable()
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    enableCancelable()
                    invisibleProgressBar()
                    showToast(it.text)
                }

                is DataState.ConnectionError -> {
                    enableCancelable()
                    invisibleProgressBar()
                    showToast(context.getString(R.string.connection_error))
                }
            }
        })
    }

    private fun isEmpty(et: TextInputEditText) : Boolean =  et.text!!.isEmpty()

    private fun checkUserId() : Boolean {

        if (isEmpty(etUserId)) {
            tilUserId.error = context.getString(R.string.please_fill_field)
            return false
        } else {
            tilUserId.error = null
            return true
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context ,text , Toast.LENGTH_SHORT).show()
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun invisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun enableCancelable() {
        this.setCancelable(true)
    }

    private fun disableCancelable() {
        this.setCancelable(false)
    }

    private fun closeDialog() {
        this.dismiss()
    }

    private fun submitButton2Consume() {
        submitVisible = false
        btnCheck.text = dialogContext.getString(R.string.consume)
    }

    private fun consume2submitButton() {
        submitVisible = true
        btnCheck.text = dialogContext.getString(R.string.submit)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnCheck -> {
                if(submitVisible) {
                    if(checkUserId()) {
                        viewModel.checkUserHasDiscount(etUserId.text.toString().trim().toInt())
                        disableCancelable()
                    }
                } else {
                    if(checkUserId()) {
                        viewModel.deleteDiscountOfUser(etUserId.text.toString().trim().toInt())
                        disableCancelable()
                    }
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // do nothing
    }

    override fun afterTextChanged(s: Editable?) {
        if(s!!.isEmpty()) {
            consume2submitButton()
            tvResult.text = ""
        }
    }
}