package net.sinasoheili.best_sellers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Criteria
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.ShopSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ShopSearchActivity : AppCompatActivity(), ChipGroup.OnCheckedChangeListener {

    @Inject
    lateinit var viewModel: ShopSearchViewModel

    private lateinit var shopListView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var categoryChipGroup: ChipGroup
    private lateinit var criteriaChipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_search)

        initObj()
        setObserver()
        viewModel.getCategories()
    }

    private fun initObj() {
        progressBar = findViewById(R.id.pb_shopSearch)

        shopListView = findViewById(R.id.lv_shopSearch_shops)

        categoryChipGroup = findViewById(R.id.chipGroup_shopSearch_categories)
        categoryChipGroup.setOnCheckedChangeListener(this)

        criteriaChipGroup = findViewById(R.id.chipGroup_shopSearch_criterias)
        criteriaChipGroup.setOnCheckedChangeListener(this)
    }

    private fun setObserver() {
        viewModel.categoriesData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showCategories(it.data)
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

        viewModel.criteriaData.observe(this , Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showCriterias(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.ConnectionError -> {
                    invisibleProgressBar()
                    showMessage(this.getString(R.string.connection_error))
                }
            }
        })

        viewModel.shopSearchData.observe(this ,  Observer {
            when(it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showShopList(it.data)
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

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun invisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showMessage(text: String) {
        Snackbar
            .make(shopListView , text, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    private fun showCategories(categories: List<ShopCategory>) {
        categoryChipGroup.removeAllViews()
        var first: Boolean = true
        for(category in categories) {
            if(first) {
                prepareChip(category.name , category.id, true , categoryChipGroup)
                first = false
            } else {
                prepareChip(category.name , category.id, false , categoryChipGroup)
            }
        }
    }

    private fun showCriterias(criterias: List<Criteria>) {
        criteriaChipGroup.removeAllViews()
        for(criteria in criterias) {
            prepareChip(criteria.name , criteria.id, false , criteriaChipGroup)
        }
    }

    private fun prepareChip(text: String , id: Int, checked: Boolean , chipGroup: ChipGroup) {
        val chip: Chip = Chip(this)
        chip.text = text
        val chipDrawable: ChipDrawable = ChipDrawable.createFromAttributes(
            this ,
            null ,
            0 ,
            R.style.Widget_MaterialComponents_Chip_Choice
        )
        chip.setChipDrawable(chipDrawable)
        chip.isChecked = checked
        chip.id = id
        chipGroup.addView(chip)
    }

    override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        when(group) {
            categoryChipGroup -> {
                viewModel.getCriteria(categoryChipGroup.checkedChipId)
            }

            criteriaChipGroup -> {
                for(i in criteriaChipGroup) {
                    val ch: Chip  = i as Chip
                    if (ch.isChecked) {
                        viewModel.searchShop(categoryChipGroup.checkedChipId , ch.id)
                    }
                }
            }
        }
    }

    private fun showShopList(shops: List<Shop>) {
        val adapter: ArrayAdapter<Shop> = ArrayAdapter(this , android.R.layout.simple_list_item_1 , shops)
        shopListView.adapter = adapter
    }
}