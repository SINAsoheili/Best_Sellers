package net.sinasoheili.best_sellers.view

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.viewModel.SellerDashboardFragmentViewModel

class CreateNewDiscountDialog constructor(val dialogContext: Context,
                                          val viewModel: SellerDashboardFragmentViewModel)
    : Dialog(dialogContext), View.OnClickListener {
    private lateinit var tilName: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var tilAmount: TextInputLayout
    private lateinit var etAmount: TextInputEditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_register_new_discount)

        initObj()
    }

    override fun onStart() {
        super.onStart()
        this.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun initObj() {
        tilName = findViewById(R.id.til_registerDiscount_name)
        etName = findViewById(R.id.et_registerDiscount_name)

        tilAmount = findViewById(R.id.til_registerDiscount_amount)
        etAmount = findViewById(R.id.et_registerDiscount_amount)

        btnSubmit = findViewById(R.id.btn_registerDiscount_submit)
        btnSubmit.setOnClickListener(this)
    }

    private fun isEmpty(et: TextInputEditText) : Boolean =  et.text!!.isEmpty()

    private fun checkName() : Boolean {

        if (isEmpty(etName)) {
            tilName.error = tilName.context.getString(R.string.please_fill_field)
            return false
        } else {
            if(etName.text.toString().contains(" "))
            {
                tilName.isHelperTextEnabled = true
                tilName.error = tilName.context.getString(R.string.name_must_be_without_space)
                return false
            } else {
                tilName.error = null
                tilName.isHelperTextEnabled = false
                return true
            }
        }
    }

    private fun checkAmount() : Boolean {
        if (isEmpty(etAmount))
        {
            tilAmount.error = context.getString(R.string.please_fill_field)
            return false
        } else {
            val amount: Int = etAmount.text.toString().toInt()
            if (amount<100 && amount>0)
            {
                tilAmount.error = null
                return true
            } else {
                tilAmount.error = context.getString(R.string.discount_amount_is_not_valid)
                return false
            }
        }
    }

    private fun closeDialog() {
        this.dismiss()
    }

    override fun onClick(v: View?) {
        when(v) {
            btnSubmit -> {
                if(checkName() && checkAmount()) {
                    val discount: Discount = Discount(title=etName.text.toString().trim(),
                                                    amount=etAmount.text.toString().trim().toInt())
                    viewModel.registerDiscount(discount)
                    closeDialog()
                }
            }
        }
    }
}