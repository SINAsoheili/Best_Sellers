package net.sinasoheili.best_sellers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.viewModel.SetRoleViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ChooseRoleActivity : AppCompatActivity(), View.OnClickListener {

    @Inject lateinit var viewModel: SetRoleViewModel

    private lateinit var btnUser: Button
    private lateinit var btnSeller: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_choose)

        initObj()
        setObserver()
    }

    private fun initObj() {

        btnUser = findViewById(R.id.btn_setRole_user)
        btnUser.setOnClickListener(this)

        btnSeller = findViewById(R.id.btn_setRole_seller)
        btnSeller.setOnClickListener(this)

        progressBar = findViewById(R.id.pb_setRole)
    }

    private fun setObserver() {
        viewModel.sellerDataState.observe(this, Observer { dataStat ->
            when(dataStat) {
                is DataState.Success<Seller> -> {
                    inVisibleProgressBar()
                    startActivity(Intent(this , RegisterShopActivity::class.java))
                    finish()
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(dataStat.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(this.getString(R.string.connection_error))
                }
            }
        })

        viewModel.userDataState.observe(this, Observer { dataState ->

            when (dataState) {
                is DataState.Success<User> -> {
                    inVisibleProgressBar()
                    startActivity(Intent(this , UserMainPage::class.java))
                    finish()
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(dataState.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(this.getString(R.string.connection_error))
                }
            }

        })

        viewModel.shopDataState.observe(this , Observer { dataState ->
            when(dataState) {
                is DataState.Success<Shop> -> {
                    inVisibleProgressBar()
                    startActivity(Intent(this , SellerMainPage::class.java))
                    finish()
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    startActivity(Intent(this , RegisterShopActivity::class.java))
                    finish()
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(this.getString(R.string.connection_error))
                }
            }
        })
    }

    private fun showRegisterFragment(who: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_setRole_container , UserAndSellerRegisterFragment(viewModel , who) )
            .addToBackStack(null)
            .commit()
    }

    override fun onClick(v: View?) {
        when(v) {
            btnUser -> {
                showRegisterFragment(Keys.USER)
            }

            btnSeller -> {
                showRegisterFragment(Keys.SELLER)
            }
        }
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String) {
        Snackbar
            .make(btnSeller , text, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }
}
