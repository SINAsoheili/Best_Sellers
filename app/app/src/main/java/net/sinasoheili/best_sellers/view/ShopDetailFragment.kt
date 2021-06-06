package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop

class ShopDetailFragment constructor(val shop: Shop): Fragment(R.layout.fragment_shop_detail) {

    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvSite: TextView
    private lateinit var tvDescription: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObj(view)
        fillField()
    }

    private fun initObj(view: View) {
        tvName = view.findViewById(R.id.tv_shopDetail_name)
        tvAddress = view.findViewById(R.id.tv_shopDetail_address)
        tvPhone = view.findViewById(R.id.tv_shopDetail_phone)
        tvSite = view.findViewById(R.id.tv_shopDetail_site)
        tvDescription = view.findViewById(R.id.tv_shopDetail_description)
    }

    private fun fillField () {
        tvName.text = shop.name
        tvAddress.text = shop.address
        tvPhone.text = shop.phone
        tvSite.text = shop.site
        tvDescription.text = shop.description
    }
}