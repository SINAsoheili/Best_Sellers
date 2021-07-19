package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowMetrics
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.util.CacheToPreference

class CitiesFragment : DialogFragment(R.layout.fragment_cities), AdapterView.OnItemClickListener {

    private lateinit var cities: Array<String>
    private lateinit var lvCities: ListView
    private lateinit var tvCurrentCity: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun init(view: View) {
        cities = resources.getStringArray(R.array.cities)

        lvCities = view.findViewById(R.id.lv_cityFragment)
        lvCities.setOnItemClickListener(this)
        prepareList()

        tvCurrentCity = view.findViewById(R.id.tv_cityFragment_currentCity)
        tvCurrentCity.text = getString(R.string.current_city_is , CacheToPreference.getCity(requireContext()))
    }

    private fun prepareList() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext() , android.R.layout.simple_list_item_1 , cities)
        lvCities.adapter = adapter
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val city: String = cities.get(position)
        CacheToPreference.registerCity(requireContext() , city)

        showMessage(getString(R.string.city_changed_to , city))
        closeDialog()
    }

    private fun showMessage(text: String) {
        Toast.makeText(requireContext() , text , Toast.LENGTH_SHORT).show()
    }

    private fun closeDialog() {
        this.dismiss()
    }
}