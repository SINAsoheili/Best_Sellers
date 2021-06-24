package net.sinasoheili.best_sellers.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Message
import java.util.zip.Inflater

class UserMessageAdapter (val adapterContext: Context , val messages: List<Message>) :
    ArrayAdapter<Message>(adapterContext, R.layout.list_item_user_message)
{
    override fun getCount(): Int {
        return messages.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if(convertView == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item_user_message , null , false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.fill(messages.get(position))
        return view
    }

    class ViewHolder(view: View) {
        private var tvText:TextView = view.findViewById(R.id.tv_userListMessage_text)

        fun fill(message: Message) {
            tvText.text = message.text
        }
    }
}