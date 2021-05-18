package net.sinasoheili.best_sellers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
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
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_bnv_statistic -> {
                Toast.makeText(this , item.title , Toast.LENGTH_SHORT).show()
            }

            R.id.item_bnv_dashboard -> {
                Toast.makeText(this , item.title , Toast.LENGTH_SHORT).show()
            }

            R.id.item_bnv_store -> {
                Toast.makeText(this , item.title , Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}