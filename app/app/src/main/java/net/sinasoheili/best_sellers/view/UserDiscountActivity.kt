package net.sinasoheili.best_sellers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.model.ShopDiscount
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.DiscountListAdapter
import net.sinasoheili.best_sellers.viewModel.UserDiscountViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UserDiscountActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: UserDiscountViewModel

    private lateinit var listViewDiscount: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_discount)

        initObj()
        setObserver()
        viewModel.getUserDiscount()
    }

    private fun initObj() {
        listViewDiscount = findViewById(R.id.lv_userDiscount)
        progressBar = findViewById(R.id.pb_userDiscount)
        tvEmpty = findViewById(R.id.tv_userDiscount_empty)
    }

    private fun setObserver() {
        viewModel.discountListData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showDiscountList(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.ConnectionError -> {
                    invisibleProgressBar()
                    showMessage(getString(R.string.connection_error))
                }
            }
        })
    }

    private fun showMessage(text: String) {
        Snackbar.make(listViewDiscount , text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun invisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showDiscountList(list: ArrayList<ShopDiscount>) {
        if(list.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
            val adapter: DiscountListAdapter = DiscountListAdapter(this , list)
            listViewDiscount.adapter = adapter
        }
    }

}