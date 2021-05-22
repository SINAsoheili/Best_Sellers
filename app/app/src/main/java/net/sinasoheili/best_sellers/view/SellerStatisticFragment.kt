package net.sinasoheili.best_sellers.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Statistic
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.viewModel.SellerStatisticViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SellerStatisticFragment: Fragment(R.layout.fragment_statistics_seller) {

    @Inject
    lateinit var viewModel: SellerStatisticViewModel

    private lateinit var tvStatistic: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setObserver()
        viewModel.getStatistic()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
    }

    private fun initObj(view: View) {
        tvStatistic = view.findViewById(R.id.tv_fragmentStatisticSeller_statistic)

        progressBar = view.findViewById(R.id.pb_fragmentStatisticSeller)
    }

    private fun setObserver() {
        viewModel.statisticData.observe(viewLifecycleOwner , Observer {
            when (it) {
                is DataState.Success -> {
                    invisibleProgressBar()
                    showStatistic(it.data)
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.ConnectionError -> {
                    invisibleProgressBar()
                    showMessage(requireContext().getString(R.string.connection_error))
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
        Snackbar.make(progressBar , text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun showStatistic(statistics: ArrayList<Statistic>) {

        tvStatistic.text = ""
        for (i in statistics) {
            tvStatistic.append(i.toString()+"\n")
        }
    }
}