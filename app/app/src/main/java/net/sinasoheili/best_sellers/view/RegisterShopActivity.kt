package net.sinasoheili.best_sellers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.RegisterShopViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RegisterShopActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    @Inject
    lateinit var registerShopViewModel: RegisterShopViewModel

    private lateinit var tilShopName: TextInputLayout
    private lateinit var etShopName: TextInputEditText
    private lateinit var tilShopPhone: TextInputLayout
    private lateinit var etShopPhone: TextInputEditText
    private lateinit var tilShopSite: TextInputLayout
    private lateinit var etShopSite: TextInputEditText
    private lateinit var tilShopDescription: TextInputLayout
    private lateinit var etShopDescription: TextInputEditText
    private lateinit var tilAddress: TextInputLayout
    private lateinit var etAddress: TextInputEditText
    private lateinit var categoriesChipGroup: ChipGroup
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var provinceChipGroup: ChipGroup

    private lateinit var map:GoogleMap
    private var location: LatLng = LatLng(35.6892 , 51.3890)
    private lateinit var provinces: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_shop)

        initObj()
        setObserver()
        registerShopViewModel.getCategories()
    }

    private fun initObj() {
        showMap()

        provinces = resources.getStringArray(R.array.cities)

        tilShopName = findViewById(R.id.til_registerShop_name)
        etShopName = findViewById(R.id.et_registerShop_name)

        tilAddress = findViewById(R.id.til_registerShop_address)
        etAddress = findViewById(R.id.et_registerShop_address)

        tilShopPhone = findViewById(R.id.til_registerShop_phone)
        etShopPhone = findViewById(R.id.et_registerShop_phone)

        tilShopSite = findViewById(R.id.til_registerShop_site)
        etShopSite = findViewById(R.id.et_registerShop_site)

        tilShopDescription = findViewById(R.id.til_registerShop_description)
        etShopDescription = findViewById(R.id.et_registerShop_description)

        categoriesChipGroup = findViewById(R.id.chipGroup_registerShop_categories)

        btnSubmit = findViewById(R.id.btn_registerShop_submit)
        btnSubmit.setOnClickListener(this)

        progressBar = findViewById(R.id.pb_registerShop)

        provinceChipGroup = findViewById(R.id.chipGroup_registerShop_province)
        showProvince()
    }

    private fun setObserver() {

        registerShopViewModel.shopDataState.observe(this, Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    startActivity(Intent(this , SellerMainPage::class.java))
                    finish()
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
                    showMessage(getString(R.string.connection_error))
                }
            }
        })

        registerShopViewModel.categoryDataState.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    inVisibleProgressBar()
                    showChips(it.data.toTypedArray())
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
                    showMessage(getString(R.string.connection_error))
                }
            }
        })
    }

    private fun showMap() {
        val supportMapFragment: SupportMapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_registerShop_mapContainer , supportMapFragment)
            .commit()

        supportMapFragment.getMapAsync(this)
    }

    private fun showChips(categories: Array<ShopCategory>) {

        var first: Boolean = true

        for (category in categories) {
            if (first){
                categoriesChipGroup.addView( prepareChip(category.id , category.name , true) )
                first = false
            }
            else {
                categoriesChipGroup.addView( prepareChip(category.id , category.name , false) )
            }
        }
    }

    private fun prepareChip(id: Int , text: String , checked: Boolean): Chip {

        val chip: Chip = Chip(this)
        chip.tag = id
        chip.text = text
        val chipDrawable: ChipDrawable = ChipDrawable.createFromAttributes(
            this,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Choice
        )
        chip.setChipDrawable(chipDrawable)
        chip.isChecked = checked
        return chip
    }

    private fun isEmpty(et: TextInputEditText) : Boolean =  et.text!!.isEmpty()

    private fun checkName() : Boolean {

        if (isEmpty(etShopName)) {
            tilShopName.error = tilShopName.context.getString(R.string.please_fill_field)
            return false
        } else {
            tilShopName.error = null
            return true
        }
    }

    private fun checkAddress() : Boolean {

        if (isEmpty(etAddress)) {
            tilAddress.error = tilAddress.context.getString(R.string.please_fill_field)
            return false
        } else {
            tilAddress.error = null
            return true
        }
    }

    private fun checkShopPhone() : Boolean {

        if (isEmpty(etShopPhone) || (etShopPhone.text.toString().length < 11)) {
            tilShopPhone.error = tilAddress.context.getString(R.string.please_fill_field)
            return false
        } else {
            tilShopPhone.error = null
            return true
        }
    }

    private fun checkDescription() : Boolean {

        if (isEmpty(etShopDescription)) {
            tilShopDescription.error = tilAddress.context.getString(R.string.please_fill_field)
            return false
        } else {
            tilShopDescription.error = null
            return true
        }
    }

    private fun checkCategory() : Boolean {
        if (categoriesChipGroup.checkedChipId != -1) {
            return true
        } else {
            showMessage(this.getString(R.string.please_choose_shop_category))
            return false
        }
    }

    private fun checkProvince() : Boolean {
        if (provinceChipGroup.checkedChipId != View.NO_ID) {
            return true
        } else {
            showMessage(this.getString(R.string.please_choose_shop_category))
            return false
        }
    }

    private fun checkValidInput() : Boolean =
            if (checkName() && checkShopPhone() && checkDescription() && checkAddress() && checkCategory() && checkProvince()) true else false

    private fun getCheckedCategoryId(): Int {
        val chip: Chip = findViewById(categoriesChipGroup.checkedChipId)
        return( chip.tag.toString().toInt())
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showMessage(text: String) {
        Snackbar
            .make(btnSubmit , text, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    private fun getSelectedProvince() : String {
        val chip: Chip = findViewById(provinceChipGroup.checkedChipId)

        for (province in provinces) {
            if (chip.text == province) {
                return province
            }
        }

        return "" //bad smell . this return never happen. because this function call after checkProvince function
    }

    override fun onClick(v: View?) {
        when(v) {
            btnSubmit -> {
                if (checkValidInput()) {

                    val site: String = if(isEmpty(etShopSite)) "" else etShopSite.text.toString()

                    val shop: Shop = Shop (
                        name = etShopName.text.toString().trim(),
                        phone = etShopPhone.text.toString().trim(),
                        description = etShopDescription.text.toString().trim(),
                        address = etAddress.text.toString().trim(),
                        idCategory = getCheckedCategoryId(),
                        latitude = location.latitude.toFloat(),
                        longitude =  location.longitude.toFloat(),
                        site = site,
                        city = getSelectedProvince()
                    )
                    registerShopViewModel.registerShop(shop)
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        showMarker(map , location)

        map.setOnMapClickListener(object:GoogleMap.OnMapClickListener {
            override fun onMapClick(loc: LatLng) {
                location = loc
                showMarker(map , loc)
            }
        })
    }

    private fun showMarker(map: GoogleMap , loc: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(loc))
        map.animateCamera(CameraUpdateFactory.newLatLng(loc))
    }

    private fun showProvince() {
        for (province in provinces) {

            val chip: Chip = Chip(this)
            chip.text = province
            chip.id = View.generateViewId()
            val chipDrawable: ChipDrawable = ChipDrawable.createFromAttributes(
                    this,
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Choice
            )
            chip.setChipDrawable(chipDrawable)

            provinceChipGroup.addView(chip)
        }
    }
}