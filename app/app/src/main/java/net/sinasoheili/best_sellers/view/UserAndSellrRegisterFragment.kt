package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.viewModel.SetRoleViewModel

class UserAndSellrRegisterFragment constructor(val viewModel: SetRoleViewModel , val who: String) :
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
    }

    private fun checkValidateInputs() : Boolean = if (checkName() && checkLastName() && checkPhone() && checkPasswd() && checkPasswdRepeat()) true else false

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

    override fun onClick(v: View?) {
        if (v!!.equals(btnSubmit)) {
            if (checkValidateInputs()) {

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
        }
    }
}