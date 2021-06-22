package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop

class ShopDetailFragment constructor(val shop: Shop): Fragment(R.layout.fragment_shop_detail), View.OnClickListener, OnMapReadyCallback {

    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvSite: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnSurvey: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObj(view)
        showShop()
        fillField()
    }

    private fun initObj(view: View) {
        tvName = view.findViewById(R.id.tv_shopDetail_name)
        tvAddress = view.findViewById(R.id.tv_shopDetail_address)
        tvPhone = view.findViewById(R.id.tv_shopDetail_phone)
        tvSite = view.findViewById(R.id.tv_shopDetail_site)
        tvDescription = view.findViewById(R.id.tv_shopDetail_description)

        btnSurvey = view.findViewById(R.id.btn_shopDetail_survey)
        btnSurvey.setOnClickListener(this)
    }

    private fun fillField () {
        tvName.text = shop.name
        tvAddress.text = context?.getString(R.string.address_show , shop.address)
        tvPhone.text = shop.phone
        tvSite.text = shop.site
        tvDescription.text = shop.description
    }

    private fun showShop() {
        val map: SupportMapFragment = SupportMapFragment.newInstance()
        parentFragmentManager
                .beginTransaction()
                .add(R.id.btn_shopDetail_mapFrame , map)
                .commit()
        map.getMapAsync(this)
    }

    override fun onClick(v: View?) {
        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fl_shopDetail_surveyContainer, SurveyFragment(shop))
                ?.addToBackStack(null)
                ?.commit()
    }

    override fun onMapReady(map: GoogleMap) {
        showMarker(map , LatLng(shop.latitude.toDouble() , shop.longitude.toDouble()))
    }

    private fun showMarker(map: GoogleMap , loc: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(loc))
        map.animateCamera(CameraUpdateFactory.newLatLng(loc))
    }
}