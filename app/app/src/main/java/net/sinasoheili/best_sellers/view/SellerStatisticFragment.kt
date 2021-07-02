package net.sinasoheili.best_sellers.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.model.Statistic
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.UserMessageAdapter
import net.sinasoheili.best_sellers.viewModel.SellerStatisticViewModel
import javax.inject.Inject


@AndroidEntryPoint
class SellerStatisticFragment: Fragment(R.layout.fragment_statistics_seller) {

    @Inject
    lateinit var viewModel: SellerStatisticViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var lvMessage: ListView
    private lateinit var barChart: BarChart
    private lateinit var tvEmptyList: TextView
    private lateinit var tvUserMessageTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.statusBarColor = requireContext().getColor(R.color.light_green)

        setObserver()
        viewModel.getStatistic()
        viewModel.getMessages()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
    }

    private fun initObj(view: View) {
        tvEmptyList = view.findViewById(R.id.tv_fragmentStatisticSeller_emptyList)

        progressBar = view.findViewById(R.id.pb_fragmentStatisticSeller)

        lvMessage = view.findViewById(R.id.lv_fragmentStatisticSeller_messages)

        barChart = view.findViewById(R.id.chart_fragmentStatisticSeller_statistic)
        initChart()

        tvUserMessageTitle = view.findViewById(R.id.tv_fragmentStatisticSeller_title)
    }

    private fun setObserver() {
        viewModel.statisticData.observe(viewLifecycleOwner, Observer {
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

        viewModel.messageData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success -> {
                    invisibleProgressBar()

                    if (it.data.isEmpty()) {
                        emptyListAlert()
                    } else {
                        showMessageList(it.data)
                    }
                }

                is DataState.Loading -> {
                    visibleProgressBar()
                }

                is DataState.Error -> {
                    invisibleProgressBar()
                    emptyListAlert()
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
        Snackbar.make(progressBar, text, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
    }

    private fun initChart() {
        barChart.description = null

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = 15F
        xAxis.setDrawGridLines(false)

        val yAxisRight: YAxis = barChart.axisRight
        val yAxisLeft: YAxis = barChart.axisLeft
        yAxisRight.isEnabled = false
        yAxisLeft.isEnabled = false
    }

    private fun showStatistic(statistics: ArrayList<Statistic>) {
        val entries: MutableList<BarEntry> = mutableListOf()

        for(i in 0 until statistics.size) {
            entries.add(BarEntry(i.toFloat(), statistics.get(i).amount))
        }

        val dataSet: BarDataSet = BarDataSet(entries, null)
        dataSet.color = requireContext().getColor(R.color.green)

        val barData: BarData = BarData(dataSet)
        barChart.data = barData
        barChart.invalidate()

        val titles: List<String> = List(statistics.size) { i: Int -> statistics.get(i).title }

        val xAxis: XAxis = barChart.xAxis
        xAxis.labelCount = titles.size
        xAxis.valueFormatter = object:IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return titles.get(value.toInt())
            }
        }
    }

    private fun showMessageList(messages: List<Message>) {
        lvMessage.visibility = View.VISIBLE
        tvUserMessageTitle.visibility = View.VISIBLE
        tvEmptyList.visibility =  View.GONE

        val adapter: UserMessageAdapter = UserMessageAdapter(requireContext() , messages)
        lvMessage.adapter = adapter
    }

    private fun emptyListAlert() {
        lvMessage.visibility = View.GONE
        tvUserMessageTitle.visibility = View.GONE
        tvEmptyList.visibility = View.VISIBLE
        tvEmptyList.text = requireContext().getString(R.string.message_not_found)
    }
}