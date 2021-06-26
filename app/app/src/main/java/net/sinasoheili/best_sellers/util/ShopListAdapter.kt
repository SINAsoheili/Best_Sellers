package net.sinasoheili.best_sellers.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop

class ShopListAdapter (private val adapterContext: Context , val items: List<Shop>)
    : ArrayAdapter<Shop>( adapterContext , R.layout.list_item_shop)
{
    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View?
        val viewHolder: ViewHolder?

        if(convertView == null) {
            val inflater: LayoutInflater = adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item_shop , null , false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.fill(items.get(position))
        return view!!
    }

    private class ViewHolder (view: View) {
        private val tvName: TextView = view.findViewById(R.id.tv_shopListItem_name)
        private val tvAddress: TextView = view.findViewById(R.id.tv_shopListItem_address)

        fun fill(shop: Shop) {
            tvName.text = shop.name
            tvAddress.text = shop.address
        }
    }
}