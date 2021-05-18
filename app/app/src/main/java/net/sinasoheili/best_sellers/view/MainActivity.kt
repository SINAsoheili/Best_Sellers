package net.sinasoheili.best_sellers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.util.ManageLogin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (ManageLogin.getWhoLogIn(this)) {

            Keys.USER -> {
                startActivity(Intent(this , UserMainPage::class.java))
                finish()
            }

            Keys.SELLER -> {
                if (ManageLogin.getShopId(this) == -1) { //shop not registered
                    startActivity(Intent(this , RegisterShopActivity::class.java))
                    finish()
                } else { // shop registered so show seller main page
                    startActivity(Intent(this , SellerMainPage::class.java))
                    finish()
                }
            }

            else -> {
                startActivity(Intent(this , ChooseRoleActivity::class.java))
                finish()
            }
        }

    }
}