package net.sinasoheili.best_sellers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.UserMainPageViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UserMainPage : AppCompatActivity() {

    @Inject
    lateinit var viewModel: UserMainPageViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserphone: TextView
    private lateinit var tvUserId: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main_page)

        initObj()
        setObserver()

        viewModel.getUserInfo()
    }

    private fun initObj() {
        drawerLayout = findViewById(R.id.dl_userMainPage)
        navigationView = findViewById(R.id.nv_userMainPage)
        progressBar = findViewById(R.id.pb_userMainPage)

        tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tv_userNavigationViewHeader_name)
        tvUserphone = navigationView.getHeaderView(0).findViewById(R.id.tv_userNavigationViewHeader_phone)
        tvUserId = navigationView.getHeaderView(0).findViewById(R.id.tv_userNavigationViewHeader_userId)
    }

    private fun setObserver() {
        viewModel.userInfoData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showUserInfo(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    invisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    invisibleProgressBar()
                    showMessage(this.getString(R.string.connection_error))
                }
            }
        })
    }

    private fun showMessage(text: String) {
        Snackbar.make(drawerLayout , text, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    private fun showUserInfo(user: User) {
        tvUserName.text = user.name + user.lastName
        tvUserphone.text = user.phone
        tvUserId.text = user.id.toString()
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun invisibleProgressBar() {
        progressBar.visibility = View.GONE
    }
}