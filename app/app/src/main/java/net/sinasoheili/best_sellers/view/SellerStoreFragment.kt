package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SellerStoreFragmentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SellerStoreFragment: Fragment(R.layout.fragment_store_seller) {

    @Inject
    lateinit var shopStoreFragmentViewModel: SellerStoreFragmentViewModel

    private lateinit var tvShopNmae: TextView
    private lateinit var tvSellerInfo: TextView
    private lateinit var tvShopAddress: TextView
    private lateinit var tvShopPhone: TextView
    private lateinit var tvShopSite: TextView
    private lateinit var tvShopDescription: TextView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
        setObserver()

        shopStoreFragmentViewModel.getShopInfo()
        shopStoreFragmentViewModel.getSellerInfo()
    }

    private fun initObj(view: View) {
        tvShopNmae = view.findViewById(R.id.tv_fragmentStoreSeller_shopName)
        tvShopAddress = view.findViewById(R.id.tv_fragmentStoreSeller_shopAddress)
        tvShopPhone = view.findViewById(R.id.tv_fragmentStoreSeller_shopPhone)
        tvShopSite = view.findViewById(R.id.tv_fragmentStoreSeller_shopSite)
        tvShopDescription = view.findViewById(R.id.tv_fragmentStoreSeller_shopDescription)
        progressBar = view.findViewById(R.id.pb_fragmentStoreSeller)
        tvSellerInfo = view.findViewById(R.id.tv_fragmentStoreSeller_sellerInfo)
    }

    private fun setObserver() {
        shopStoreFragmentViewModel.shopDataState.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    fillField(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.connection_error))

                    //todo: show try icon to fetch again and invisible in another states
                }
            }
        })

        shopStoreFragmentViewModel.sellerDataState.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showSellerInfo(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.connection_error))
                }
            }
        })
    }

    private fun showMessage(text: String) {
        Snackbar
                .make(tvShopNmae , text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun fillField(shop: Shop) {
        showShopName(shop.name)
        showShopAddress(shop.address)
        showShopSite(shop.site)
        showShopDescription(shop.description)
        showShopPhone(shop.phone)
    }

    private fun showShopName(name: String?) {
        if(name != null)
            tvShopNmae.text = name
    }

    private fun showShopAddress(address: String?) {
        if(address != null)
            tvShopAddress.text = address
    }

    private fun showShopSite(site: String?) {
        if(site != null)
            tvShopSite.text = site
    }

    private fun showShopDescription(description: String?) {
        if(description != null)
            tvShopDescription.text = description
    }

    private fun showShopPhone(phone: String?) {
        if(phone != null)
            tvShopPhone.text = phone
    }

    private fun showSellerInfo(seller: Seller) {
        tvSellerInfo.text = seller.toString()
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.GONE
    }
}