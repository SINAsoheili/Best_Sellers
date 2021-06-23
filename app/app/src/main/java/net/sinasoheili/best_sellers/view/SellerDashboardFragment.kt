package net.sinasoheili.best_sellers.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SellerDashboardFragmentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SellerDashboardFragment: Fragment(R.layout.fragment_dashboard_seller), View.OnClickListener {

    @Inject
    lateinit var viewModel: SellerDashboardFragmentViewModel

    private lateinit var tvCurrentDiscountAmount: TextView
    private lateinit var tvCurrentDiscountName: TextView
    private lateinit var btnCreateDiscount: Button
    private lateinit var btnDeleteDiscount: Button
    private lateinit var cvCheckUserDiscount: CardView
    private lateinit var cvQRcode: CardView
    private lateinit var progressBar: ProgressBar

    private var currentDiscount: Discount? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
        setObserver()
        viewModel.getShopDiscount()
    }

    private fun initObj(view: View) {
        tvCurrentDiscountAmount = view.findViewById(R.id.tv_SellerDashboard_currentDiscountAmount)
        tvCurrentDiscountName = view.findViewById(R.id.tv_SellerDashboard_currentDiscountName)


        btnCreateDiscount = view.findViewById(R.id.btn_SellerDashboard_registerDiscount)
        btnCreateDiscount.setOnClickListener(this)

        btnDeleteDiscount = view.findViewById(R.id.btn_SellerDashboard_deleteDiscount)
        btnDeleteDiscount.setOnClickListener(this)

        cvCheckUserDiscount = view.findViewById(R.id.cv_SellerDashboard_checkUserHasDiscount)
        cvCheckUserDiscount.setOnClickListener(this)

        cvQRcode = view.findViewById(R.id.cv_SellerDashboard_QRgenerate)
        cvQRcode.setOnClickListener(this)

        progressBar = view.findViewById(R.id.pb_fragmentDashboardSeller)
    }

    private fun setObserver() {
        viewModel.registerDiscountData.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.discount_register_successfully))
                    viewModel.getShopDiscount()
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
                    viewModel.getShopDiscount()
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

        viewModel.getShopDiscountData.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    currentDiscount = it.data
                    showCurrentDiscount(currentDiscount)
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    currentDiscount = null
                    showCurrentDiscount(currentDiscount)
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

    private fun showCurrentDiscount(discount: Discount?) {
        if (discount == null) {
            tvCurrentDiscountName.text = requireContext().getString(R.string.there_is_not_any_Discount_for_this_shop)
            tvCurrentDiscountAmount.text = getString(R.string.discount_percent , 0.toString())
        } else {
            tvCurrentDiscountAmount.text = getString(R.string.discount_percent , discount.amount.toString())
            tvCurrentDiscountName.text = discount.title
        }
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
                if(currentDiscount == null) {
                    CreateNewDiscountDialog(requireContext() , viewModel).show()
                } else {
                    showMessage(getString(R.string.you_can_not_register_new_discount_please_remove_previous_discount_code))
                }
            }

            btnDeleteDiscount -> {
                if(currentDiscount == null) {
                    showMessage(getString(R.string.there_is_not_any_discount_please_create_new_discount))
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle(requireContext().getString(R.string.warning))
                        .setMessage(requireContext().getString(R.string.are_you_sure_to_delete_discount))
                        .setNegativeButton(requireContext().getString(R.string.no) , object:DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        })
                        .setPositiveButton(requireContext().getString(R.string.yes) , object:DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                viewModel.deleteDiscount()
                            }
                        })
                        .show()
                }
            }

            cvCheckUserDiscount -> {
                CheckUserDiscountDialog(requireContext() , viewModel , viewLifecycleOwner).show()
            }

            cvQRcode -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragmentDashboardSeller_container,ShowQRcodeFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}