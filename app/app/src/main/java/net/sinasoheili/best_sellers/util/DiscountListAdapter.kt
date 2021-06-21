package net.sinasoheili.best_sellers.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.ShopDiscount

class DiscountListAdapter constructor(context: Context ,val list: ArrayList<ShopDiscount>): ArrayAdapter<ShopDiscount>(context, R.layout.discount_list_item) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View
        val viewHolder: ViewHolder
        if(convertView == null) {
            view = layoutInflater.inflate(R.layout.discount_list_item , null , false)
            viewHolder = ViewHolder(context, view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.fill(list.get(position))
        return view
    }

    class ViewHolder constructor(val context: Context , view: View){

        private var tvShopName: TextView = view.findViewById(R.id.tv_userShopDiscount_name)
        private var tvShopSite: TextView = view.findViewById(R.id.tv_userShopDiscount_site)
        private var tvDiscountName: TextView = view.findViewById(R.id.tv_userShopDiscount_discount_name)
        private var tvDiscountAmount: TextView = view.findViewById(R.id.tv_userShopDiscount_discountAmount)

        fun fill(shopDiscount: ShopDiscount) {
            tvShopName.text = shopDiscount.shop.name
            tvShopSite.text = shopDiscount.shop.site
            tvDiscountName.text = context.getString(R.string.discount_name_show , shopDiscount.discount.title)
            tvDiscountAmount.text = context.getString(R.string.discount_percent , shopDiscount.discount.amount.toString())
        }
    }
}