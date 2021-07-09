package net.sinasoheili.best_sellers.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Message

class CommentRecyclerViewAdapter(val context: Context, val items: List<Message>) : RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRecyclerViewHolder {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.list_item_recyclerview , parent , false)
        var vh: CommentRecyclerViewHolder = CommentRecyclerViewHolder(view)
        return vh
    }

    override fun onBindViewHolder(holder: CommentRecyclerViewHolder, position: Int) {
        holder.fill(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CommentRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tv_comment)

        public fun fill(msg : Message) {
            tv.text = msg.text
        }
    }
}