package net.sinasoheili.best_sellers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.sinasoheili.best_sellers.R

class SellerMainPage : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bnv: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_main_page)

        initObj()
    }

    private fun initObj() {
        bnv = findViewById(R.id.bnv_sellerMainPage)
        bnv.setOnNavigationItemSelectedListener(this)
        bnv.selectedItemId = R.id.item_bnv_statistic
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_sellerMainPage_container, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_bnv_statistic -> {
                showFragment(SellerStatisticFragment())
            }

            R.id.item_bnv_dashboard -> {
                showFragment(SellerDashboardFragment())
            }

            R.id.item_bnv_store -> {
                showFragment(SellerStoreFragment())
            }
        }
        return true
    }
}