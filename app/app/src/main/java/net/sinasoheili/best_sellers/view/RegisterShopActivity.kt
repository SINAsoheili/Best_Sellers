package net.sinasoheili.best_sellers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.Observer
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
class RegisterShopActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var registerShopViewModel: RegisterShopViewModel

    private lateinit var tilShopName: TextInputLayout
    private lateinit var etShopName: TextInputEditText
    private lateinit var tilAddress: TextInputLayout
    private lateinit var etAddress: TextInputEditText
    private lateinit var chipGroup: ChipGroup
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_shop)

        initObj()
        setObserver()
        registerShopViewModel.getCategories()
    }

    private fun initObj() {
        tilShopName = findViewById(R.id.til_registerShop_name)
        etShopName = findViewById(R.id.et_registerShop_name)

        tilAddress = findViewById(R.id.til_registerShop_address)
        etAddress = findViewById(R.id.et_registerShop_address)

        chipGroup = findViewById(R.id.chipGroup_registerShop_categories)

        btnSubmit = findViewById(R.id.btn_registerShop_submit)
        btnSubmit.setOnClickListener(this)

        progressBar = findViewById(R.id.pb_registerShop)
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

    private fun showChips(categories: Array<ShopCategory>) {

        var first: Boolean = true

        for (category in categories) {
            if (first){
                chipGroup.addView( prepareChip(category.id , category.name , true) )
                first = false
            }
            else {
                chipGroup.addView( prepareChip(category.id , category.name , false) )
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

    private fun checkCategory() : Boolean {
        if (chipGroup.checkedChipId != -1) {
            return true
        } else {
            showMessage(this.getString(R.string.please_choose_shop_category))
            return false
        }
    }

    private fun checkValidInput() : Boolean = if (checkName() && checkAddress() && checkCategory()) true else false

    private fun getCheckedCategoryId(): Int {
        val chip: Chip = findViewById(chipGroup.checkedChipId)
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

    override fun onClick(v: View?) {
        when(v) {
            btnSubmit -> {
                if (checkValidInput()) {

                    //todo: send latitude and longitude of location
                    val shop: Shop = Shop (
                        name = etShopName.text.toString().trim(),
                        address = etAddress.text.toString().trim(),
                        idCategory = getCheckedCategoryId(),
                    )
                    registerShopViewModel.registerShop(shop)
                }
            }
        }
    }
}