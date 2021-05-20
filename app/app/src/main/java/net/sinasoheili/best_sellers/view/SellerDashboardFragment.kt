package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SellerDashboardFragmentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SellerDashboardFragment: Fragment(R.layout.fragment_dashboard_seller), View.OnClickListener {

    @Inject
    lateinit var viewModel: SellerDashboardFragmentViewModel

    private lateinit var btnCreateDiscount: Button
    private lateinit var btnCheckUserDiscount: Button
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
        setObserver()
    }

    private fun initObj(view: View) {
        btnCreateDiscount = view.findViewById(R.id.btn_SellerDashboard_registerDiscount)
        btnCreateDiscount.setOnClickListener(this)

        btnCheckUserDiscount = view.findViewById(R.id.btn_SellerDashboard_checkUserDiscount)
        btnCheckUserDiscount.setOnClickListener(this)

        progressBar = view.findViewById(R.id.pb_fragmentDashboardSeller)
    }

    private fun setObserver() {
        viewModel.registerDiscountData.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.discount_register_successfully))
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.connection_error))
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }
            }
        })


        viewModel.deleteDiscountData.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.discount_delete_successfully))
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.connection_error))
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }
            }
        })
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String) {
        Snackbar.make(progressBar , text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    override fun onClick(v: View?) {
        when (v) {
            btnCreateDiscount -> {
                CreateNewDiscountDialog(requireContext() , viewModel).show()
            }

            btnCheckUserDiscount -> {
                Toast.makeText(context, "check" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}