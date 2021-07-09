package net.sinasoheili.best_sellers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
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
import net.sinasoheili.best_sellers.util.ShopListAdapter
import net.sinasoheili.best_sellers.viewModel.ShopSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ShopSearchActivity : AppCompatActivity(), ChipGroup.OnCheckedChangeListener, SearchView.OnQueryTextListener {

    @Inject
    lateinit var viewModel: ShopSearchViewModel

    private lateinit var shopListView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var categoryChipGroup: ChipGroup
    private lateinit var criteriaChipGroup: ChipGroup
    private lateinit var searchView: SearchView

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

        searchView = findViewById(R.id.sv_shopSearch_categories_shopSearch)
        searchView.setOnQueryTextListener(this)
    }

    private fun setObserver() {
        viewModel.categoriesData.observe(this, Observer {
            when (it) {
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

        viewModel.criteriaData.observe(this, Observer {
            when (it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showCriterias(it.data)
                    searchShop()
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

        viewModel.shopSearchData.observe(this, Observer {
            when (it) {
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
            .make(shopListView, text, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    private fun showCategories(categories: List<ShopCategory>) {
        categoryChipGroup.removeAllViews()
        var first: Boolean = true
        for (category in categories) {
            if (first) {
                prepareChip(category.name, category.id, true, categoryChipGroup)
                first = false
            } else {
                prepareChip(category.name, category.id, false, categoryChipGroup)
            }
        }
    }

    private fun showCriterias(criterias: List<Criteria>) {
        var checked: Boolean = true; // set this boolean to show first item of list checked
        criteriaChipGroup.removeAllViews()
        for (criteria in criterias) {
            prepareChip(criteria.name, criteria.id, checked, criteriaChipGroup)
            if(checked)
                checked = false
        }
    }

    private fun prepareChip(text: String, id: Int, checked: Boolean, chipGroup: ChipGroup) {
        val chip: Chip = Chip(this)
        chip.text = text
        val chipDrawable: ChipDrawable = ChipDrawable.createFromAttributes(
            this,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Choice
        )
        chip.setChipDrawable(chipDrawable)
        chip.isChecked = checked
        chip.id = id
        chipGroup.addView(chip)
    }

    override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        when (group) {
            categoryChipGroup -> {
                viewModel.getCriteria(categoryChipGroup.checkedChipId)
            }

            criteriaChipGroup -> {
                searchShop()
            }
        }
    }

    private fun showShopList(shops: List<Shop>) {
        val adapter: ShopListAdapter =
            ShopListAdapter(this, shops)
        shopListView.adapter = adapter

        shopListView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val shop = shops.get(position)
                supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.fl_shopSearch_container, ShopDetailFragment(shop))
                    .commit()
            }

        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchShop()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText.isNullOrEmpty()) {
            searchShop()
            return true
        }
        return false
    }

    private fun searchShop() {
        val categoryId: Int = categoryChipGroup.checkedChipId
        var criteriaId : Int = -1
        for (i in criteriaChipGroup) {
            val ch: Chip = i as Chip
            if (ch.isChecked) {
                criteriaId = ch.id
                break
            }
        }
        val shopName: String = searchView.query.toString()
        viewModel.searchShop(categoryId , criteriaId  , shopName)
    }
}