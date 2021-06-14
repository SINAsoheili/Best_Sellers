package net.sinasoheili.best_sellers.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SetRoleViewModel
import net.sinasoheili.best_sellers.viewModel.UserMainPageViewModel
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class UserMainPage : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModel: UserMainPageViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserphone: TextView
    private lateinit var tvUserId: TextView
    private lateinit var btnShopSearch: Button
    private lateinit var btnCheckDiscount: Button
    private lateinit var btnScanQrCode: Button
    private lateinit var tvRemoveUser: TextView
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

        btnShopSearch = findViewById(R.id.btn_userMainPage_searchShop)
        btnShopSearch.setOnClickListener(this)

        btnCheckDiscount = findViewById(R.id.btn_userMainPage_checkDiscount)
        btnCheckDiscount.setOnClickListener(this)

        tvRemoveUser = findViewById(R.id.tv_userMainPage_deleteUser)
        tvRemoveUser.setOnClickListener(this)

        btnScanQrCode = findViewById(R.id.btn_userMainPage_scanQRcode)
        btnScanQrCode.setOnClickListener(this)
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

        viewModel.userDeleteData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    startActivity(Intent(this , ChooseRoleActivity::class.java))
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
                    showMessage(getString(R.string.connection_error))
                }
            }
        })

        viewModel.shopData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    supportFragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fl_userMainPage_continer, ShopDetailFragment(it.data))
                            .commit()
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
                    showMessage(getString(R.string.connection_error))
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

    private fun openQRcodeScanner() {
        try {
            val intent: Intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            startActivityForResult(intent, 0);
        } catch (e: Exception) {
            showMessage(getString(R.string.to_use_this_section_please_install_qr_code_scanner))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                val sShopId: String = data?.getStringExtra("SCAN_RESULT") ?: "-1"
                val shopId: Int = sShopId.toInt()
                if(shopId == -1) {
                    showMessage(getString(R.string.shop_not_found))
                } else {
                    viewModel.getShopInfo(shopId)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            btnShopSearch -> {
                startActivity(Intent(this, ShopSearchActivity::class.java))
            }

            btnCheckDiscount -> {
                startActivity(Intent(this , UserDiscountActivity::class.java))
            }

            btnScanQrCode -> {
                openQRcodeScanner()
            }

            tvRemoveUser -> {

                drawerLayout.closeDrawer(Gravity.RIGHT)

                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.warning))
                        .setMessage(getString(R.string.are_you_sure_want_to_delete_user))
                        .setPositiveButton(getString(R.string.yes) , object: DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                viewModel.deleteUser()
                            }
                        })
                        .setNegativeButton(R.string.no , object: DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }

                        })
                        .show()
            }
        }
    }
}