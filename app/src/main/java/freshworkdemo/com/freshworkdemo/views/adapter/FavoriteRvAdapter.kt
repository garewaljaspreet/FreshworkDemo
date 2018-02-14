package freshworkdemo.com.freshworkdemo.views.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import freshworkdemo.com.freshworkdemo.R

/**
 * Adapter to show grid of favorite gifs in grid
 * @author Jass
 * @version 1.0
 */
class FavoriteRvAdapter(var userList: ArrayList<String>, val activity: Context): RecyclerView.Adapter<FavoriteRvAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItems(userList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.favorite_item_layout, parent, false)
        return ViewHolder(v,activity);
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View, val activity: Context) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : String){

            val imgGif:ImageView = itemView.findViewById(R.id.imgGif)
            Glide.with(activity)
                    .load(data)
                    .asGif()
                    .override(300,300)
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imgGif)

        }

    }


}