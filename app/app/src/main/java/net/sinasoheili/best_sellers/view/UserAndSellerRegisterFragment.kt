package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.viewModel.SetRoleViewModel

class UserAndSellerRegisterFragment constructor(val viewModel: SetRoleViewModel, val who: String) :
    Fragment(R.layout.fragment_register_user_and_seller), View.OnClickListener {

    private lateinit var etName: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var etLastName: TextInputEditText
    private lateinit var tilLastName: TextInputLayout
    private lateinit var etPhone: TextInputEditText
    private lateinit var tilPhone: TextInputLayout
    private lateinit var etPasswd: TextInputEditText
    private lateinit var tilPasswd: TextInputLayout
    private lateinit var etPasswdRepeat: TextInputEditText
    private lateinit var tilPasswdRepeat: TextInputLayout
    private lateinit var btnSubmit: Button
    private lateinit var tvLoginSignUp: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView

    private var signUpPageVisible: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
    }

    private fun initObj(view: View) {
        tilName = view.findViewById(R.id.til_registerUser_name)
        etName = view.findViewById(R.id.et_registerUser_name)

        tilLastName = view.findViewById(R.id.til_registerUser_lastName)
        etLastName = view.findViewById(R.id.et_registerUser_lastName)

        tilPhone = view.findViewById(R.id.til_registerUser_phone)
        etPhone = view.findViewById(R.id.et_registerUser_phone)

        tilPasswd = view.findViewById(R.id.til_registerUser_passwd)
        etPasswd = view.findViewById(R.id.et_registerUser_passwd)

        tilPasswdRepeat = view.findViewById(R.id.til_registerUser_passwdRepeat)
        etPasswdRepeat = view.findViewById(R.id.et_registerUser_passwdRepeat)

        btnSubmit = view.findViewById(R.id.btn_registerUser_submit)
        btnSubmit.setOnClickListener(this)

        tvLoginSignUp = view.findViewById(R.id.tv_registerUser_login_signUp)
        tvLoginSignUp.setOnClickListener(this)

        tvTitle = view.findViewById(R.id.tv_registerUser_title)
        tvDescription = view.findViewById(R.id.tv_registerUser_login_description)
    }

    private fun checkSignUpValidInputs() : Boolean = if (checkName() && checkLastName() && checkPhone() && checkPasswd() && checkPasswdRepeat()) true else false

    private fun checkSignInValidInputs() : Boolean = if (checkPhone() && checkPasswd()) true else false

    private fun isEmpty(et: TextInputEditText) : Boolean =  et.text!!.isEmpty()

    private fun checkLength(et: TextInputEditText , length: Int) : Boolean = et.text!!.toString().length == length

    private fun haveSameContent(et1: TextInputEditText, et2: TextInputEditText) : Boolean = et1.text.toString().equals( et2.text.toString() )

    private fun checkName() : Boolean {

        if (isEmpty(etName)) {
            tilName.error = tilName.context.getString(R.string.please_fill_field)
            return false
        } else {
            tilName.error = null
            return true
        }
    }

    private fun checkLastName() : Boolean {
        if (isEmpty(etLastName)) {
            tilLastName.error = etLastName.context.getString(R.string.please_fill_field)
            return false
        } else {
            tilLastName.error = null
            return true
        }
    }

    private fun checkPhone() : Boolean {
        if (isEmpty(etPhone))
        {
            tilPhone.error = tilPhone.context.getString(R.string.please_fill_field)
            return false
        } else {
            if (checkLength(etPhone , 11))
            {
                tilPhone.error = null
                return true
            } else {
                tilPhone.error = tilPhone.context.getString(R.string.please_insert_correct_information)
                return false
            }
        }
    }

    private fun checkPasswd() : Boolean {
        if (isEmpty(etPasswd)) {
            tilPasswd.error = tilPasswd.context.getString(R.string.please_fill_field)
            return false
        }
        else {
            tilPasswd.error = null
            return true
        }
    }

    private fun checkPasswdRepeat() : Boolean {
        if (isEmpty(etPasswdRepeat)) {
            tilPasswdRepeat.error = tilPasswd.context.getString(R.string.please_fill_field)
            return false
        }
        else {
            if (haveSameContent(etPasswd, etPasswdRepeat)) {
                tilPasswdRepeat.error = null
                return true
            } else {
                tilPasswdRepeat.error = tilPasswdRepeat.context.getString(R.string.please_insert_correct_information)
                return false
            }
        }
    }

    private fun closeFragment() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun invisibleNameField() {
        tilName.visibility = View.GONE
        etName.visibility = View.GONE
    }

    private fun visibleNameField() {
        tilName.visibility = View.VISIBLE
        etName.visibility = View.VISIBLE
    }

    private fun invisibleLastNameField() {
        tilLastName.visibility = View.GONE
        etLastName.visibility = View.GONE
    }

    private fun visibleLastNameField() {
        tilLastName.visibility = View.VISIBLE
        etLastName.visibility = View.VISIBLE
    }

    private fun invisiblePasswordRepeatField() {
        tilPasswdRepeat.visibility = View.GONE
        etPasswdRepeat.visibility = View.GONE
    }

    private fun visiblePasswordRepeatField() {
        tilPasswdRepeat.visibility = View.VISIBLE
        etPasswdRepeat.visibility = View.VISIBLE
    }

    private fun showLoginPage() {
        //invisible signUpPage
        tvTitle.text = getString(R.string.login)
        btnSubmit.text = getString(R.string.login)

        signUpPageVisible = false

        invisibleNameField()
        invisibleLastNameField()
        invisiblePasswordRepeatField()

        tvDescription.text = getString(R.string.if_you_do_not_sign_up_sign_up_please)
        tvLoginSignUp.text = view?.context?.getString(R.string.signUp)
    }

    private fun showSignUpPage() {
        //visible SignUpPage
        tvTitle.text = getString(R.string.signUp)
        btnSubmit.text = getString(R.string.signUp)

        signUpPageVisible = true

        visibleNameField()
        visibleLastNameField()
        visiblePasswordRepeatField()

        tvDescription.text = getString(R.string.if_you_sign_up_before_login)
        tvLoginSignUp.text = view?.context?.getString(R.string.login)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSubmit -> {

                if (signUpPageVisible) {
                    if (checkSignUpValidInputs()) {

                        val passwd: String = etPasswd.text.toString().trim()

                        if (who.equals(Keys.USER)) {

                            val user: User = User(
                                    name = etName.text.toString().trim(),
                                    lastName = etLastName.text.toString().trim(),
                                    phone = etPhone.text.toString().trim()
                            )
                            viewModel.registerUser(user, passwd)

                        } else {

                            val seller: Seller = Seller(
                                    name = etName.text.toString().trim(),
                                    lastName = etLastName.text.toString().trim(),
                                    phone = etPhone.text.toString().trim()
                            )
                            viewModel.registerSeller(seller,passwd)
                        }

                        closeFragment()
                    }
                } else {
                    if (checkSignInValidInputs()) {

                        val passwd: String = etPasswd.text.toString().trim()
                        val phone: String = etPhone.text.toString().trim()

                        if (who.equals(Keys.USER)) {
                            viewModel.logInUser(phone , passwd)
                        } else {
                            viewModel.logInSeller(phone , passwd)
                        }

                        closeFragment()
                    }
                }
            }

            tvLoginSignUp -> {
                if (signUpPageVisible) {
                    showLoginPage()
                } else {
                    showSignUpPage()
                }
            }
        }
    }
}