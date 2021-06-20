package net.sinasoheili.best_sellers.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SellerStoreFragmentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SellerStoreFragment: Fragment(R.layout.fragment_store_seller), View.OnClickListener,
    OnMapReadyCallback {

    @Inject
    lateinit var shopStoreFragmentViewModel: SellerStoreFragmentViewModel

    private lateinit var tvShopNmae: TextView
    private lateinit var tvSellerInfo: TextView
    private lateinit var tvShopAddress: TextView
    private lateinit var tvShopPhone: TextView
    private lateinit var tvShopSite: TextView
    private lateinit var tvShopDescription: TextView
    private lateinit var btnDeleteShop: Button
    private lateinit var btnDeleteSeller: Button
    private lateinit var progressBar: ProgressBar

    private var shop: Shop? = null
    private var map: GoogleMap? = null

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
        tvSellerInfo = view.findViewById(R.id.tv_fragmentStoreSeller_sellerInfo)

        progressBar = view.findViewById(R.id.pb_fragmentStoreSeller)

        btnDeleteShop = view.findViewById(R.id.btn_fragmentStoreSeller_deleteShop)
        btnDeleteShop.setOnClickListener(this)

        btnDeleteSeller = view.findViewById(R.id.btn_fragmentStoreSeller_deleteSeller)
        btnDeleteSeller.setOnClickListener(this)

        showMap()
    }

    private fun setObserver() {
        shopStoreFragmentViewModel.shopDataState.observe(viewLifecycleOwner , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    this.shop = it.data
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

        shopStoreFragmentViewModel.deleteShopDataState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    startActivity(Intent(requireContext() , ChooseRoleActivity::class.java))
                    requireActivity().finish()
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error-> {
                    inVisibleProgressBar()
                    showMessage(it.text)
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(requireContext().getString(R.string.connection_error))
                }
            }
        })

        shopStoreFragmentViewModel.deleteSellerDataState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    startActivity(Intent(requireContext() , ChooseRoleActivity::class.java))
                    requireActivity().finish()
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error-> {
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

    private fun showMap() {
        val mapFragment: SupportMapFragment = SupportMapFragment.newInstance()
        parentFragmentManager
            .beginTransaction()
            .add(R.id.fl_fragmentStoreSeller_mapContiner, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
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

        if(map != null) {
            showMarker(map!! , LatLng(shop.latitude.toDouble() , shop.longitude.toDouble()))
        }
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

    private fun showMarker(map: GoogleMap , loc: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(loc))
        map.animateCamera(CameraUpdateFactory.newLatLng(loc))
    }

    override fun onClick(v: View?) {
        when(v) {
            btnDeleteShop -> {
                AlertDialog.Builder(requireContext())
                        .setTitle(requireContext().getString(R.string.warning))
                        .setMessage(requireContext().getString(R.string.are_you_sure_want_to_delete_shop))
                        .setNegativeButton(requireContext().getString(R.string.no) , object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        })
                        .setPositiveButton(requireContext().getString(R.string.yes) , object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                shopStoreFragmentViewModel.deleteShop()
                            }

                        })
                        .show()
            }

            btnDeleteSeller -> {
                AlertDialog.Builder(requireContext())
                        .setTitle(requireContext().getString(R.string.warning))
                        .setMessage(requireContext().getString(R.string.are_you_sure_want_to_delete_seller))
                        .setNegativeButton(requireContext().getString(R.string.no) , object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        })
                        .setPositiveButton(requireContext().getString(R.string.yes) , object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                shopStoreFragmentViewModel.deleteSeller()
                            }

                        })
                        .show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if(shop != null) {
            showMarker(map , LatLng(shop!!.latitude.toDouble() , shop!!.longitude.toDouble()))
        }
    }
}