package net.sinasoheili.best_sellers.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.CommentRecyclerViewAdapter
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.ShopSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ShopDetailFragment constructor(val shop: Shop ):
        Fragment(R.layout.fragment_shop_detail), View.OnClickListener, OnMapReadyCallback, SurveyFragment.CallBack{

    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvSite: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnSurvey: Button
    private lateinit var btnDirection: Button
    private lateinit var rvComment: RecyclerView
    private lateinit var tvComment: TextView
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var viewModel: ShopSearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObj(view)
        initObserver()
        viewModel.getShopComment(shop.id)
        showMap()
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

        btnDirection = view.findViewById(R.id.btn_shopDetail_direction)
        btnDirection.setOnClickListener(this)

        rvComment = view.findViewById(R.id.rv_shopDetail_comments)
        tvComment = view.findViewById(R.id.tv_shopDetail_comments)

        progressBar = view.findViewById(R.id.pb_shopDetail)
    }

    private fun initObserver() {
        viewModel.commentData.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showComment(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    inVisibleProgressBar()
                    showEmptyListAlert()
                }

                is DataState.ConnectionError -> {
                    inVisibleProgressBar()
                    showMessage(getString(R.string.connection_error))
                }
            }
        })
    }

    private fun fillField () {
        tvName.text = shop.name
        tvAddress.text = context?.getString(R.string.address_show , shop.address)
        tvPhone.text = shop.phone
        tvSite.text = shop.site
        tvDescription.text = shop.description
    }

    private fun showMap() {
        val map: SupportMapFragment = SupportMapFragment.newInstance()
        parentFragmentManager
                .beginTransaction()
                .add(R.id.btn_shopDetail_mapFrame , map)
                .commit()
        map.getMapAsync(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSurvey -> {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fl_shopDetail_surveyContainer, SurveyFragment(shop , this))
                    ?.addToBackStack(null)
                    ?.commit()
            }

            btnDirection -> {

                val uri: Uri = Uri.parse("google.navigation:q=${shop.latitude},${shop.longitude}")
                val intent: Intent = Intent(Intent.ACTION_VIEW , uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        showMarker(map , LatLng(shop.latitude.toDouble() , shop.longitude.toDouble()))
    }

    private fun showMarker(map: GoogleMap , loc: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(loc))
        map.animateCamera(CameraUpdateFactory.newLatLng(loc))
    }

    private fun showComment(messages: List<Message>) {
        rvComment.visibility = View.VISIBLE
        tvComment.visibility = View.GONE


        val layoutManager: LinearLayoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvComment.layoutManager = layoutManager

        val adapter: CommentRecyclerViewAdapter = CommentRecyclerViewAdapter(requireContext() , messages)
        rvComment.adapter = adapter
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String) {
        Snackbar
                .make(tvName , text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun showEmptyListAlert() {
        rvComment.visibility = View.GONE
        tvComment.visibility = View.VISIBLE

        tvComment.text = getString(R.string.message_not_found)
    }

    override fun onCloseCallBack() {
        viewModel.getShopComment(shop.id)
    }
}